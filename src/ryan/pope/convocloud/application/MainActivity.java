package ryan.pope.convocloud.application;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ryan.pope.convocloud.R;
import ryan.pope.convocloud.business.ListenerManager;
import ryan.pope.convocloud.business.WordCloudManager;
import ryan.pope.convocloud.cloud.objects.WordInfo;
import ryan.pope.convocloud.objects.DataContact;
import ryan.pope.convocloud.objects.DataFile;
import ryan.pope.convocloud.objects.DataBase;
import ryan.pope.convocloud.persistance.DataAccess;
import ryan.pope.convocloud.presentation.ProgressDialogHelper;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MainActivity extends Activity 
{
	private static ListenerManager _listenerManager;
	private DataAccess _dataAccess;
	private ProgressDialogHelper _progressHelper;
	private TextView _statusTextView;
	private File _photoFile;

	private DataBase _selectData;
	private WordCloudManager _wordCloudManager;
	private boolean _smsCapable;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);

		doStartUp();
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		_progressHelper.end();
	}

	private void doStartUp() 
	{
		/* Remove title and menu bar */
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);

		/* Create all click listeners */
		_wordCloudManager = new WordCloudManager(this);
		_listenerManager = new ListenerManager();
		_listenerManager.setup(this, _wordCloudManager);

		_dataAccess = new DataAccess(this);

		_progressHelper = new ProgressDialogHelper(this);

		_selectData = null;
		_photoFile = null;

		/* Initialize views */
		_statusTextView = (TextView) findViewById(R.id.status_text);
		
		TelephonyManager manager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        _smsCapable = manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE ? false : true;
        
        if (Globals.DEBUG) Log.i(Globals.DEBUG_TAG, _smsCapable ? "SMS" : "No SMS");
	}
	
	public WordCloudManager getWordCloudManager()
	{
		return _wordCloudManager;
	}
	
	public boolean hasSMS()
	{
		return _smsCapable;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if(resultCode == RESULT_OK)
		{
			if(requestCode == Globals.CONTACT_SELECT_CODE)
			{
				_dataAccess.fetchData(data, Globals.Type.CONTACT);
			}
			if(requestCode == Globals.FILE_SELECT_CODE)
			{
	            _dataAccess.fetchData(data, Globals.Type.FILE);
			}
		}
	}

	public void setDataStore(DataBase dataToFetch)
	{
		_selectData = dataToFetch;
		if(_selectData != null)
		{
			runOnUiThread(new Runnable() 
			{
				@Override
				public void run() 
				{
					if(_selectData instanceof DataContact)
					{
						_statusTextView.setText("Word Count: " + _selectData.getWordCount() + "\nContact: " + ((DataContact)_selectData).getName());
					}
					else if(_selectData instanceof DataFile)
					{
						_statusTextView.setText("Word Count: " + _selectData.getWordCount() + "\nSelected File: " + ((DataFile)_selectData).getFileName());
					}
				}
			});
		}
	}

	public ProgressDialogHelper getProgressHelper()
	{
		return _progressHelper;
	}

	public ArrayList<WordInfo> getWords() 
	{
		return _selectData.getWordFrequencies();
	}

	@SuppressWarnings("deprecation")
	public void setBackground(File file) 
	{
		if(file != null)
		{
			_photoFile = file;
			Resources res = getResources();
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			final BitmapDrawable bd = new BitmapDrawable(res, bitmap);
			final View view = findViewById(R.id.main_layout);
			
	        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
	        {
				runOnUiThread(new Runnable() 
				{
					@Override
					public void run() 
					{
						view.setBackground(bd);
					}
				});
	        }
	        else
	        {
				runOnUiThread(new Runnable() 
				{
					@Override
					public void run() 
					{
						view.setBackgroundDrawable(bd);
					}
				});
	        }
		}

	}
	
	public void sendNotification()
	{
		if(!isInForeground())
		{
	        NotificationCompat.Builder builder =
	                new NotificationCompat.Builder(this)
	                        .setSmallIcon(R.drawable.smallicon)
	                        .setContentTitle("Convo Cloud")
	                        .setContentText("Your Cloud has completed!");
	
	        Intent targetIntent = new Intent(this, MainActivity.class);
	        targetIntent.setAction("android.intent.action.MAIN");
	        targetIntent.addCategory("android.intent.category.LAUNCHER");
	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	        builder.setContentIntent(contentIntent);
	        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	        nManager.notify(1, builder.build());
		}
	}

	public boolean hasPhotoLoaded() 
	{
		return _photoFile != null ? true : false;
	}
	
	public boolean isInForeground()
	{	    
		boolean isActivityFound = false;
		try
		{
		    ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		    List<RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);
	
	
		    if (services.get(0).topActivity.getPackageName().toString().equalsIgnoreCase(getPackageName().toString())) 
		    {
		        isActivityFound = true;
		    }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	    return isActivityFound;
	}

	public Uri getPhotoURI() 
	{
		return Uri.fromFile(_photoFile);
	}

	public boolean hasDataStoreLoaded() 
	{
		return _selectData != null ? true: false;
	}

	public DataBase getDataStore() 
	{
		return _selectData;
	}

	public DataAccess getDataManager() 
	{
		return _dataAccess;
	}
}

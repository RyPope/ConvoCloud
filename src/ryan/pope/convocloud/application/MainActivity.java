package ryan.pope.convocloud.application;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ryan.pope.convocloud.R;
import ryan.pope.convocloud.business.MainListenerManager;
import ryan.pope.convocloud.business.WordCloudManager;
import ryan.pope.convocloud.objects.DataBase;
import ryan.pope.convocloud.objects.DataType;
import ryan.pope.convocloud.persistance.DataAccess;
import ryan.pope.convocloud.persistance.SettingsAccess;
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
	private static MainListenerManager _listenerManager;
	private DataAccess _dataAccess;
	private ProgressDialogHelper _progressHelper;
	private TextView _statusTextView;
	private File _photoFile;

	private DataBase _selectData;
	private WordCloudManager _wordCloudManager;
	private SettingsAccess _settings;
	private boolean _smsCapable;

	private void doStartUp() 
	{
		/* Remove title and menu bar */
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);

		/* Create all click listeners */
		_wordCloudManager = new WordCloudManager(this);
		_listenerManager = new MainListenerManager();
		_listenerManager.setup(this, _wordCloudManager);

		_dataAccess = new DataAccess(this);
		_settings = new SettingsAccess(this);

		_progressHelper = new ProgressDialogHelper(this);

		_selectData = null;
		_photoFile = null;

		/* Initialize views */
		_statusTextView = (TextView) findViewById(R.id.status_text);
		
		TelephonyManager manager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        _smsCapable = manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE ? false : true;
		
		reloadProgress();
		
        if (Globals.DEBUG) Log.i(Globals.DEBUG_TAG, _smsCapable ? "SMS" : "No SMS");
	}
	
	private void reloadProgress()
	{
		setBackground(_settings.getRecentImage());

		if(_settings.getInProgress())
		{
			if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "Restoring Selected data");
			
			DataBase newBase = new DataBase();
			newBase.setName(_settings.getImageName());
			newBase.setFauxWordCount(_settings.getImageCount());
			setDataStore(newBase);
			_wordCloudManager.createCloud();
		}
		else
		{
			if(_settings.getImageCount() != 0)
			{
				DataBase newBase = new DataBase();
				newBase.setName(_settings.getImageName());
				newBase.setFauxWordCount(_settings.getImageCount());
				setDataStore(newBase);
			}
		}
		
	}

	public DataAccess getDataManager() 
	{
		return _dataAccess;
	}

	public DataBase getDataStore() 
	{
		return _selectData;
	}
	
	public Uri getPhotoURI() 
	{
		return Uri.fromFile(_photoFile);
	}
	
	public ProgressDialogHelper getProgressHelper()
	{
		return _progressHelper;
	}

	public WordCloudManager getWordCloudManager()
	{
		return _wordCloudManager;
	}

	public ArrayList<String> getWords() 
	{
		return _settings.getInProgress() ? _settings.getRemainingWords() : _selectData.getWords();
	}

	public boolean hasDataStoreLoaded() 
	{
		return (_selectData != null && _selectData.isLoaded());
	}

	public boolean hasPhotoLoaded() 
	{
		return _photoFile != null ? true : false;
	}

	public boolean hasSMS()
	{
		return _smsCapable;
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if(resultCode == RESULT_OK)
		{
			if(requestCode == Globals.CONTACT_SELECT_CODE)
			{
				_dataAccess.fetchData(data, DataType.CONTACT);
			}
			if(requestCode == Globals.FILE_SELECT_CODE)
			{
	            _dataAccess.fetchData(data, DataType.FILE);
			}
		}
	}
	
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

		_progressHelper.kill();
		if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "onStop()");
	}
	
	@Override
	protected void onPause()
	{

		super.onPause();
		
		if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "onPause()");
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		if(_wordCloudManager.isRunning())
		{
			_wordCloudManager.stop();
			
			_settings.loadSettings();
			if(_settings.getInProgress())
				sendNotification("Convo Cloud paused. Click to resume.");
		}
		if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "onDestroy()");
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "onResume()");
	}
	

	public void sendNotification(String message)
	{
		if(!isInForeground())
		{
	        NotificationCompat.Builder builder =
	                new NotificationCompat.Builder(this)
	                        .setSmallIcon(R.drawable.small_icon)
	                        .setContentTitle("Convo Cloud")
	                        .setContentText(message);
	        builder.setAutoCancel(true);
	        builder.setOnlyAlertOnce(true);
	
	        Intent targetIntent = new Intent(this, MainActivity.class);
	        targetIntent.setAction("android.intent.action.MAIN");
	        targetIntent.addCategory("android.intent.category.LAUNCHER");
	        targetIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	        builder.setContentIntent(contentIntent);
	        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	        nManager.notify(1, builder.build());
		}
	}

	@SuppressWarnings("deprecation")
	public void setBackground(File file) 
	{
		if(file != null && file.exists() && !file.isDirectory())
		{
			_photoFile = file;
			final View background = findViewById(R.id.main_layout);
			
			try
			{
				Resources res = getResources();
				Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
				final BitmapDrawable bd = new BitmapDrawable(res, bitmap);
				
		        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
		        {
					runOnUiThread(new Runnable() 
					{
						@Override
						public void run() 
						{
							background.setBackground(bd);
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
							background.setBackgroundDrawable(bd);
						}
					});
		        }
			}
			catch(Exception e)
			{
				e.printStackTrace();
				background.setBackgroundResource(R.drawable.main_logo);
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
					_statusTextView.setText("Word Count: " + _selectData.getWordCount() + "\nName: " + _selectData.getName());
				}
			});
			
			if(_selectData.isLoaded())
			{
				_settings.setImageName(_selectData.getName());
				_settings.setImageCount(_selectData.getWordCount());
				_settings.save();
			}
		}
	}
	
	public String getDataName()
	{
		String dataName = "None";
		
		if(_selectData != null)
		{

			dataName = _selectData.getName();

		}
		
		return dataName;
	}
	
	public int getDataCount()
	{
		int count = 0;
		
		if(_selectData != null)
		{
			count = _selectData.getWordCount();
		}
		
		return count;
	}
}

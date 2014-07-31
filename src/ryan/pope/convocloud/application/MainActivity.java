package ryan.pope.convocloud.application;

import java.io.File;
import java.util.ArrayList;

import ryan.pope.convocloud.R;
import ryan.pope.convocloud.business.ListenerManager;
import ryan.pope.convocloud.business.WordCloudManager;
import ryan.pope.convocloud.cloud.objects.WordFrequency;
import ryan.pope.convocloud.objects.Contact;
import ryan.pope.convocloud.persistance.ContactDataAccess;
import ryan.pope.convocloud.presentation.ProgressDialogHelper;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MainActivity extends Activity 
{
	private static ListenerManager _listenerManager;
	private ContactDataAccess _contactDataAccess;
	private ProgressDialogHelper _progressHelper;
	private TextView _statusTextView;
	private File _photoFile;

	private Contact _selectedContact;
	private WordCloudManager _wordCloudManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

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

		_contactDataAccess = new ContactDataAccess(this);

		_progressHelper = new ProgressDialogHelper(this);

		_selectedContact = null;
		_photoFile = null;

		/* Initialize views */
		_statusTextView = (TextView) findViewById(R.id.status_text);
	}
	
	public WordCloudManager getWordCloudManager()
	{
		return _wordCloudManager;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		_contactDataAccess.fetchContact(requestCode, resultCode, data);
	}

	public void setContact(Contact contactToFetch)
	{
		_selectedContact = contactToFetch;
		if(_selectedContact != null)
		{
			runOnUiThread(new Runnable() 
			{
				@Override
				public void run() 
				{
					_statusTextView.setText("Selected Contact: " + _selectedContact.getName());
				}
			});
		}
	}

	public ProgressDialogHelper getProgressHelper()
	{
		return _progressHelper;
	}

	public ArrayList<WordFrequency> getWordFrequencies() 
	{
		return _selectedContact.getWordFrequencies();
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

	public boolean hasPhotoLoaded() 
	{
		return _photoFile != null ? true : false;
	}

	public Uri getPhotoURI() 
	{
		return Uri.fromFile(_photoFile);
	}

	public boolean hasContactLoaded() 
	{
		return _selectedContact != null ? true: false;
	}

	public Contact getContact() 
	{
		return _selectedContact;
	}

	public ContactDataAccess getContactManager() 
	{
		return _contactDataAccess;
	}
}

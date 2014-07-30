package ryan.pope.convocloud.application;

import java.io.File;
import java.util.ArrayList;

import ryan.pope.convocloud.R;
import ryan.pope.convocloud.business.ListenerManager;
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

	private Contact selectedContact;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		doStartUp();

	}

	private void doStartUp() 
	{
		/* Remove title and menu bar */
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);

		/* Create all click listeners */
		_listenerManager = new ListenerManager();
		_listenerManager.setup(this);

		_contactDataAccess = new ContactDataAccess();

		_progressHelper = new ProgressDialogHelper(this);

		selectedContact = null;

		/* Initialize views */
		_statusTextView = (TextView) findViewById(R.id.status_text);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		_contactDataAccess.fetchContact(this, requestCode, resultCode, data);
	}

	public void setContact(Contact contactToFetch)
	{
		selectedContact = contactToFetch;
		if(selectedContact != null)
		{
			runOnUiThread(new Runnable() 
			{
				@Override
				public void run() 
				{
					_statusTextView.setText("Selected Contact: " + selectedContact.getName());
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
		return selectedContact.getWordFrequencies();
	}

	@SuppressWarnings("deprecation")
	public void setBackground(File file) 
	{
		Resources res = getResources();
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
		BitmapDrawable bd = new BitmapDrawable(res, bitmap);
		View view = findViewById(R.id.main_layout);
		
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            view.setBackground(bd);
        else
            view.setBackgroundDrawable(bd);

	}

}

package ryan.pope.convocloud.application;

import java.io.File;
import java.util.ArrayList;

import ryan.pope.convocloud.R;
import ryan.pope.convocloud.business.ListenerManager;
import ryan.pope.convocloud.cloud.objects.WordFrequency;
import ryan.pope.convocloud.objects.Contact;
import ryan.pope.convocloud.persistance.ContactDataAccess;
import ryan.pope.convocloud.presentation.ProgressDialogHelper;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity 
{
	private static ListenerManager _listenerManager;
	private ContactDataAccess _contactDataAccess;
	private ProgressDialogHelper _progressHelper;
	
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
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		_contactDataAccess.fetchContact(this, requestCode, resultCode, data);
	}

	public void setContact(Contact contactToFetch)
	{
		selectedContact = contactToFetch;
	}
	
	public ProgressDialogHelper getProgressHelper()
	{
		return _progressHelper;
	}
	
	public ArrayList<WordFrequency> getWordFrequencies() 
	{
		return selectedContact.getWordFrequencies();
	}

	public void setBackground(File file) 
	{
        Resources res = getResources();
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        BitmapDrawable bd = new BitmapDrawable(res, bitmap);
        View view = findViewById(R.id.main_layout);
        view.setBackground(bd);
		
	}

}

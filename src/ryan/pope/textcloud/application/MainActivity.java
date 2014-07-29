package ryan.pope.textcloud.application;

import java.util.ArrayList;

import ryan.pope.textcloud.R;
import ryan.pope.textcloud.business.ListenerManager;
import ryan.pope.textcloud.cloud.objects.WordFrequency;
import ryan.pope.textcloud.objects.Contact;
import ryan.pope.textcloud.persistance.ContactDataAccess;
import ryan.pope.textcloud.presentation.ProgressDialogHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

}

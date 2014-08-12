package ryan.pope.convocloud.business;

import android.app.Dialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import ryan.pope.convocloud.R;
import ryan.pope.convocloud.application.Globals;
import ryan.pope.convocloud.application.MainActivity;
import ryan.pope.convocloud.application.SettingsActivity;
import ryan.pope.convocloud.objects.DataContact;
import ryan.pope.convocloud.objects.DataFile;
import ryan.pope.convocloud.presentation.UIHelper;

public class ListenerManager 
{
	private Button sourceSelectionButton;
	private Button createTextCloudButton;
	private Button fileButton;
	private Button contactButton;
	private Button cancelButton;
	private ImageView visibilityButton;
	private ImageView shareButton;
	private ImageView settingsButton;
	private RelativeLayout backgroundLayout;
	
	private MainActivity _mainActivity;
	private UIHelper _UIHelper;
	private WordCloudManager _wordCloudManager;
	
	public void setup(MainActivity mainActivity, WordCloudManager wordCloudManager) 
	{
		_mainActivity = mainActivity;
		_wordCloudManager = wordCloudManager;
		_UIHelper = new UIHelper(_mainActivity);
		
		if(_mainActivity != null)
		{
			setupSourceListener();
			setupCreateTextCloudListener();
			setupVisiblityButtonListener();
			setupBackgroundListener();
			setupShareButtonListener();
			setupSettingsButtonListener();
			setupSelectionButtonListeners();
		}
	}

	private void setupSettingsButtonListener()
	{
		settingsButton = (ImageView) _mainActivity.findViewById(R.id.settings_button);
		settingsButton.setOnClickListener( new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(_mainActivity, SettingsActivity.class);
				_mainActivity.startActivity(intent);
			}
		});
		
	}

	private void setupSelectionButtonListeners()
	{
		Dialog dialogView = _UIHelper.getSelectionDialog();
		
		fileButton = (Button) dialogView.findViewById(R.id.select_file_button);
		contactButton = (Button) dialogView.findViewById(R.id.select_contact_button);
		cancelButton = (Button) dialogView.findViewById(R.id.select_cancel_button);
		
		fileButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				_UIHelper.dismissSelectionDialog();
				
			    Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
			    intent.setType("*/*"); 
			    intent.addCategory(Intent.CATEGORY_OPENABLE);

			    try 
			    {
			    	_mainActivity.startActivityForResult(Intent.createChooser(intent, "Select a File"), Globals.FILE_SELECT_CODE);
			    } catch (android.content.ActivityNotFoundException ex) 
			    {
			        Toast.makeText(_mainActivity, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
			    }
			}
			
		});
		
		contactButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				_UIHelper.dismissSelectionDialog();
				
				Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
				intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
				_mainActivity.startActivityForResult(intent, Globals.CONTACT_SELECT_CODE);
			}
			
		});
		
		cancelButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				_UIHelper.dismissSelectionDialog();
			}
			
		});
		
	}

	private void setupShareButtonListener() 
	{
		shareButton = (ImageView) _mainActivity.findViewById(R.id.share_button);
		shareButton.setOnClickListener( new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				if(Globals.DEBUG)Log.i(Globals.DEBUG_TAG, "Share Button clicked"); 
				if(_mainActivity.hasPhotoLoaded() && _mainActivity.hasDataStoreLoaded())
				{
					String shareMessage = "";
					Intent shareIntent = new Intent();
					shareIntent.setAction(Intent.ACTION_SEND);
					shareIntent.putExtra(Intent.EXTRA_STREAM, _mainActivity.getPhotoURI());
					
					if(_mainActivity.getDataStore() instanceof DataContact)
					{
						shareMessage = "My Convo Cloud with " + ((DataContact)_mainActivity.getDataStore()).getName() + ". Made with <TODO INSERT URL> for Android #ConvoCloud";	
					}
					else if(_mainActivity.getDataStore() instanceof DataFile)
					{
						shareMessage = "My Convo Cloud. Made with <TODO INSERT URL> for Android #ConvoCloud";	
					}
					shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
					shareIntent.setType("*/*");
					_mainActivity.startActivity(Intent.createChooser(shareIntent, "Share ConvoCloud with..."));
				}
				else
				{
					Intent shareIntent = new Intent();
					shareIntent.setAction(Intent.ACTION_SEND);
					shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Check out ConvoCloud for Android, TODO: LINK #ConvoCloud");
					shareIntent.setType("text/plain");
					_mainActivity.startActivity(Intent.createChooser(shareIntent, "Share ConvoCloud with..."));
				}
			}

		});
	}

	private void setupBackgroundListener() 
	{
		backgroundLayout = (RelativeLayout) _mainActivity.findViewById(R.id.main_layout);
		backgroundLayout.setOnClickListener( new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				if(Globals.DEBUG)Log.i(Globals.DEBUG_TAG, "Background Image clicked"); 
				if(!_UIHelper.layoutVisible())
					_UIHelper.toggleUI();
			}

		});
		
	}

	private void setupVisiblityButtonListener() 
	{
		visibilityButton = (ImageView) _mainActivity.findViewById(R.id.visible_button);
		visibilityButton.setOnClickListener( new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				if(Globals.DEBUG)Log.i(Globals.DEBUG_TAG, "Visibility Button clicked"); 
				_UIHelper.toggleUI();
			}

		});
	}

	private void setupCreateTextCloudListener() 
	{
		createTextCloudButton = (Button) _mainActivity.findViewById(R.id.create_text_cloud_button);
		createTextCloudButton.setOnClickListener( new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				_wordCloudManager.createCloud();
			}
		});
	}

	private void setupSourceListener() 
	{

		sourceSelectionButton = (Button) _mainActivity.findViewById(R.id.source_selection_button);
		sourceSelectionButton.setOnClickListener( new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				_UIHelper.displaySelectionDialog();
			}
		});
		
		
		
	}
}

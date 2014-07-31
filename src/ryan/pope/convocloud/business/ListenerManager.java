package ryan.pope.convocloud.business;

import java.io.File;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import ryan.pope.convocloud.R;
import ryan.pope.convocloud.application.Globals;
import ryan.pope.convocloud.application.MainActivity;
import ryan.pope.convocloud.cloud.background.RectangleBackground;
import ryan.pope.convocloud.cloud.objects.CollisionMode;
import ryan.pope.convocloud.cloud.objects.WordCloud;
import ryan.pope.convocloud.cloud.objects.WordFrequency;
import ryan.pope.convocloud.presentation.UIHelper;

public class ListenerManager 
{
	private Button contactSelectionButton;
	private Button createTextCloudButton;
	private ImageView visibilityButton;
	private ImageView shareButton;
	private RelativeLayout backgroundLayout;
	
	private MainActivity _mainActivity;
	private UIHelper _UIHelper;
	
	public void setup(MainActivity mainActivity) 
	{
		_mainActivity = mainActivity;
		_UIHelper = new UIHelper(_mainActivity);
		
		if(_mainActivity != null)
		{
			setupContactListener();
			setupCreateTextCloudListener();
			setupVisiblityButtonListener();
			setupBackgroundListener();
			setupShareButtonListener();
		}
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
				if(_mainActivity.hasPhotoLoaded() && _mainActivity.hasContactLoaded())
				{
					Intent shareIntent = new Intent();
					shareIntent.setAction(Intent.ACTION_SEND);
					shareIntent.putExtra(Intent.EXTRA_STREAM, _mainActivity.getPhotoURI());
					
					shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "My Convo Cloud with " + _mainActivity.getContact().getName() + ". Made with http://goo.gl/zEgP5B for Android #ConvoCloud");
					shareIntent.setType("image/png");
					_mainActivity.startActivity(Intent.createChooser(shareIntent, _mainActivity.getResources().getText(R.string.send_to)));
				}
				else
				{
					Intent shareIntent = new Intent();
					shareIntent.setAction(Intent.ACTION_SEND);
					shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Check out Convo Cloud for Android, http://goo.gl/zEgP5B #ConvoCloud");
					shareIntent.setType("text/plain");
					_mainActivity.startActivity(Intent.createChooser(shareIntent, _mainActivity.getResources().getText(R.string.send_to)));
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
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) 
			{
				Display display = _mainActivity.getWindowManager().getDefaultDisplay(); 
				int width = display.getWidth();
				int height = display.getHeight();
				
				ArrayList<WordFrequency> wordFrequencies = _mainActivity.getWordFrequencies();

				WordCloud wordCloud = new WordCloud(width, height, CollisionMode.PIXEL_PERFECT);
				wordCloud.setPadding(0);
				wordCloud.setBackground(new RectangleBackground(width, height));
				wordCloud.setTypeface(Typeface.createFromAsset(_mainActivity.getAssets(), "neue.otf"));
				wordCloud.build(wordFrequencies);
				
				File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				File file = new File(path, "/" + "wordcloud.png");
				wordCloud.writeToFile(file.getAbsolutePath());
				
				_mainActivity.setBackground(file);
			}
		});
	}

	private void setupContactListener() 
	{

		contactSelectionButton = (Button) _mainActivity.findViewById(R.id.contact_selection_button);
		contactSelectionButton.setOnClickListener( new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
				intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
				_mainActivity.startActivityForResult(intent, 1);
			}
		});
		
		
		
	}
}

package ryan.pope.textcloud.business;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import ryan.pope.textcloud.R;
import ryan.pope.textcloud.application.MainActivity;
import ryan.pope.textcloud.business.bg.RectangleBackground;
import ryan.pope.textcloud.business.font.scale.LinearFontScalar;
import ryan.pope.textcloud.business.palette.ColorPalette;

public class ListenerManager 
{
	private Button contactSelectionButton;
	private Button createTextCloudButton;
	
	private MainActivity _mainActivity;
	
	public void setup(MainActivity mainActivity) 
	{
		_mainActivity = mainActivity;
		
		if(_mainActivity != null)
		{
			setupContactListener();
			setupCreateTextCloudListener();
		}
	}

	private void setupCreateTextCloudListener() 
	{
		createTextCloudButton = (Button) _mainActivity.findViewById(R.id.create_text_cloud_button);
		createTextCloudButton.setOnClickListener( new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				final List<WordFrequency> wordFrequencies = _mainActivity.getWordFrequencies();

				final WordCloud wordCloud = new WordCloud(600, 600, CollisionMode.RECTANGLE);
				wordCloud.setPadding(0);
				wordCloud.setBackground(new RectangleBackground(600, 600));
				wordCloud.setColorPalette(new ColorPalette(Color.BLACK, Color.BLUE));
				wordCloud.setFontScalar(new LinearFontScalar(10, 40));
				wordCloud.build(wordFrequencies);
				wordCloud.writeToFile("output/wordcloud_rectangle.png");
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

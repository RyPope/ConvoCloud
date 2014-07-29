package ryan.pope.textcloud.business;

import java.io.File;
import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import ryan.pope.textcloud.R;
import ryan.pope.textcloud.application.MainActivity;
import ryan.pope.textcloud.cloud.background.RectangleBackground;
import ryan.pope.textcloud.cloud.font.scale.LinearFontScalar;
import ryan.pope.textcloud.cloud.objects.CollisionMode;
import ryan.pope.textcloud.cloud.objects.WordCloud;
import ryan.pope.textcloud.cloud.objects.WordFrequency;
import ryan.pope.textcloud.cloud.palette.ColorPalette;

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
				Display display = _mainActivity.getWindowManager().getDefaultDisplay(); 
				int width = display.getWidth();
				int height = display.getHeight();
				
				ArrayList<WordFrequency> wordFrequencies = _mainActivity.getWordFrequencies();

				WordCloud wordCloud = new WordCloud(width, height, CollisionMode.PIXEL_PERFECT);
				wordCloud.setPadding(0);
				wordCloud.setBackground(new RectangleBackground(width, height));
				//wordCloud.setColorPalette(new ColorPalette(Color.RED, Color.BLACK));
				wordCloud.setFontScalar(new LinearFontScalar(10, 100));
				wordCloud.build(wordFrequencies);
				
				File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				File file = new File(path, "/" + "wordcloud.png");
				wordCloud.writeToFile(file.getAbsolutePath());
				
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), "image/*");
				_mainActivity.startActivity(intent);
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

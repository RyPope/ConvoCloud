package ryan.pope.convocloud.business;

import java.io.File;
import java.util.ArrayList;

import android.graphics.Typeface;
import android.os.Environment;
import android.view.Display;
import ryan.pope.convocloud.application.MainActivity;
import ryan.pope.convocloud.cloud.background.RectangleBackground;
import ryan.pope.convocloud.cloud.objects.CollisionMode;
import ryan.pope.convocloud.cloud.objects.WordCloud;
import ryan.pope.convocloud.cloud.objects.WordFrequency;

public class WordCloudManager
{
	private MainActivity _mainActivity;
	private Thread _cloudThread;

	public WordCloudManager(MainActivity mainActivity)
	{
		_mainActivity = mainActivity;
		_cloudThread = new Thread()
		{
			@SuppressWarnings("deprecation")
			@Override
			public void run() 
			{
				_mainActivity.runOnUiThread(new Runnable() 
				{
				      @Override
				      public void run()
				      {
				    	  _mainActivity.getProgressHelper().showCloudProgressDialog("Creating ConvoCloud", "Creating Canvas.");
				      }
				});
				Display display = _mainActivity.getWindowManager().getDefaultDisplay(); 
				int width = display.getWidth();
				int height = display.getHeight();
				
				ArrayList<WordFrequency> wordFrequencies = _mainActivity.getWordFrequencies();

				WordCloud wordCloud = new WordCloud(_mainActivity.getProgressHelper(), width, height, CollisionMode.PIXEL_PERFECT);
				wordCloud.setPadding(0);
				wordCloud.setBackground(new RectangleBackground(width, height));
				wordCloud.setTypeface(Typeface.createFromAsset(_mainActivity.getAssets(), "neue.otf"));
				wordCloud.build(wordFrequencies);
				
				File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				File file = new File(path, "/" + "wordcloud.png");
				wordCloud.writeToFile(file.getAbsolutePath());
				
				_mainActivity.setBackground(file);
				
				_mainActivity.runOnUiThread(new Runnable() 
				{
				      @Override
				      public void run()
				      {
				  		_mainActivity.getProgressHelper().dismissCloudProgressDialog();
				      }
				});

			}
		};
	}

	public void createCloud() 
	{
		_cloudThread.start();
	}

}

package ryan.pope.convocloud.business;

import java.io.File;
import java.util.ArrayList;

import ryan.pope.convocloud.application.MainActivity;
import ryan.pope.convocloud.cloud.background.RectangleBackground;
import ryan.pope.convocloud.cloud.objects.CollisionMode;
import ryan.pope.convocloud.cloud.objects.WordCloud;
import ryan.pope.convocloud.cloud.objects.WordFrequency;
import android.graphics.Typeface;
import android.os.Environment;
import android.view.Display;

public class WordCloudThread implements Runnable 
{
	private MainActivity _mainActivity;
	private WordCloud _wordCloud;
	public WordCloudThread(MainActivity mainActivity) 
	{
		_mainActivity = mainActivity;
	}

	public void kill()
	{
		if(_wordCloud != null)
		{
			_wordCloud.kill();
		}
	}
	
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

		_wordCloud = new WordCloud(_mainActivity.getProgressHelper(), width, height, CollisionMode.PIXEL_PERFECT);
		_wordCloud.setPadding(0);
		_wordCloud.setBackground(new RectangleBackground(width, height));
		_wordCloud.setTypeface(Typeface.createFromAsset(_mainActivity.getAssets(), "neue.otf"));
		_wordCloud.build(wordFrequencies);
		
		File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File file = new File(path, "/" + "wordcloud.png");
		_wordCloud.writeToFile(file.getAbsolutePath());
		
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
}
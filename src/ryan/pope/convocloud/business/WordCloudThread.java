package ryan.pope.convocloud.business;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import ryan.pope.convocloud.application.MainActivity;
import ryan.pope.convocloud.cloud.objects.WordCloud;
import ryan.pope.convocloud.cloud.objects.WordInfo;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
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
	
	@SuppressLint("NewApi")
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
		
	    int width = 0, height = 0;
	    final DisplayMetrics metrics = new DisplayMetrics();
	    Display display = _mainActivity.getWindowManager().getDefaultDisplay();
	    Method mGetRawH = null, mGetRawW = null;

	    try 
	    {
	        // For JellyBean 4.2 (API 17) and onward
	        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
	            display.getRealMetrics(metrics);

	            width = metrics.widthPixels;
	            height = metrics.heightPixels;
	        } 
	        else 
	        {
	            mGetRawH = Display.class.getMethod("getRawHeight");
	            mGetRawW = Display.class.getMethod("getRawWidth");

	            try 
	            {
	                width = (Integer) mGetRawW.invoke(display);
	                height = (Integer) mGetRawH.invoke(display);
	            } 
	            catch (Exception e) 
	            {
	                e.printStackTrace();
	            }
	        }
	    } catch (NoSuchMethodException e3) 
	    {  
	        e3.printStackTrace();
            display.getRealMetrics(metrics);

            width = metrics.widthPixels;
            height = metrics.heightPixels;
	    }
		
		ArrayList<WordInfo> wordFrequencies = _mainActivity.getWords();

		_wordCloud = new WordCloud(_mainActivity.getProgressHelper(), width, height);
		_wordCloud.setTypeface(Typeface.createFromAsset(_mainActivity.getAssets(), "neue.otf"));
		_wordCloud.build(wordFrequencies);
		
		File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File file = new File(path, File.separator + "wordcloud.png");
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
		
		_mainActivity.sendNotification();

	}
}
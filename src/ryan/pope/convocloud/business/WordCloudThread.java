package ryan.pope.convocloud.business;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import ryan.pope.convocloud.application.Globals;
import ryan.pope.convocloud.application.MainActivity;
import ryan.pope.convocloud.cloud.objects.WordCloud;
import ryan.pope.convocloud.persistance.SettingsAccess;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class WordCloudThread implements Runnable 
{
	private MainActivity _mainActivity;
	private WordCloud _wordCloud;
	private SettingsAccess _settings;
	private boolean _forceStopped;
	
	public WordCloudThread(MainActivity mainActivity) 
	{
		_mainActivity = mainActivity;
		_forceStopped = false;
	}

	public void kill()
	{
		if(_wordCloud != null)
		{
			_forceStopped = false;
			_wordCloud.kill();
		}
	}
	
	public void stop()
	{
		if(_wordCloud != null)
		{
			_forceStopped = true;
			_wordCloud.kill();
		}
	}
	
	@SuppressLint("NewApi")
	@Override
	public void run() 
	{
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
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
	        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) 
	        {
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
	    } 
	    catch (NoSuchMethodException e3) 
	    {  
	        e3.printStackTrace();
            display.getRealMetrics(metrics);

            width = metrics.widthPixels;
            height = metrics.heightPixels;
	    }

		_settings = new SettingsAccess(_mainActivity);	
		
		_wordCloud = new WordCloud(_mainActivity.getProgressHelper(), width, height);
		_wordCloud.setTypeface(Typeface.createFromAsset(_mainActivity.getAssets(), "neue.otf"));
		_wordCloud.setBackgroundColor(_settings.getBackground());
		_wordCloud.setExcludedWords(_settings.getExcludedWords());
		_wordCloud.setScheme(_settings.getScheme());
		_wordCloud.setRotation(_settings.getRotation());
		
		ArrayList<String> wordList = _mainActivity.getWords();
		
		if(_settings.getInProgress())
		{
			_wordCloud.setBitmap(_settings.getImagePath().getAbsolutePath());
		}

		_wordCloud.build(wordList);
		
		File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		
		if(!_forceStopped)
		{
			File file = new File(path, File.separator + _mainActivity.getDataName() + "-convocloud.png");
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
			
			_settings.setInProgress(false);
			_settings.saveSettings();
			_mainActivity.sendNotification("Your Cloud has completed!");
		}
		else
		{ 
			if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "Saved partial bitmap ");
			File file = new File(path, File.separator + "tmp.png");
			_wordCloud.writeToFile(file.getAbsolutePath());
			
			_settings.setImagePath(file);
			_settings.setInProgress(true);
			_settings.setRemainingWords(_wordCloud.getRemaining());
			_settings.saveSettings();
		}

	}
}
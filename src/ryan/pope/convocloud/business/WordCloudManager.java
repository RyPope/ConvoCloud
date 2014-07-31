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
import ryan.pope.convocloud.persistance.ContactFetchThread;

public class WordCloudManager
{
	private MainActivity _mainActivity;
	private WordCloudThread _wordCloudThread;
	private Thread _cloudThread;

	public WordCloudManager(MainActivity mainActivity)
	{
		_mainActivity = mainActivity;
		_wordCloudThread = new WordCloudThread(_mainActivity);
	}

	public void createCloud() 
	{
		try 
		{
			if(_cloudThread != null)
			{
				_wordCloudThread.kill();
				_cloudThread.join();
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		_cloudThread = new Thread(_wordCloudThread);
		_cloudThread.start();
	}

}

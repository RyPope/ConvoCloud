package ryan.pope.convocloud.business;

import ryan.pope.convocloud.application.MainActivity;

public class WordCloudManager
{
	private MainActivity _mainActivity;
	private WordCloudThread _wordCloudThread;
	private Thread _cloudThread;

	public WordCloudManager(MainActivity mainActivity)
	{
		_mainActivity = mainActivity;
	}

	public void createCloud() 
	{
		_wordCloudThread = new WordCloudThread(_mainActivity);
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
	
	public void kill()
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
	}
	
	public void stop()
	{
		try 
		{
			if(_cloudThread != null)
			{
				_wordCloudThread.stop();
				_cloudThread.join();
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}

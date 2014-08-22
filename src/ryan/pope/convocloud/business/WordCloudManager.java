package ryan.pope.convocloud.business;

import ryan.pope.convocloud.application.MainActivity;

public class WordCloudManager
{
	private MainActivity _mainActivity;
	private WordCloudThread _wordCloudThread;
	private Thread _cloudThread;
	private boolean _running;

	public WordCloudManager(MainActivity mainActivity)
	{
		_mainActivity = mainActivity;
		_running = false;
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
		
		_running = true;
	}
	
	public void kill()
	{
		try 
		{
			if(_cloudThread != null)
			{
				_wordCloudThread.kill();
				_cloudThread.join();
				_running = false;
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
				_running = false;
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public boolean isRunning()
	{
		return _running;
	}

}

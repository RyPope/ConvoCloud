package ryan.pope.convocloud.persistance;

import android.content.Intent;
import ryan.pope.convocloud.application.MainActivity;
import ryan.pope.convocloud.objects.DataType;

public class DataAccess 
{
	private Thread _dataAccessThread;
	private MainActivity _mainActivity;
	private DataFetchThread _contactFetchThread;
	
	public DataAccess(MainActivity mainActivity)
	{
		_mainActivity = mainActivity;
	}
	
	public void fetchData(Intent data, DataType dataType)
	{
		try 
		{
			if(_dataAccessThread != null)
			{
				_contactFetchThread.kill();
				_dataAccessThread.join();
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		_contactFetchThread = new DataFetchThread(_mainActivity, data, dataType);
		_dataAccessThread = new Thread(_contactFetchThread);
		_dataAccessThread.start();
	}
	
	public void kill()
	{
		try 
		{
			if(_dataAccessThread != null)
			{
				_contactFetchThread.kill();
				_dataAccessThread.join();
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}

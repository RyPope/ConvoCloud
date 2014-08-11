package ryan.pope.convocloud.persistance;

import android.content.Intent;
import ryan.pope.convocloud.application.MainActivity;

public class ContactDataAccess 
{
	private Thread _dataAccessThread;
	private MainActivity _mainActivity;
	private ContactFetchThread _contactFetchThread;
	
	public ContactDataAccess(MainActivity mainActivity)
	{
		_mainActivity = mainActivity;
	}
	
	public void fetchContact(int requestCode, int resultCode, Intent data)
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
		
		_contactFetchThread = new ContactFetchThread(_mainActivity, data);
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

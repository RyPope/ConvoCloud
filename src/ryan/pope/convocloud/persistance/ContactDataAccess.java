package ryan.pope.convocloud.persistance;

import android.content.Intent;
import ryan.pope.convocloud.application.MainActivity;

public class ContactDataAccess 
{
	private Thread _dataAccessThread;
	public void fetchContact(MainActivity mainActivity, int requestCode, int resultCode, Intent data)
	{
		_dataAccessThread = new Thread(new ContactFetchThread(mainActivity, requestCode, resultCode, data));
		_dataAccessThread.start();
	}
}

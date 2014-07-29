package ryan.pope.convocloud.persistance;

import android.content.Intent;
import ryan.pope.convocloud.application.MainActivity;

public class ContactDataAccess 
{
	public void fetchContact(MainActivity mainActivity, int requestCode, int resultCode, Intent data)
	{
		new Thread(new ContactFetchThread(mainActivity, requestCode, resultCode, data)).start();
	}
}

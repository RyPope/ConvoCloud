package ryan.pope.textcloud.persistance;

import android.content.Intent;
import ryan.pope.textcloud.application.MainActivity;

public class ContactDataAccess 
{
	public void fetchContact(MainActivity mainActivity, int requestCode, int resultCode, Intent data)
	{
		new Thread(new ContactFetchThread(mainActivity, requestCode, resultCode, data)).start();
	}
}

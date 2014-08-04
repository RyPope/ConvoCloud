package ryan.pope.convocloud.persistance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import ryan.pope.convocloud.application.Globals;
import ryan.pope.convocloud.application.MainActivity;
import ryan.pope.convocloud.objects.Contact;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

public class ContactFetchThread implements Runnable 
{
	private MainActivity _mainActivity;
	private Intent _data;
	private boolean _running = true;

	public ContactFetchThread(MainActivity mainActivity, int requestCode, int resultCode, Intent data) 
	{
		_mainActivity = mainActivity;
		_data = data;
	}

	@Override
	public void run() 
	{
		_mainActivity.getProgressHelper().showContactProgressDialog("Fetching Conversation", "Finding conversation...");

		Contact contactToFetch = fetchContact();

		if(contactToFetch != null)
		{
			if(_mainActivity.isTablet())
			{
				fetchStubData(contactToFetch);
			}
			else
			{
				fetchContactMessages(contactToFetch);
			}
		}

		_mainActivity.setContact(contactToFetch);

		_mainActivity.getProgressHelper().dismissContactProgressDialog();


	}

	private void fetchStubData(Contact contactToFetch) 
	{
		try 
		{
			File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			File file = new File(path, File.separator + "stub.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));  
			String line;   
			int i = 0;
			while ((line = br.readLine()) != null) 
			{
				i++;
				contactToFetch.addMessage(line);

				if(i % 10 == 0)
				{
					_mainActivity.getProgressHelper().changeContactDialogMessage("Fetching message " + i);
				}
			} 
			
			br.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private void fetchContactMessages(Contact contactToFetch) 
	{
		boolean threadFound = false;
		String threadID = "";
		String contactNumber = contactToFetch.getPhoneNumber();

		/* Find Thread ID with the contact,
		 * there may be multiple thread IDs if the client has used
		 * mass messaging.
		 */

		if(contactToFetch != null)
		{
			String[] projection = new String[]{"thread_id", "address", "body"};
			Cursor findContactCursor = _mainActivity.getContentResolver().query(Uri.parse("content://sms/"), projection, null, null, null);

			if(findContactCursor != null && findContactCursor.moveToFirst())
			{

				for(int i=0; i < findContactCursor.getCount() && !threadFound; i++)
				{

					String thread_id = findContactCursor.getString(findContactCursor.getColumnIndexOrThrow("thread_id"));
					String number = findContactCursor.getString(findContactCursor.getColumnIndexOrThrow("address"));

					findContactCursor.moveToNext();

					if(number != null)
					{

						if(number.equalsIgnoreCase(contactNumber) && !threadFound)
						{
							if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "Correct thread found with ID: " + thread_id);

							threadFound = true;
							threadID = thread_id;
						}
					}
				}
			}

			/* Get all messages from that thread */
			if(!threadID.equals(""))
			{
				String selection = "thread_id=" + threadID;
				final Cursor findThreadCursor = _mainActivity.getContentResolver().query(Uri.parse("content://sms/"), projection, selection, null, null);
				if(findThreadCursor != null && findThreadCursor.moveToFirst())
				{
					for(int i = 0; i < findThreadCursor.getCount() && _running; i++)
					{
						String number = findThreadCursor.getString(findThreadCursor.getColumnIndexOrThrow("address"));
						String sms = findThreadCursor.getString(findThreadCursor.getColumnIndexOrThrow("body")).toString();

						findThreadCursor.moveToNext();

						if(number != null)
						{
							if (Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "Message: " + sms);
							contactToFetch.addMessage(sms);

							if(i % 10 == 0)
							{
								int msgCount = i;

								_mainActivity.getProgressHelper().changeContactDialogMessage("Fetching message " + msgCount + " of " + findThreadCursor.getCount());

							}

						}
					}

					if (Globals.DEBUG) Log.i(Globals.DEBUG_TAG, contactToFetch.getMessages());

				}
				else
				{
					//No messages found
				}
				
				if(findThreadCursor != null)
				{
					findThreadCursor.close();
				}
			}
			
			if(findContactCursor != null)
			{
				findContactCursor.close();
			}
		}
	}

	private Contact fetchContact() 
	{

		Contact contactToFetch = null;
		Uri contactURI = null;

		if(_data != null)
		{
			contactURI = _data.getData();

			if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, _data.getDataString());
		}

		if (contactURI != null) 
		{
			Cursor contactSelectionCursor = null;
			try {
				contactSelectionCursor = _mainActivity.getContentResolver().query(contactURI, new String[]
						{ 
						ContactsContract.CommonDataKinds.Phone.NUMBER,  
						ContactsContract.CommonDataKinds.Phone.TYPE,
						ContactsContract.CommonDataKinds.Phone._ID,
						ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
						null, null, null);

				if (contactSelectionCursor != null && contactSelectionCursor.moveToFirst()) 
				{
					String number = contactSelectionCursor.getString(0);
					int type = contactSelectionCursor.getInt(1);
					String id = contactSelectionCursor.getString(2);
					String name = contactSelectionCursor.getString(3);

					if (Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "Selected Contact: " + name); 

					contactToFetch = new Contact(id, number, type, name);
				}
			} 
			finally 
			{
				if (contactSelectionCursor != null) 
				{
					contactSelectionCursor.close();
				}

			}

		}

		return contactToFetch;

	}
	
	public void kill()
	{
		_running = false;
	}

}

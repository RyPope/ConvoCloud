package ryan.pope.convocloud.persistance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import ryan.pope.convocloud.application.Globals;
import ryan.pope.convocloud.application.MainActivity;
import ryan.pope.convocloud.objects.DataContact;
import ryan.pope.convocloud.objects.DataFile;
import ryan.pope.convocloud.presentation.UIHelper;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

public class DataFetchThread implements Runnable 
{
	private MainActivity _mainActivity;
	private Intent _data;
	private boolean _running;
	private Globals.Type _dataType;
	private UIHelper _UIHelper;

	public DataFetchThread(MainActivity mainActivity, Intent data, Globals.Type dataType) 
	{
		_mainActivity = mainActivity;
		_data = data;
		_dataType = dataType;
		_running = true;
	}

	@Override
	public void run() 
	{
		if(_dataType == Globals.Type.CONTACT)
		{
			_mainActivity.getProgressHelper().showContactProgressDialog("Fetching Conversation", "Finding conversation...");

			DataContact contact = fetchContact();

			if(contact != null)
			{
				if(_mainActivity.hasSMS())
				{
					fetchContactMessages(contact);
				}
				else
				{
					_UIHelper.notifyNoSMS();
				}
			}

			_mainActivity.setDataStore(contact);
		}
		else if (_dataType == Globals.Type.FILE)
		{
			_mainActivity.getProgressHelper().showContactProgressDialog("Fetching File", "Finding conversation...");

			DataFile dataFile = fetchFile();
			_mainActivity.setDataStore(dataFile);
		}

		_mainActivity.getProgressHelper().dismissContactProgressDialog();
	}

	private DataFile fetchFile() 
	{
		DataFile fileToFetch = new DataFile(_data.getData());	
		InputStream is = null;
		try 
		{
			is = _mainActivity.getContentResolver().openInputStream(fileToFetch.getFile());

			BufferedReader r = new BufferedReader(new InputStreamReader(is));
			String line;
			int i = 0;
			while ((line = r.readLine()) != null) 
			{
				i++;
				fileToFetch.addWords(line);

				if(i % 10 == 0)
				{
					_mainActivity.getProgressHelper().changeContactDialogMessage("Fetching message " + i);
				}
			}

			is.close();

		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		return fileToFetch;
	}

	private void fetchContactMessages(DataContact contactToFetch) 
	{
		String contactNumber = contactToFetch.getPhoneNumber();

		String[] projection = new String[]{"address", "body"};
		String selection = "address=" + contactNumber;
		final Cursor findThreadCursor = _mainActivity.getContentResolver().query(Uri.parse("content://sms/"), projection, selection, null, null);
		if(findThreadCursor != null && findThreadCursor.moveToFirst())
		{
			for(int i = 0; i < findThreadCursor.getCount() && _running; i++)
			{
				String sms = findThreadCursor.getString(findThreadCursor.getColumnIndexOrThrow("body")).toString();
				findThreadCursor.moveToNext();

				if(sms != null)
				{
					if (Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "Message: " + sms);
					contactToFetch.addWords(sms);
				}

				if(i % 10 == 0)
				{
					int msgCount = i;
					_mainActivity.getProgressHelper().changeContactDialogMessage("Fetching message " + msgCount + " of " + findThreadCursor.getCount());
				}
			}

			if (Globals.DEBUG) Log.i(Globals.DEBUG_TAG, contactToFetch.getWords());

		}
		else
		{
			_UIHelper.notifyNoMessages();
		}

		if(findThreadCursor != null)
		{
			findThreadCursor.close();
		}
	}

	private DataContact fetchContact() 
	{

		DataContact contactToFetch = null;
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
					String name = contactSelectionCursor.getString(3);

					if (Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "Selected Contact: " + name); 

					contactToFetch = new DataContact(name, number);
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

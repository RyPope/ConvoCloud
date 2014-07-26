package ryan.pope.textcloud.business;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import ryan.pope.textcloud.R;
import ryan.pope.textcloud.application.MainActivity;

public class ListenerManager 
{
	private Button contactSelectionButton;
	
	private MainActivity _mainActivity;
	
	public void setup(MainActivity mainActivity) 
	{
		_mainActivity = mainActivity;
		
		if(_mainActivity != null)
		{
			setupContactListener();
		}
	}

	private void setupContactListener() 
	{

		contactSelectionButton = (Button) _mainActivity.findViewById(R.id.contact_selection_button);
		contactSelectionButton.setOnClickListener( new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
				intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
				_mainActivity.startActivityForResult(intent, 1);
			}
		});
		
		
		
	}
}

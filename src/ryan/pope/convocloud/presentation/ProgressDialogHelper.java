package ryan.pope.convocloud.presentation;

import android.app.ProgressDialog;
import android.util.Log;
import ryan.pope.convocloud.application.Globals;
import ryan.pope.convocloud.application.MainActivity;
import ryan.pope.convocloud.R;

public class ProgressDialogHelper 
{
	private ProgressDialog _contactProgressDialog;
	private ProgressDialog _cloudProgressDialog;
	private MainActivity _mainActivity;

	public ProgressDialogHelper(MainActivity mainActivity) 
	{
		_mainActivity = mainActivity;
	}

	public void showContactProgressDialog(final String title, final String message)
	{
		_mainActivity.runOnUiThread(new Runnable() 
		{
			@Override
			public void run()
			{
				if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "Showing progress dialog"); 
				_contactProgressDialog = new ProgressDialog(_mainActivity);
				_contactProgressDialog.setTitle(title);
				_contactProgressDialog.setMessage(message);
				_contactProgressDialog.setIcon(R.drawable.smallicon);
				_contactProgressDialog.setCancelable(true);
				_contactProgressDialog.show();
			}
		});
	}

	public void dismissContactProgressDialog()
	{
		_mainActivity.runOnUiThread(new Runnable() 
		{
			@Override
			public void run()
			{
				if(_contactProgressDialog != null && _contactProgressDialog.isShowing())
					_contactProgressDialog.dismiss();
			}
		});
	}

	public void changeContactDialogMessage(final String message) 
	{
		_mainActivity.runOnUiThread(new Runnable() 
		{
			@Override
			public void run()
			{
				if(_contactProgressDialog != null)
				{
					_contactProgressDialog.setMessage(message);
				}
			}
		});
	}

	public void showCloudProgressDialog(final String title, final String message)
	{
		_mainActivity.runOnUiThread(new Runnable() 
		{
			@Override
			public void run()
			{
				if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "Showing cloud progress dialog"); 
				_cloudProgressDialog = new ProgressDialog(_mainActivity);
				_cloudProgressDialog.setTitle(title);
				_cloudProgressDialog.setMessage(message);
				_cloudProgressDialog.setIcon(R.drawable.smallicon);
				_cloudProgressDialog.setCancelable(true);
				_cloudProgressDialog.show();
			}
		});
	}

	public void dismissCloudProgressDialog()
	{
		_mainActivity.runOnUiThread(new Runnable() 
		{
			@Override
			public void run()
			{
				if(_cloudProgressDialog != null && _cloudProgressDialog.isShowing())
					_cloudProgressDialog.dismiss();
			}
		});
	}

	public void changeCloudDialogMessage(final String message) 
	{
		_mainActivity.runOnUiThread(new Runnable() 
		{
			@Override
			public void run()
			{
				if(_cloudProgressDialog != null)
				{
					_cloudProgressDialog.setMessage(message);
				}
			}
		});
	}

	public void end() 
	{
		dismissCloudProgressDialog();
		dismissContactProgressDialog();
		
	}

}

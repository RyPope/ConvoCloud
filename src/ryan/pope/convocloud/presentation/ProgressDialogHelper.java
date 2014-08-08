package ryan.pope.convocloud.presentation;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.WindowManager;
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
			@SuppressWarnings("deprecation")
			@Override
			public void run()
			{
				if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "Showing progress dialog"); 
				_contactProgressDialog = new ProgressDialog(_mainActivity);
				_contactProgressDialog.setTitle(title);
				_contactProgressDialog.setMessage(message);
				_contactProgressDialog.setIcon(R.drawable.smallicon);
				_contactProgressDialog.setCanceledOnTouchOutside(false);
				_contactProgressDialog.setCancelable(false);
				_contactProgressDialog.setButton("Continue", new DialogInterface.OnClickListener() 
			    {
			        public void onClick(DialogInterface dialog, int which) 
			        {
			    		changeContactDialogMessage("Ending Search early...");
			        	_mainActivity.getContactManager().kill();
			        	return;
			        }
			    });
				_contactProgressDialog.show();
				
				setScreenFlag(true);
			}
		});
	}
	
	protected void setScreenFlag(boolean flag)
	{
		if(flag)
		{
			_mainActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		else
		{
			_mainActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}

	public void dismissContactProgressDialog()
	{
		_mainActivity.runOnUiThread(new Runnable() 
		{
			@Override
			public void run()
			{
				if(_contactProgressDialog != null && _contactProgressDialog.isShowing())
				{
					_contactProgressDialog.dismiss();
					setScreenFlag(false);
				}
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
			@SuppressWarnings("deprecation")
			@Override
			public void run()
			{
				if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "Showing cloud progress dialog"); 
				_cloudProgressDialog = new ProgressDialog(_mainActivity);
				_cloudProgressDialog.setTitle(title);
				_cloudProgressDialog.setMessage(message);
				_cloudProgressDialog.setIcon(R.drawable.smallicon);
				_cloudProgressDialog.setCanceledOnTouchOutside(false);
				_cloudProgressDialog.setCancelable(false);
				_cloudProgressDialog.setButton("Continue", new DialogInterface.OnClickListener() 
			    {
			        public void onClick(DialogInterface dialog, int which) 
			        {
			    		changeCloudDialogMessage("Ending ConvoCloud early...");
			        	_mainActivity.getWordCloudManager().kill();
			        	return;
			        }
			    });
				_cloudProgressDialog.show();
				
				setScreenFlag(true);
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
				{
					_cloudProgressDialog.dismiss();
					setScreenFlag(false);
				}
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

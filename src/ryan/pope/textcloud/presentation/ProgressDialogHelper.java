package ryan.pope.textcloud.presentation;

import android.app.ProgressDialog;
import android.util.Log;
import ryan.pope.textcloud.R;
import ryan.pope.textcloud.application.Globals;
import ryan.pope.textcloud.application.MainActivity;

public class ProgressDialogHelper 
{
	private ProgressDialog _progressDialog;
	private MainActivity _mainActivity;

	public ProgressDialogHelper(MainActivity mainActivity) 
	{
		_mainActivity = mainActivity;
	}
	
	public void showProgressDialog(final String title, final String message)
	{
		if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "Showing progress dialog"); 
		_progressDialog = new ProgressDialog(_mainActivity);
		_progressDialog.setTitle(title);
		_progressDialog.setMessage(message);
		_progressDialog.setIcon(R.drawable.smallicon);
		_progressDialog.show();
		//_progressDialog = ProgressDialog.show(_mainActivity, title, message, true);
	}
	
	public void dismissProgressDialog()
	{
		if(_progressDialog != null)
			_progressDialog.dismiss();
	}

	public void changeMessage(String message) 
	{
		if(_progressDialog != null)
		{
			_progressDialog.setMessage(message);
		}
	}

}

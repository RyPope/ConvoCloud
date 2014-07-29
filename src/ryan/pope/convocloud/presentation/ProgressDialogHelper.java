package ryan.pope.convocloud.presentation;

import android.app.ProgressDialog;
import android.util.Log;
import ryan.pope.convocloud.application.Globals;
import ryan.pope.convocloud.application.MainActivity;
import ryan.pope.convocloud.R;

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

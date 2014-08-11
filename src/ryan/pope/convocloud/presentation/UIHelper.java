package ryan.pope.convocloud.presentation;

import android.app.Dialog;
import android.view.View;
import android.widget.LinearLayout;
import ryan.pope.convocloud.R;
import ryan.pope.convocloud.application.MainActivity;

public class UIHelper 
{
	private MainActivity _mainActivity;
	private LinearLayout _iconLayout;
	private LinearLayout _buttonLayout;
	private LinearLayout _statusLayout;
	private Dialog _selectionDialog;
	
	private boolean _uiVisible = true;
	
	public UIHelper(MainActivity mainActivity)
	{
		_mainActivity = mainActivity;
		initViews();
		initDialogs();
	}
	
	private void initDialogs()
	{
		_selectionDialog = new Dialog(_mainActivity);
		_selectionDialog.setContentView(R.layout.selection_dialog);
		_selectionDialog.setTitle("Select source...");
		_selectionDialog.setCanceledOnTouchOutside(false);
		_selectionDialog.setCancelable(true);

	}

	private void initViews() 
	{
		_iconLayout = (LinearLayout) _mainActivity.findViewById(R.id.icon_layout);
		_buttonLayout = (LinearLayout) _mainActivity.findViewById(R.id.button_layout);
		_statusLayout = (LinearLayout) _mainActivity.findViewById(R.id.status_layout);
		
	}

	public void toggleUI()
	{
		if(_uiVisible)
		{
			_iconLayout.setVisibility(View.INVISIBLE);
			_buttonLayout.setVisibility(View.INVISIBLE);
			_statusLayout.setVisibility(View.INVISIBLE);
		}
		else
		{
			_iconLayout.setVisibility(View.VISIBLE);
			_buttonLayout.setVisibility(View.VISIBLE);
			_statusLayout.setVisibility(View.VISIBLE);
		}
		
		_uiVisible = !_uiVisible;
	}

	public boolean layoutVisible() 
	{
		return _uiVisible;
	}
	
	public void displaySelectionDialog()
	{
		if(_selectionDialog != null && !_selectionDialog.isShowing())
		{
			_selectionDialog.show();
		}
	}

	public void dismissSelectionDialog()
	{
		if(_selectionDialog != null && _selectionDialog.isShowing())
		{
			_selectionDialog.dismiss();
		}
	}
	
	public Dialog getSelectionDialog()
	{
		return _selectionDialog;
	}

	public void notifyNoSMS()
	{
		// TODO Auto-generated method stub
		
	}

	public void notifyNoMessages()
	{
		// TODO Auto-generated method stub
		
	}

}

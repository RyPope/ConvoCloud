package ryan.pope.convocloud.presentation;

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
	
	private boolean _uiVisible = true;
	
	public UIHelper(MainActivity mainActivity)
	{
		_mainActivity = mainActivity;
		initViews();
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

}

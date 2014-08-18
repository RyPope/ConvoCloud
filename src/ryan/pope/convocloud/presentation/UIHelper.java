package ryan.pope.convocloud.presentation;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import ryan.pope.convocloud.R;
import ryan.pope.convocloud.objects.RotationType;
import ryan.pope.convocloud.objects.Scheme;

public class UIHelper 
{
	private Activity _activity;
	private LinearLayout _iconLayout;
	private LinearLayout _buttonLayout;
	private LinearLayout _statusLayout;
	private Dialog _selectionDialog;
	
	private boolean _uiVisible = true;
	private Spinner _colourSchemeSpinner;
	private Spinner _rotationSpinner;
	
	public UIHelper(Activity activity)
	{
		_activity = activity;
		initViews();
		initDialogs();
	}

	public void dismissSelectionDialog()
	{
		if(_selectionDialog != null && _selectionDialog.isShowing())
		{
			_selectionDialog.dismiss();
		}
	}

	public void displaySelectionDialog()
	{
		if(_selectionDialog != null && !_selectionDialog.isShowing())
		{
			_selectionDialog.show();
		}
	}

	public Dialog getSelectionDialog()
	{
		return _selectionDialog;
	}

	private void initDialogs()
	{
		_selectionDialog = new Dialog(_activity);
		_selectionDialog.setContentView(R.layout.selection_dialog);
		_selectionDialog.setTitle("Select source...");
		_selectionDialog.setCanceledOnTouchOutside(false);
		_selectionDialog.setCancelable(true);

	}
	
	private void initViews() 
	{
		_iconLayout = (LinearLayout) _activity.findViewById(R.id.icon_layout);
		_buttonLayout = (LinearLayout) _activity.findViewById(R.id.button_layout);
		_statusLayout = (LinearLayout) _activity.findViewById(R.id.status_layout);
		
	}

	public boolean layoutVisible() 
	{
		return _uiVisible;
	}
	
	public void notifyNoData()
	{
		_activity.runOnUiThread(new Runnable() 
		{
			@Override
			public void run()
			{
				Toast.makeText(_activity, "No source selected", Toast.LENGTH_SHORT).show();
			}
		});
		
	}

	public void notifyNoMessages(final String contactName)
	{
		_activity.runOnUiThread(new Runnable() 
		{
			@Override
			public void run()
			{
				Toast.makeText(_activity, "No messages found with " + contactName, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void notifyNoSMS()
	{
		_activity.runOnUiThread(new Runnable() 
		{
			@Override
			public void run()
			{
				Toast.makeText(_activity, "You do not have an SMS enabled device.", Toast.LENGTH_SHORT).show();
	
			}
		});
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

	public void setupSchemeSpinner(ArrayList<String> schemeList, Scheme selection)
	{
		_colourSchemeSpinner = (Spinner) _activity.findViewById(R.id.select_scheme_spinner);
        ArrayAdapter<String> schemeAdapter = new ArrayAdapter<String>(_activity, R.layout.spinner_item, schemeList);
         
        schemeAdapter.setDropDownViewResource(R.layout.spinner_item_drop);
         
        _colourSchemeSpinner.setAdapter(schemeAdapter);
        _colourSchemeSpinner.setSelection(schemeList.indexOf(selection.name()));
		
	}
	
	public void setupRotationSpinner(ArrayList<String> rotationList, RotationType selection)
	{
		_rotationSpinner = (Spinner) _activity.findViewById(R.id.select_rotation_spinner);
        ArrayAdapter<String> rotationAdapter = new ArrayAdapter<String>(_activity, R.layout.spinner_item, rotationList);
         
        rotationAdapter.setDropDownViewResource(R.layout.spinner_item_drop);
         
        _rotationSpinner.setAdapter(rotationAdapter);
        _rotationSpinner.setSelection(rotationList.indexOf(selection.name()));
		
	}

}

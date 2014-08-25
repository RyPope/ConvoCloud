package ryan.pope.convocloud.presentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
	private Dialog _excludedDialog;
	
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
		_selectionDialog.setTitle(_activity.getString(R.string.select_source));
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
				Toast.makeText(_activity, _activity.getString(R.string.reselect), Toast.LENGTH_SHORT).show();
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
				Toast.makeText(_activity, _activity.getString(R.string.no_messages) + contactName, Toast.LENGTH_SHORT).show();
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
				Toast.makeText(_activity, _activity.getString(R.string.no_sms), Toast.LENGTH_SHORT).show();
	
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

	public void displayExcludedDialog(ArrayList<String> excludedWords)
	{
		
		EditText excludedBox = (EditText) _excludedDialog.findViewById(R.id.excluded_words);
		
		excludedBox.setText("");
		
		if(excludedWords != null)
		{
			for(String word : excludedWords)
			{
				excludedBox.append(word + ", ");
			}
		}
			
		
		excludedBox.setSelection(excludedBox.getText().length());
		
		_excludedDialog.show();
	}

	public void initExcludedDialog()
	{
		_excludedDialog = new Dialog(_activity);
		_excludedDialog.setContentView(R.layout.exclude_words_dialog);
		_excludedDialog.setTitle(_activity.getString(R.string.excluded_words));
		_excludedDialog.setCanceledOnTouchOutside(false);
		_excludedDialog.setCancelable(true);
	}
	
	public Dialog getExcludedDialog()
	{
		return _excludedDialog;
	}

	public void dismissExcludedDialog()
	{
		if(_excludedDialog != null && _excludedDialog.isShowing())
		{
			_excludedDialog.dismiss();
		}
	}

	public ArrayList<String> getExcludedWords()
	{
		ArrayList<String> excludedWords = new ArrayList<String>();
		if(_excludedDialog != null && _excludedDialog.isShowing())
		{
			EditText excludedBox = (EditText) _excludedDialog.findViewById(R.id.excluded_words);
			
			String text = excludedBox.getText().toString();
			text = text.toUpperCase(Locale.getDefault()).replaceAll("[^A-Za-z0-9,]", "");
			
			String[] list = text.split(",");
			excludedWords = new ArrayList<String>(Arrays.asList(list));
			
		}
		
		return excludedWords;
	}

	public void displayNoFile()
	{
		_activity.runOnUiThread(new Runnable() 
		{
			@Override
			public void run()
			{
				Toast.makeText(_activity, _activity.getString(R.string.no_file_app), Toast.LENGTH_SHORT).show();
			}
		});
		
	}

}

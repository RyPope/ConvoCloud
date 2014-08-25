package ryan.pope.convocloud.business;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import ryan.pope.convocloud.R;
import ryan.pope.convocloud.application.Globals;
import ryan.pope.convocloud.application.SettingsActivity;
import ryan.pope.convocloud.objects.Scheme;
import ryan.pope.convocloud.objects.RotationType;
import ryan.pope.convocloud.persistance.SettingsAccess;
import ryan.pope.convocloud.presentation.ColorPickerDialog;
import ryan.pope.convocloud.presentation.ColorPickerDialog.OnColorSelectedListener;
import ryan.pope.convocloud.presentation.UIHelper;

public class SettingsListenerManager
{
	private SettingsActivity _settingsActivity;
	private SettingsAccess _settings;
	private Button _backgroundColorButton;
	private Button _saveButton;
	private Button _cancelButton;
	private Button _excludedButton;
	private Button _excludedSaveButton;
	private Button _excludedCancelButton;
	private Spinner _schemeSpinner;
	private Spinner _rotationSpinner;
	private UIHelper _UIHelper;

	public void setup(SettingsActivity settingsActivity, SettingsAccess settings)
	{
		_settingsActivity = settingsActivity;
		_settings = settings;
		_settings = new SettingsAccess(_settingsActivity);
		_UIHelper = new UIHelper(settingsActivity);
		
		if(_settingsActivity != null)
		{
			setupColorListener();
			setupSaveListener();
			setupSchemeListener();
			setupCancelListener();
			setupRotationListener();
			setupExcludedListener();
			setupExcludedDialogListeners();
		}
	}

	private void setupExcludedDialogListeners()
	{
		_excludedCancelButton = (Button) _UIHelper.getExcludedDialog().findViewById(R.id.excluded_cancel_button);
		_excludedCancelButton.setOnClickListener( new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				_UIHelper.dismissExcludedDialog();
			}
		});
		
		_excludedSaveButton = (Button) _UIHelper.getExcludedDialog().findViewById(R.id.excluded_save_button);
		_excludedSaveButton.setOnClickListener( new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				_settings.setExcludedWords(_UIHelper.getExcludedWords());
            	if(Globals.DEBUG)Log.i(Globals.DEBUG_TAG, "Excluded words: " + _UIHelper.getExcludedWords().toString());
				_UIHelper.dismissExcludedDialog();
			}
		});
		
	}

	private void setupExcludedListener()
	{
		_UIHelper.initExcludedDialog();
		_excludedButton = (Button) _settingsActivity.findViewById(R.id.select_words_button);
		_excludedButton.setOnClickListener( new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				_UIHelper.displayExcludedDialog(_settings.getExcludedWords());
			}
		});
		
	}

	private void setupCancelListener()
	{
		_cancelButton = (Button) _settingsActivity.findViewById(R.id.settings_cancel_button);
		_cancelButton.setOnClickListener( new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				_settingsActivity.onBackPressed();
			}
		});
		
	}

	private void setupSchemeListener()
	{
		_schemeSpinner = (Spinner) _settingsActivity.findViewById(R.id.select_scheme_spinner);
		_schemeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) 
            {

            	_settings.setScheme(Scheme.valueOf(_settings.getSchemeList().get(position)));
            	if(Globals.DEBUG)Log.i(Globals.DEBUG_TAG, "Rotation selected: " + _settings.getSchemeList().get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) 
            {
            	
            }
        });
		
	}
	
	private void setupRotationListener()
	{
		_rotationSpinner = (Spinner) _settingsActivity.findViewById(R.id.select_rotation_spinner);
		_rotationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) 
            {

            	_settings.setRotation(RotationType.valueOf(_settings.getRotationList().get(position)));
            	if(Globals.DEBUG)Log.i(Globals.DEBUG_TAG, "Scheme selected: " + _settings.getRotationList().get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) 
            {
            	
            }
        });
		
	}

	private void setupSaveListener()
	{
		_saveButton = (Button) _settingsActivity.findViewById(R.id.settings_save_button);
		_saveButton.setOnClickListener( new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				_settings.save();
				_settingsActivity.onBackPressed();
			}
		});
		
	}

	private void setupColorListener()
	{
		_backgroundColorButton = (Button) _settingsActivity.findViewById(R.id.select_background_button);
		_backgroundColorButton.setOnClickListener( new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				int initialColor = _settings.getBackground();
			    ColorPickerDialog colorPickerDialog = new ColorPickerDialog(_settingsActivity, initialColor, new OnColorSelectedListener() 
			    {

					@Override
					public void onColorSelected(int color)
					{
						if(Globals.DEBUG)Log.i(Globals.DEBUG_TAG, "Colour selected: " + color);
						_settings.setBackground(color);
					}


			    });
			    
			    colorPickerDialog.show();
			}
		});
	}
}

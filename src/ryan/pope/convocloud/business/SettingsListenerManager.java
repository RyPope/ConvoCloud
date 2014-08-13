package ryan.pope.convocloud.business;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import ryan.pope.convocloud.R;
import ryan.pope.convocloud.application.Globals;
import ryan.pope.convocloud.application.SettingsActivity;
import ryan.pope.convocloud.persistance.SettingsAccess;
import ryan.pope.convocloud.presentation.ColorPickerDialog;
import ryan.pope.convocloud.presentation.ColorPickerDialog.OnColorSelectedListener;

public class SettingsListenerManager
{
	private SettingsActivity _settingsActivity;
	private SettingsAccess _settings;
	private Button _backgroundColorButton;

	public void setup(SettingsActivity settingsActivity)
	{
		_settingsActivity = settingsActivity;
		_settings = new SettingsAccess(_settingsActivity);
		
		if(_settingsActivity != null)
		{
			setupColorListener();
		}
	}

	private void setupColorListener()
	{
		_backgroundColorButton = (Button) _settingsActivity.findViewById(R.id.select_background_button);
		_backgroundColorButton.setOnClickListener( new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				int initialColor = Color.BLACK;
			    ColorPickerDialog colorPickerDialog = new ColorPickerDialog(_settingsActivity, initialColor, new OnColorSelectedListener() 
			    {

					@Override
					public void onColorSelected(int color)
					{
						if(Globals.DEBUG)Log.i(Globals.DEBUG_TAG, "Colour selected: " + color);
					}


			    });
			    
			    colorPickerDialog.show();
			}
		});
	}
}

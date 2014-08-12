package ryan.pope.convocloud.business;

import com.chiralcode.colorpicker.ColorPickerDialog;
import com.chiralcode.colorpicker.ColorPickerDialog.OnColorSelectedListener;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import ryan.pope.convocloud.R;
import ryan.pope.convocloud.application.Globals;
import ryan.pope.convocloud.application.SettingsActivity;

public class SettingsListenerManager
{
	private SettingsActivity _settingsActivity;
	private Button _backgroundColorButton;

	public void setUp(SettingsActivity settingsActivity)
	{
		_settingsActivity = settingsActivity;
		
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

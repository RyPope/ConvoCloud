package ryan.pope.convocloud.application;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class SettingsActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		if (Globals.DEBUG)Log.i(Globals.DEBUG_TAG, "Settings activity loaded.");
		
		
	}
}

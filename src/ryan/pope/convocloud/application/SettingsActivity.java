package ryan.pope.convocloud.application;

import ryan.pope.convocloud.R;
import ryan.pope.convocloud.business.SettingsListenerManager;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class SettingsActivity extends Activity
{
	private SettingsListenerManager _settingsListener;
	private void doStartUp()
	{
		/* Remove title and menu bar */
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_settings);
		
		_settingsListener = new SettingsListenerManager();
		_settingsListener.setup(this);
	}

	private void endTransition()
	{
	    finish();
	    overridePendingTransition(R.anim.slide_right_left_out, R.anim.slide_right_left_in);
	}
	
	@Override
	public void onBackPressed() 
	{
		endTransition();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		if (Globals.DEBUG)Log.i(Globals.DEBUG_TAG, "Settings activity loaded.");
		
		doStartUp();
	}
}

package ryan.pope.convocloud.persistance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.preference.PreferenceManager;
import ryan.pope.convocloud.application.Globals;
import ryan.pope.convocloud.objects.RotationType;
import ryan.pope.convocloud.objects.Scheme;

public class SettingsAccess
{
	private static String BACKGROUND = "Background";
	private static String SCHEME = "Scheme";
	private static String ROTATION = "Rotation";
	private static String EXCLUDED = "Excluded";
	
	private Context _context;
	private Scheme _scheme;
	
	private int _background;

	private ArrayList<String> _excludedWords;

	private RotationType _rotation;

	private SharedPreferences _sharedPrefs;

	public SettingsAccess(Context context)
	{
		_context = context;
		_sharedPrefs = PreferenceManager.getDefaultSharedPreferences(_context);
		
		loadSettings();
	}

	public int getBackground()
	{
		return _background;
	}

	public ArrayList<String> getExcludedWords()
	{
		return _excludedWords;
	}

	public RotationType getRotation()
	{
		return _rotation;
	}

	public Scheme getScheme()
	{
		return _scheme;
	}
	public void loadSettings()
	{
		try
		{
			_background = _sharedPrefs.getInt(BACKGROUND, Color.WHITE);
			_scheme = Scheme.valueOf(_sharedPrefs.getString(SCHEME, Scheme.Default.name()));
			_rotation = RotationType.valueOf(_sharedPrefs.getString(ROTATION, RotationType.RANDOM.name()));
			_excludedWords = new ArrayList<String>(_sharedPrefs.getStringSet(EXCLUDED, new HashSet<String>()));
		}
		catch(IllegalArgumentException e)
		{
			_background = Color.WHITE;
			_scheme = Scheme.Default;
			_rotation = RotationType.RANDOM;
			_excludedWords = new ArrayList<String>();
		}
	}
	public void saveSettings()
	{
		Editor prefsEditor = _sharedPrefs.edit();
		
		prefsEditor.putInt(BACKGROUND, _background);
		prefsEditor.putString(SCHEME, _scheme.name());
		prefsEditor.putString(ROTATION, _rotation.name());
		prefsEditor.putStringSet(EXCLUDED, new HashSet<String>(_excludedWords));
		
		prefsEditor.apply();
	}
	public void setBackground(int background)
	{
		_background = background;
	}
	
	public void setExcludedWords(ArrayList<String> excludedWords)
	{
		_excludedWords = excludedWords;
	}

	public void setRotation(RotationType rotation)
	{
		_rotation = rotation;
	}
	
	public void setScheme(Scheme scheme)
	{
		_scheme = scheme;
	}

	public ArrayList<String> getSchemeList()
	{
		return new ArrayList<String>(Arrays.asList(Globals.SCHEME_LIST));
	}
	
	public ArrayList<String> getRotationList()
	{
		return new ArrayList<String>(Arrays.asList(Globals.ROTATION_LIST));
	}
}

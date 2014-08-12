package ryan.pope.convocloud.objects;

import ryan.pope.convocloud.application.MainActivity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

public class DataFile extends DataStore
{
	private Uri _filePath;
	private String _fileName;
	private MainActivity _mainActivity;
	public DataFile(MainActivity mainActivity, Uri filePath)
	{
		super();
		_mainActivity = mainActivity;
		_filePath = filePath;
		_fileName = parseFileForName();
	}
	
	private String parseFileForName()
	{
		String fileName = "File";
		try
		{
		    Cursor returnCursor = _mainActivity.getContentResolver().query(_filePath, null, null, null, null);
		    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
		    returnCursor.moveToFirst();
		    fileName = returnCursor.getString(nameIndex);
		    
		    returnCursor.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	    return fileName;
	}

	public String getFileName()
	{
		return _fileName;
	}
	
	public Uri getFile()
	{
		return _filePath;
	}
}

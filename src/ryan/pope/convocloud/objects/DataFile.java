package ryan.pope.convocloud.objects;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

public class DataFile extends DataBase
{
	private Uri _filePath;
	private ContentResolver _resolver;
	public DataFile(ContentResolver resolver, Uri filePath)
	{
		super();
		_resolver = resolver;
		_filePath = filePath;
		super.setName(parseFileForName());
	}
	
	public Uri getFile()
	{
		return _filePath;
	}
	
	private String parseFileForName()
	{
		String fileName = _filePath.getLastPathSegment();
		try
		{
		    Cursor returnCursor = _resolver.query(_filePath, null, null, null, null);
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
}

package ryan.pope.convocloud.objects;

import java.io.File;

import android.net.Uri;

public class DataFile extends DataStore
{
	private Uri _filePath;
	private String _fileName;
	public DataFile(Uri filePath)
	{
		super();
		_filePath = filePath;
		_fileName = filePath.getLastPathSegment();
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

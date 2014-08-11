package ryan.pope.convocloud.objects;

import java.io.File;

import android.net.Uri;

public class DataFile extends DataStore
{
	private File _filePath;
	private String _fileName;
	public DataFile(Uri filePath)
	{
		super();
		_filePath = new File(filePath.getPath());
		_fileName = filePath.getLastPathSegment();
	}
	
	public String getFileName()
	{
		return _fileName;
	}
	
	public File getFile()
	{
		return _filePath;
	}
}

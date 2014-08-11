package ryan.pope.convocloud.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ryan.pope.convocloud.application.Globals;
import ryan.pope.convocloud.cloud.objects.WordInfo;

public class DataStore 
{
	private String _phoneNumber;
	private String _name;
	private HashMap<String, Integer> _allWords;
	
	public DataStore(String id, String number, int type, String name)
	{
		_phoneNumber = stripNumber(number);
		_allWords = new HashMap<String, Integer>();
		_name = name;
	}

	private String stripNumber(String number) 
	{
		return number.replaceAll("[^0-9]", "");
	}

	public String getPhoneNumber() 
	{
		return _phoneNumber;
	}

	public void addMessage(String sms) 
	{
		/* Parse each word and count them */
		String parsedSMS = sms.replaceAll("[^A-Za-z0-9 ]+", "");
		String[] splitSMS = parsedSMS.toUpperCase(Locale.getDefault()).split("\\s+");
		for(String s : splitSMS)
		{
			if(s.length() >= Globals.MIN_MESSAGE_SIZE && !Globals.STOP_WORDS.contains(s))
			{
				if(_allWords.get(s) == null)
					_allWords.put(s, 1);
				else
					_allWords.put(s, _allWords.get(s) + 1);
			}
		}
	}
	
	public String getMessages()
	{
		String allMessages = "";
		for (Map.Entry<String, Integer> entry : _allWords.entrySet())
		{
			allMessages = allMessages + entry.getKey() + ":" + entry.getValue() + " ";
		}
		
		return allMessages;
	}

	public ArrayList<WordInfo> getWordFrequencies() 
	{
		ArrayList<WordInfo> _wordFreqList = new ArrayList<WordInfo>();
		for (Map.Entry<String, Integer> entry : _allWords.entrySet())
		{
			_wordFreqList.add(new WordInfo(entry.getKey(), entry.getValue()));
		}
		
		return _wordFreqList;
	}
	
	public void setName(String name)
	{
		_name = name;
	}
	
	public String getName()
	{
		return _name;
	}
}

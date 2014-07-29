package ryan.pope.convocloud.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ryan.pope.convocloud.application.Globals;
import ryan.pope.convocloud.cloud.objects.WordFrequency;

public class Contact 
{
	private String _phoneNumber;
	private HashMap<String, Integer> _allMessages;
	
	public Contact(String id, String number, int type, String name)
	{
		_phoneNumber = stripNumber(number);
		_allMessages = new HashMap<String, Integer>();
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
			if(s.length() >= Globals.MIN_MESSAGE_SIZE)
			{
				if(_allMessages.get(s) == null)
					_allMessages.put(s, 1);
				else
					_allMessages.put(s, _allMessages.get(s) + 1);
			}
		}
	}
	
	public String getMessages()
	{
		String allMessages = "";
		for (Map.Entry<String, Integer> entry : _allMessages.entrySet())
		{
			allMessages = allMessages + entry.getKey() + ":" + entry.getValue() + " ";
		}
		
		return allMessages;
	}

	public ArrayList<WordFrequency> getWordFrequencies() 
	{
		ArrayList<WordFrequency> _wordFreqList = new ArrayList<WordFrequency>();
		for (Map.Entry<String, Integer> entry : _allMessages.entrySet())
		{
			_wordFreqList.add(new WordFrequency(entry.getKey(), entry.getValue()));
		}
		
		return _wordFreqList;
	}
}

package ryan.pope.textcloud.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Contact 
{
	private String _name;
	private String _phoneNumber;
	private String _id;
	private int _numberType;
	private HashMap<String, Integer> _allMessages;
	
	public Contact(String id, String number, int type, String name)
	{
		_id = id;
		_phoneNumber = stripNumber(number);
		_numberType = type;
		_name = name;
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
		String[] splitSMS = parsedSMS.toUpperCase().split("\\s+");
		for(String s : splitSMS)
		{
			if(_allMessages.get(s) == null)
				_allMessages.put(s, 1);
			else
				_allMessages.put(s, _allMessages.get(s) + 1);
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
}

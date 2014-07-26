package ryan.pope.textcloud.objects;

import java.util.ArrayList;

public class Contact 
{
	private String _name;
	private String _phoneNumber;
	private String _id;
	private int _numberType;
	private ArrayList<String> _allMessages;
	
	public Contact(String id, String number, int type, String name)
	{
		_id = id;
		_phoneNumber = stripNumber(number);
		_numberType = type;
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
}

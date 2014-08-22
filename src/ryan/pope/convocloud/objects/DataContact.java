package ryan.pope.convocloud.objects;

public class DataContact extends DataBase
{

	private String _phoneNumber;
	public DataContact(String contactName, String number)
	{
		super();
		super.setName(contactName);
		_phoneNumber = stripNumber(number);
	}
	
	public String getPhoneNumber() 
	{
		return _phoneNumber;
	}
	
	private String stripNumber(String number) 
	{
		return number.replaceAll("[^0-9]", "");
	}
	
}

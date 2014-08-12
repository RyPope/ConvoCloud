package ryan.pope.convocloud.objects;

public class DataContact extends DataBase
{

	private String _phoneNumber;
	private String _contactName;
	public DataContact(String contactName, String number)
	{
		super();
		_contactName = contactName;
		_phoneNumber = stripNumber(number);
	}
	
	private String stripNumber(String number) 
	{
		return number.replaceAll("[^0-9]", "");
	}
	
	public String getPhoneNumber() 
	{
		return _phoneNumber;
	}
	
	
	public void setName(String contactName)
	{
		_contactName = contactName;
	}
	
	public String getName()
	{
		return _contactName;
	}
	
}

package A1;

public class Token {
	private String Type;
	private String Value;
	private String Location;
	private String Final;
	private int Code;
	
	Token()
	{
		Type = null;
		Value = null;
		Location = null;
		Final = null;
		Code = 0;
	}
	
	Token(String type, String value, String location)
	{
		Type = type;
		Value = value;
		Location = location;
		Final = null;
		Code = 0;
	}
	
	public String toString()
	{
		String s = "";
		
		s = Type + " " + Value + " " + Location + " ";
		
		return s;
				
	}
	
	public String toStringErr()
	{
		String s = "";
		
		s = Type + " [" + "invalid " + Final + " " + Value + "] " + Location + " ";
		
		return s;
				
	}
	
	public String getType()
	{
		return Type;
	}
	
	public String getValue()
	{
		return Value;
	}
	
	public String getLocation()
	{
		return Location;
	}
	
	public String getFinal()
	{
		return Final;
	}
	
	public int getCode()
	{
		return Code;
	}
	public void setType(String type)
	{
		Type = type;
	}
	
	public void setValue(String value)
	{
		Value = value;
	}
	
	public void setLocation(String location)
	{
		Location = location;
	}
	
	public void setFinal(String final1)
	{
		Final = final1;
	}
	
	public void setCode(int Code)
	{
		this.Code = Code;
	}
}

package AST_Tree;

public class Operation {
	
	private String Name;
	private String Value;
	
	public Operation()
	{
		Name = "";
		Value = "";
	}
	
	
	public String getName()
	{
		return Name;
	}
	
	public String getValue()
	{
		return Value;
	}
	
	public void setName(String name)
	{
		Name = name;
	}
	
	public void setValue(String value)
	{
		Value = value;
	}
	
	public boolean isOP(String value)
	{
		switch(value)
		{
		case"PLUS":
			return true;
		case"MINUS":
			return true;
		case"EQUALS":
			return true;
		case"COMPARISON":
			return true;
		case"DNE":
			return true;
		case"LT":
			return true;
		case"GT":
			return true;
		case"LTE":
			return true;
		case"GTE":
			return true;
		case"OR":
			return true;
		case"MULT":
			return true;
		case"SLASH":
			return true;
		case"AND":
			return true;
		case"NOT":
			return true;
		}
		
		return false;
	}
	
	public boolean ismakeOP(String value)
	{
		switch(value)
		{
		case"Asop":
			return true;
		case"Add":
			return true;
		case"Rop":
			return true;
		case"Mult":
			return true;
		
		}
		
		return false;
	}
	
}

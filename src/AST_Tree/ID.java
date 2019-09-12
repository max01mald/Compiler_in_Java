package AST_Tree;

public class ID 
{
	private String Name;
	
	ID()
	{
		Name = "";
	}
	
	public ID(String name)
	{
		Name = name;
	}
	
	public String getName()
	{
		return Name;
	}
	
	public void setName(String name)
	{
		Name = name;
	}
}

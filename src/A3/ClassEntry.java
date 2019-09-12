package A3;

public class ClassEntry extends SymTabEntry{

	public ClassEntry(String classname, SymTabl localtable) 
	{
		this.m_name = classname;
		this.m_kind = "Class";
		this.m_type = classname;
		this.m_subtable = localtable;
		
		
		
	}

}

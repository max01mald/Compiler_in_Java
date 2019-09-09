package A3;

import java.util.Vector;

public class FuncEntry extends SymTabEntry{

	public Vector<VarEntry> Var;
	
	public FuncEntry(String type, String name, Vector<VarEntry> vector, SymTabl localtable) 
	{
		if(type.equals("INTDC"))
		{
			type = "int";
		}
		else if(type.equals("FLOATDC"))
		{
			type = "float";
		}
		
		
		this.m_type = type;
		this.m_name = name;
		this.m_kind = "Function";
		
		Var = vector;
		
		for(int i=0;i<Var.size();i++)
		{
			localtable.addEntry(Var.get(i));
			//System.out.println(Var.get(i));
		}
		
		System.out.println("hhhhh");
		
		//System.out.println(localtable);
		
		this.m_subtable = localtable;
		
		
		
	}

}

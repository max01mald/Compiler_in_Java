package A3;

import java.util.Vector;

public class VarEntry extends SymTabEntry
{

	public VarEntry(String kind, String vartype, String varid, Vector<Integer> dimlist) 
	{
		this.m_name = varid;
		this.m_kind = kind;
		
		if(vartype.equals("INTDC"))
		{
			vartype = "int";
		}
		else if(vartype.equals("FLOATDC"))
		{
			vartype = "float";
		}
		
		
		this.m_type = vartype;
		this.m_dims = dimlist;
		
	}

}

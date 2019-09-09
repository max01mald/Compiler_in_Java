package A3;

import java.util.Vector;

public class SymTabEntry {
	public String          m_kind       = null;
	public String          m_type       = null;
	public String          m_name       = null;
	public int             m_size       = 0;
	public int             m_offset     = 0;
	public SymTabl          m_subtable   = null;
	public Vector<Integer> m_dims       = new Vector<Integer>();
	
	public SymTabEntry() {}
	
	public SymTabEntry(String p_kind, String p_type, String p_name, SymTabl p_subtable){
		m_kind = p_kind;
		m_type = p_type;
		m_name = p_name;
		m_subtable = p_subtable;
	}
	
	public String toString()
	{
		String s = "";
		
		String subname = "";
		
		if(this.m_subtable != null)
		{
			subname = this.m_subtable.m_name;
		}
		else
		{
			subname = "null";
		}
		
		String dim = "";
		
		for(int i= 0; i<m_dims.size();i++)
		{
			dim += "[" + m_dims.get(i) + "]";
		}
		
		s = String.format("%-15s" , "| " + m_name) + String.format("%-15s" , "| " + m_kind) + String.format("%-20s" , "| " + m_type + " " + dim) + String.format("%-15s" , "| " + m_size + "") 
		+ String.format("%-20s" , "| " + m_offset + "") +String.format("%-15s" , "| " + subname + "");

		return s;
	}
	
}


package A3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import AST_Tree.AST_Node;

public class SymTabl 
{
	
		public String                 m_name       = null;
		public ArrayList<SymTabEntry> m_symlist    = null; 
		public int                    m_size       = 0;
		public int                    m_tablelevel = 0;
		public SymTabl                m_uppertable = null;
		public int                    m_for = 0;
		public int                    m_forgl = 0;
		public int                    m_if = 0;
		public int                    m_ifgl = 0;
		public boolean                m_rel = false;
		public boolean                m_relgl = true;
		public boolean                valid = true;
		
		
		public SymTabl(int p_level, SymTabl p_uppertable){
			m_tablelevel = p_level;
			m_name = null;
			m_symlist = new ArrayList<SymTabEntry>();
			m_uppertable = p_uppertable;
		}
		
		
		public SymTabl(int p_level, String p_name, SymTabl p_uppertable){
			m_tablelevel = p_level;
			m_name = p_name;
			m_symlist = new ArrayList<SymTabEntry>();
			m_uppertable = p_uppertable;
		}
		
		public void addEntry(SymTabEntry p_entry){
			m_symlist.add(p_entry);	
		}
		
		public SymTabEntry lookupName(String p_tolookup) {
			SymTabEntry returnvalue = new SymTabEntry();
			boolean found = false;
			for( SymTabEntry rec : m_symlist) {
				if (rec.m_name.equals(p_tolookup)) {
					returnvalue = rec;
					found = true;
				}
			}
			if (!found) {
				if (m_uppertable != null) {
					returnvalue = this.m_uppertable.lookupName(p_tolookup);	
				}
			}
			return returnvalue;
		}
		
		public boolean lookupVar(String p_tolookup) {
			
			boolean found = false;
			for( SymTabEntry rec : m_symlist) {
				if (rec.m_name.equals(p_tolookup)) {
					return true;
				}
			}
			return false;
		}
		
		public String toString(){
			String parentname = new String();
			
			if(m_uppertable != null)
			{
				parentname = m_uppertable.m_name;
			}
			
			
			String stringtoreturn = new String();
			String prelinespacing = new String();
			for (int i = 0; i < this.m_tablelevel; i++)
				prelinespacing += "|    "; 
			stringtoreturn += "\n" + prelinespacing + "=================================================================================================\n";
			stringtoreturn += prelinespacing + String.format("%-25s" , "| table: " + m_name + "\tparent: " + parentname) + String.format("%-27s" , "\tscope offset: " + m_size) + "|\n";
			stringtoreturn += prelinespacing        + "=================================================================================================\n";
			
			stringtoreturn += prelinespacing + String.format("%-15s" , "| Name ") + String.format("%-15s" , "| Kind ") + String.format("%-20s" , "| Type ") + 
					String.format("%-15s" , "| Size         |") + String.format("%-15s" , "| Offset         |") + String.format("%-15s" , "| Link         |") + "\n";
			stringtoreturn += prelinespacing        + "=================================================================================================\n";
			
			
			for (int i = 0; i < m_symlist.size(); i++){
				stringtoreturn +=  prelinespacing + m_symlist.get(i).toString() + '\n'; 
			}
			stringtoreturn += prelinespacing        + "=================================================================================================";
			return stringtoreturn;
		}
		
		public void printTables(AST_Node Head, String name)
		{
			
			for(int i=0; i<Head.getChildren().size(); i++)
			{
				if(Head.getChildren().get(i).m_symtab != null)
				{
					String name2 = Head.getChildren().get(i).m_symtab.m_name;
				
				
					if(!name.equals(name2))
					{
						System.out.println(Head.getChildren().get(i).m_symtab);
						this.printTables(Head.getChildren().get(i), name2);
					}
					else
					{
						this.printTables(Head.getChildren().get(i), name2);
					}
					//System.out.println(Head.getChildren().get(i).m_symtab);
				
				}
			}
			
		}
		
		public void printTables(AST_Node Head, String name, String Hold) throws IOException, FileNotFoundException
		{
			
			for(int i=0; i<Head.getChildren().size(); i++)
			{
				
				if(Head.getChildren().get(i).m_symtab != null)
				{
					String name2 = Head.getChildren().get(i).m_symtab.m_name;
				
					File file = new File("SymTable.txt");
					
					
					BufferedWriter bw = new BufferedWriter(new FileWriter("SymTable.txt", true));
					
					if(!name.equals(name2))
					{
						bw.write(Head.getChildren().get(i).m_symtab.toString() + "\n");
						bw.flush();
						bw.close();
						
						
						this.printTables(Head.getChildren().get(i), name2, Hold);
						
					}
					else
					{
						this.printTables(Head.getChildren().get(i), name2, Hold);
						
					}
					
				
				}
			}
			
		}
		
		
		
		
		
		

}

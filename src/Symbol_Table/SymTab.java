package A3;

import java.util.ArrayList;

public class SymTab 
{
	private String ParentTableName;
	private SymTab Parent;
	private String TableName;
	private int Name = 0;
	private int Kind = 1;
	private int Type = 2;
	private int Link = 3;
	private int Offset = 4;
	private String CurrOff = "0";
	
	public ArrayList<SymTab> EntryLink;
	public ArrayList<String[]> Entry;
	
	public int m_size = 0;
	public int m_tablelevel = 0;
	
	SymTab()
	{
		ParentTableName = "";
		Parent = null;
		TableName = "";
		Entry = new ArrayList<String[]>();
		EntryLink = new ArrayList<SymTab>();
	}
	
	SymTab(int level, SymTab parent)
	{
		Parent = parent;
		ParentTableName = parent.getTableName();
		TableName = "";
		Entry = new ArrayList<String[]>();
		EntryLink = new ArrayList<SymTab>();
		m_tablelevel = level;
	}
	
	SymTab(int level, String name, SymTab parent)
	{
		Parent = parent;
		if(parent != null)
		{
			ParentTableName = parent.getTableName();
		}
		else
		{
			ParentTableName = "null";
		}
		TableName = name + " table";
		Entry = new ArrayList<String[]>();
		EntryLink = new ArrayList<SymTab>();
		m_tablelevel = level;
	}
	
	public ArrayList<String[]> getTable()
	{
		return Entry;
	}
	
	public SymTab getLink(String Name)
	{
		ArrayList<String[]> Table = getTable();
		
		String Entry = getEntryLink(Name);
		
		
		for(int j = 0; j<EntryLink.size(); j++)
		{
			if(EntryLink.get(j).getTableName().equals(Entry))
			{
				return EntryLink.get(j);
			}
		}
		
		return null;
	}
	
	public String getTableName()
	{
		return TableName;
	}
	
	public String getEntryName(String Name)
	{
		ArrayList<String[]> Table = getTable();
		
		for(int i=0; i<Table.size(); i++)
		{
			if(Table.get(i)[this.Name].equals(Name))
			{
				return Table.get(i)[this.Name];
			}
		}
		return null;
	}
	
	public String getEntryKind(String Kind)
	{
		ArrayList<String[]> Table = getTable();
		
		for(int i=0; i<Table.size(); i++)
		{
			if(Table.get(i)[this.Name].equals(Kind))
			{
				return Table.get(i)[this.Kind];
			}
		}
		return null;
	}
	
	public String getEntryType(String Type)
	{
		ArrayList<String[]> Table = getTable();
		
		for(int i=0; i<Table.size(); i++)
		{
			if(Table.get(i)[this.Name].equals(Type))
			{
				return Table.get(i)[this.Type];
			}
		}
		return null;
	}
	
	public String getEntryLink(String Link)
	{
		ArrayList<String[]> Table = getTable();
		
		for(int i=0; i<Table.size(); i++)
		{
			if(Table.get(i)[this.Name].equals(Link))
			{
				return Table.get(i)[this.Link];
			}
		}
		return null;
	}
	
	public String getEntryOffset(String Offset)
	{
		ArrayList<String[]> Table = getTable();
		
		for(int i=0; i<Table.size(); i++)
		{
			if(Table.get(i)[this.Name].equals(Offset))
			{
				return Table.get(i)[this.Offset];
			}
		}
		return null;
	}
	
	public String[] getEntry(String Name)
	{
		ArrayList<String[]> Table = getTable();
		
		for(int i=0; i<Table.size(); i++)
		{
			if(Table.get(i)[this.Name].equals(Name))
			{
				return Table.get(i);
			}
		}
		return null;
	}
	
	public SymTab getEntryST(String Name)
	{
		for(int i=0; i<this.EntryLink.size(); i++)
		{
			if(this.EntryLink.get(i).getTableName().equals(Name))
			{
				return this.EntryLink.get(i);
			}
		}
		return null;
	}
	
	public String getCurrentOffset()
	{
		return this.CurrOff;
	}
	
	public void setTableName(String Name)
	{
		TableName = Name;
	}
	
	public void setLinkEntry(String Name, SymTab Add)
	{
		ArrayList<String[]> Table = getTable();
		String Link = "";
		
		for(int i=0; i<Table.size(); i++)
		{
			if(Table.get(i)[this.Name].equals(Name))
			{
				Link = Table.get(i)[this.Link];
				
				for(int j=0; j<this.EntryLink.size(); j++)
				{
					if(this.EntryLink.get(j).getTableName().equals(Link))
					{
						System.out.println("Already there");
					}
					else
					{
						this.EntryLink.add(Add);
					}
				}
			}
			
		}
		
	}
	
	
	public void setEntryName(String Name, String N_name)
	{
		ArrayList<String[]> Table = getTable();
		
		for(int i=0; i<Table.size(); i++)
		{
			if(Table.get(i)[this.Name].equals(Name))
			{
				Table.get(i)[this.Name] = N_name;
			}
		}
		
	}
	
	public void setEntryKind(String Name, String Kind)
	{
		ArrayList<String[]> Table = getTable();
		
		for(int i=0; i<Table.size(); i++)
		{
			if(Table.get(i)[this.Name].equals(Name))
			{
				Table.get(i)[this.Kind] = Kind;
			}
		}
		
	}
	
	public void setEntryType(String Name, String Type)
	{
		ArrayList<String[]> Table = getTable();
		
		for(int i=0; i<Table.size(); i++)
		{
			if(Table.get(i)[this.Name].equals(Name))
			{
				Table.get(i)[this.Type] = Type;
			}
		}
		
	}
	
	public void setEntryLink(String Name, String Link)
	{
		ArrayList<String[]> Table = getTable();
		
		for(int i=0; i<Table.size(); i++)
		{
			if(Table.get(i)[this.Name].equals(Name + " table"))
			{
				Table.get(i)[this.Link] = Link;
			}
		}
	}
	
	public void setEntryOffset(String Name, String Offset)
	{
		ArrayList<String[]> Table = getTable();
		
		for(int i=0; i<Table.size(); i++)
		{
			if(Table.get(i)[this.Name].equals(Name))
			{
				Table.get(i)[this.Offset] = Offset;
			}
		}
		
	}
	
	public void setEntry(String Name, String Type, String Kind, String Link)
	{
		this.setEntryName(this.getTableName(), Name);
		this.setEntryType(this.getTableName(), Type);
		this.setEntryKind(this.getTableName(), Kind);
		this.setEntryLink(this.getTableName(), Link);
		
		String Offsets = this.getCurrentOffset();
		
		this.setEntryOffset(this.getTableName(), Offsets);
	}
	
	public void setEntryST(String Name, SymTab Entry)
	{
		for(int i=0; i<this.EntryLink.size(); i++)
		{
			if(this.EntryLink.get(i).getTableName().equals(Name))
			{
				this.EntryLink.add(i, Entry);
			}
		}
		
	}
	
	
	
	public void setCurrentOffset(String Off)
	{
		this.CurrOff = Off;
	}
	
	public void addToOff(String Offset)
	{
		int temp = Integer.parseInt(this.CurrOff);
		int temp2 = Integer.parseInt(Offset);
		
		temp = temp + temp2;
		
		this.CurrOff = temp+"";
	}
	
	public void AddEntry(String Name, String Kind, String Type, String Link)
	{
		String check = "";
		
		
		String [] entry = new String [5];
		entry[this.Name] = Name;
		entry[this.Kind] = Kind;
		entry[this.Type] = Type;
		if(Link.equals(""))
		{
			entry[this.Link] = "null";
		}
		else
		{
			entry[this.Link] = Link + " table";
		}
		entry[this.Offset] = this.getCurrentOffset();
		
		this.Entry.add(entry);
		int level = this.m_tablelevel +1;
		
		if(!(entry[3].equals("null")))
		{
			SymTab table = new SymTab(level, entry[3], this);
			
			this.EntryLink.add(table);
		}
		
	}
	
	public void AddNewEntry(String Name)
	{
		String check = "";
		
		
		String [] entry = new String [5];
		entry[this.Name] = "New";
		entry[this.Kind] = "New";
		entry[this.Type] = "New";
		entry[this.Link] = "New";
		entry[this.Offset] = "0";
		
		this.Entry.add(entry);
		int level = this.m_tablelevel +1;
		SymTab table = new SymTab(level, Name, this);
		
		entry[this.Link] = "Name";
		
		this.EntryLink.add(table);
		
	}
	
	public SymTab CreateNewTable(String Name)
	{
		this.AddNewEntry(Name);
		
		int level = this.m_tablelevel +1;
		SymTab temp = new SymTab(level, "New", this);
		
		return temp;
	}
	
	public String calculateOffset(String Name)
	{
		String Type = "";
		for(int i=0; i<this.Entry.size(); i++)
		{
			if(this.Entry.get(i)[this.Name].equals(Name))
			{
				Type = this.Entry.get(i)[this.Type];
			}
		}
		
		int Int = 4;
		int Float = 8;
		int Off = 1;
		int hold = 1;
		
		for(int i=0; i< Type.length(); i++)
		{
			if(Type.charAt(i) == 'i')
			{
				while(Type.charAt(i) != 'f' || i == Type.length()-1)
				{
					if((int)Type.charAt(i) > 48 || (int)Type.charAt(i) < 58)
					{
						String temp = Type.charAt(i) + "";
						
						hold *= Integer.parseInt(temp);
						hold = hold*Int;
						
						
					}
				}
				
			}
			else if(Type.charAt(i) == 'f')
			{
				while(Type.charAt(i) != 'i' || i == Type.length()-1)
				{
					if((int)Type.charAt(i) > 48 || (int)Type.charAt(i) < 58)
					{
						String temp = Type.charAt(i) + "";
						
						hold *= Integer.parseInt(temp);
						hold = hold*Float;
						
						
					}
				}
				
			}
		}
		System.out.println(hold);
		return hold+"";
	}
	
	public void Insert(String Name, String Kind, String Type, String Link, String Offset)
	{
		for(int i=0; i<this.Entry.size(); i++)
		{
			if(this.Entry.get(i)[this.Name].equals("New"))
			{
				this.Entry.get(i)[this.Name] = Name;
				this.Entry.get(i)[this.Kind] = Kind;
				this.Entry.get(i)[this.Type] = Type;
				this.Entry.get(i)[this.Link] = Link;
				
				String hold = this.calculateOffset(Name);
				
				this.Entry.get(i)[this.Offset] = this.CurrOff;
				
				this.addToOff(hold);
			}
		}
	}
	
	
	public boolean SearchTable(String Name)
	{
		String scheck = "";
		boolean check = false;
		
		scheck = this.getEntryName(Name);
		
		if(scheck == null)
		{
			return false;
		}
		else
		{
			return this.SearchInLinkTable(Name);
		}
	}
	
	public boolean SearchInLinkTable(String Name)
	{
		for(int i=0; i<this.EntryLink.size(); i++)
		{
			if(this.EntryLink.get(i).SearchTable(Name))
			{
				return true;
			}
			
		}
		return false;
	}
	
	public boolean Search(String Name)
	{
		boolean check = false;
		
		SymTab Table = this;
		
		check = Table.SearchTable(Name);
		
		if(check)
		{
			return check;
		}
		
		while(Table.Parent != null)
		{
			Table = Table.Parent;
			Table.SearchTable(Name);
			
			if(check)
			{
				return check;
			}
		}
		
		
		
		return check;
	}
	
	public void printEntries()
	{
		System.out.println("Table Name: " + this.TableName + " Parent Table: " + this.ParentTableName);
		System.out.println("Name\tKind\tType\tLink" );
		System.out.println();
		
		for(int i=0; i<this.Entry.size(); i++)
		{
			for(int j=0; j<this.Entry.get(i).length; j++)
			{
				System.out.print(this.Entry.get(i)[j] + "  ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void printSub()
	{
		
		for(int i=0; i<this.EntryLink.size(); i++)
		{
			System.out.println("Derived Tables");
			this.EntryLink.get(i).printEntries();
			this.EntryLink.get(i).printSub();
			System.out.println();
		}
	}
	
	public void printTables()
	{
		this.printEntries();
		this.printSub();
	}
	
	
	public static void main(String args[])
	{
		SymTab st = new SymTab(0, "Global" , null);
		
		st.AddNewEntry("Test");
		
		st.Insert("hello", "yolo", "jojo", "Global", "50");
		
		st.AddNewEntry("Mommy");
		
		st.Insert("ggg", "hh", "hh", "h", "h");
		
		//st.printEntries();
		//st.printSub();
		st.printTables();
		
	}
	
	
}

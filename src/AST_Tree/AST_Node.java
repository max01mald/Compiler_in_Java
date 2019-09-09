package AST_Tree;

import Visitor.Visitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import Element_Node.*;
import A3.SymTab;
import A3.SymTabEntry;
import A3.SymTabl;



public class AST_Node 
{
	private String Name;
	public String Type;
	private String Value;
	private AST_Node Parent;
	private ArrayList<AST_Node> Children;
	private AST_Node Left_Most_Child;
	private AST_Node L_Most_Sibling;
	private AST_Node R_Sibling;
	private int IntNum;
	private double FloatNum;
	private ID Id;
	private Operation Op;
	private boolean Visited;
	public SymTabl m_symtab;
	public SymTabEntry m_symtabentry;
	public String m_moonVarName;
	public static int m_nodelevel;
	public String m_subtreeString;
	public boolean m_Inl = false;
	public String Location = "";
	public String m_declty = "";
	
	public AST_Node()
	{
		Name = "";
		Type = "";
		Value = "";
		Parent = null;
		Children = new ArrayList<AST_Node>();
		Left_Most_Child = null;
		L_Most_Sibling = this;
		R_Sibling = null;
		IntNum = 0;
		FloatNum = 0;
		Id = null;
		Visited = false;
		m_symtab = null;
		m_symtabentry = null;
		m_moonVarName = "";
		m_nodelevel = 0;
		m_subtreeString = "";
		Location = "";
		
	}
	
	public AST_Node(String name)
	{
		this.Name = name;
		Type = "";
		Value = "";
		Parent = null;
		Children = new ArrayList<AST_Node>();
		Left_Most_Child = null;
		L_Most_Sibling = this;
		R_Sibling = null;
		IntNum = 0;
		FloatNum = 0;
		Id = null;
		Visited = false;
		m_symtab = null;
		m_symtabentry = null;
		m_moonVarName = "";
		m_nodelevel = 0;
		m_subtreeString = "";
		Location = "";
		m_declty = "";
	}
	
	public AST_Node(AST_Node Node)
	{
		this.Name = Node.Name;
		Type = Node.Type;
		Value = Node.Value;
		Parent = Node.Parent;
		Children = Node.Children;
		Left_Most_Child = Node.Left_Most_Child;
		L_Most_Sibling = Node.L_Most_Sibling;
		R_Sibling = Node.R_Sibling;
		IntNum = Node.IntNum;
		FloatNum = Node.FloatNum;
		Id = Node.Id;
		Visited = Node.Visited;
		m_symtab = Node.m_symtab;
		m_symtabentry = Node.m_symtabentry;
		m_moonVarName = Node.m_moonVarName;
		m_nodelevel = Node.m_nodelevel;
		m_subtreeString = Node.m_subtreeString;
		Location = Node.Location;
		m_declty = "";
	}
	
	public void deleteNode()
	{
		Name = "";
		Type = "";
		Value = "";
		Parent = null;
		Children = new ArrayList<AST_Node>();
		Left_Most_Child = null;
		L_Most_Sibling = this;
		R_Sibling = null;
		IntNum = 0;
		FloatNum = 0;
		Id = null;
		Visited = false;
		Location = "";
		m_declty = "";
	}
	
	public void setLocation(String loc)
	{
		Location = loc;
	}
	
	public String getLocation()
	{
		return Location;
	}
	
	public void setVisited(Boolean b)
	{
		Visited = b;
		
		if(this.getParent()!= null)
		{
			for(int i=0; i<this.getParent().getChildren().size(); i++)
			{
				if(this.getParent().getChildren().get(i) == this)
				{
					this.getParent().getChildren().get(i).Visited = b;
				}
			}
			
			
		}
		
		
	}
	
	public boolean getVisited()
	{
		return Visited;
	}
	
	public ArrayList<AST_Node> getChildren()
	{
		return this.Children;
	}
	
	public void accept(Visitor p_visitor) 
	{
			p_visitor.visit(this);
	}
	
	public void setNode(AST_Node Node)
	{
		
		Parent = Node.Parent;
		Children = Node.Children;
		Left_Most_Child = Node.Left_Most_Child;
		L_Most_Sibling = Node.L_Most_Sibling;
		R_Sibling = Node.R_Sibling;
	}
	
	public boolean hasName()
	{
		if(!this.getName().equals(""))
		{
			return true;
		}
		
		return false;
	}
	
	public void abandonChild()
	{
		AST_Node Temp = new AST_Node();
		AST_Node Temp2 = new AST_Node();
		
		Temp = this.getLC();
		
		if(Temp == null)
		{
			return;
		}
		
		while(Temp.getRSib() != null)
		{
			if(Temp.getRSib() != null)
			{
				Temp2 = Temp;
				
				Temp = Temp.getRSib();
				
				Temp2.setParent(null);
			}
			else
			{
				Temp.setParent(null);
				
			}
		}
		this.Children = new ArrayList<AST_Node>();
		this.Left_Most_Child = null;
	}
	
	public String getName()
	{
		return Name;
	}
	
	public String getType()
	{
		return Type;
	}
	
	public String getValue()
	{
		return Value;
	}
	
	public AST_Node getParent()
	{
		return Parent;
	}
	
	public AST_Node getLC()
	{
		return Left_Most_Child;
	}
	
	public AST_Node getLSib()
	{
		return L_Most_Sibling;
	}
	
	public AST_Node getRSib()
	{
		return R_Sibling;
	}
	
	public AST_Node getChild(String Value)
	{
		AST_Node Temp = new AST_Node();
		
		Temp = this.Left_Most_Child;
		
		//System.out.println("AST GET " + this.getName());
		
		if(Temp == null)
		{
			return null;
		}
		
		if(Temp.Name.equals(Value))
		{
			return Temp;
		}
		else
		{
			while(Temp != null)
			{
				Temp = Temp.R_Sibling;
				
				if(Temp != null)
				{
					if(Temp.Name.equals(Value))
					{
						return Temp;
					}
				}
				
			}
		}
		return null;
	}
	
	public AST_Node getChildType(String Value)
	{
		AST_Node Temp = new AST_Node();
		
		Temp = this.Left_Most_Child;
		
		//System.out.println("AST GET " + this.getName());
		
		if(Temp == null)
		{
			return null;
		}
		
		if(Temp.Type.equals(Value))
		{
			return Temp;
		}
		else
		{
			while(Temp != null)
			{
				Temp = Temp.R_Sibling;
				
				if(Temp != null)
				{
					if(Temp.Type.equals(Value))
					{
						return Temp;
					}
				}
				
			}
		}
		return null;
	}
	
	public int getNumSib(AST_Node Original)
	{
		AST_Node Temp = Original.L_Most_Sibling;
		
		int count = 0;
		
		while(Temp.R_Sibling != null)
		{
			if(Temp.R_Sibling != null)
			{
				if(Temp.getName().equals(Original.getName()))
				{
					count += 1;
				}
			}
			Temp = Temp.R_Sibling;
		}
		
		return count;
	}
	
	public AST_Node getSibling(String Value)
	{
		AST_Node Temp = new AST_Node();
		Temp = this.L_Most_Sibling;
		
		if(Temp.Name.equals(Value))
		{
			return Temp;
		}
		else
		{
			while(Temp != null)
			{
				Temp = Temp.R_Sibling;
				
				if(Temp != null)
				{
					if(Temp.Name.equals(Value))
					{
						return Temp;
					}
				}
			}
		}
		return null;
	}
	
	public AST_Node getSearchNumb(String Value)
	{
		AST_Node Temp = new AST_Node();
		Temp = this.L_Most_Sibling;
		
		if(Temp.Name.equals(Value)&&!Temp.getValue().equals("")&&Temp.getType().equals(""))
		{
			return Temp;
		}
		else
		{
			while(Temp != null)
			{
				Temp = Temp.R_Sibling;
				
				if(Temp != null)
				{
					if(Temp.Name.equals(Value)&&!Temp.getValue().equals("")&&Temp.getType().equals(""))
					{
						return Temp;
					}
				}
			}
		}
		return null;
	}
	
	public void listSib()
	{
		AST_Node Temp = new AST_Node();
		Temp = this.L_Most_Sibling;
		
		System.out.print(Temp.getName() + " ");
		while(Temp != null)
		{
			Temp = Temp.R_Sibling;
			
			if(Temp != null)
			{
				System.out.print(Temp.getName() + " ");
			}
		}
		System.out.println();
		
	}
	
	public int getIntNum()
	{
		if(this.Type.equals("IntNum"))
		{
			return IntNum;
		}
		
		return -1;
	}
	
	public double getFloatNum()
	{
		if(this.Type.equals("FloatNum"))
		{
			return FloatNum;
		}
		
		return -1;
	}
	
	public ID getID()
	{
		if(this.Type.equals("ID"))
		{
			return Id;
		}
		
		return null;
	}
	
	public String getIDName()
	{
		if(this.Type.equals("ID"))
		{
			return this.Id.getName();
		}
		return "";
	}
	
	public Operation getOperation()
	{
		return Op;
	}
	
	public String getOperationName()
	{
		if(this.Type.equals("OP"))
		{
			return this.Op.getName();
		}
		return "";
	}
	
	public String getOperationValue()
	{
		if(this.Type.equals("OP"))
		{
			return this.Op.getValue();
		}
		return "";
	}
	
	public void setName(String name)
	{
		Name = name;
	}
	
	public void setType(String type)
	{
		Type = type;
	}
	
	public void setValue(String value)
	{
		Value = value;
	}
	
	public void setParent(AST_Node parent)
	{
		Parent = parent;
	}
	
	public void setLC(AST_Node lmc)
	{
		Left_Most_Child = lmc;
	}
	
	public void setLSib(AST_Node lsib)
	{
		L_Most_Sibling = lsib;
	}
	
	public void setRSib(AST_Node rsib)
	{
		R_Sibling = rsib;
	}
	
	public void setChild(AST_Node Value)
	{
		AST_Node Temp = new AST_Node();
		Temp = this.Left_Most_Child;
		AST_Node LS = Temp.L_Most_Sibling;
		
		if(Temp == null)
		{
			Temp = Value;
			Temp.L_Most_Sibling = LS;
		}
		else
		{
			while(Temp != null)
			{
				Temp = Temp.R_Sibling;
				
				if(Temp == null)
				{
					Temp = Value;
					Temp.L_Most_Sibling = LS;
				}
			}
		}
		
	}
	
	public void printChildList(AST_Node Parent)
	{
		for(int i=0; i<Parent.Children.size();i++)
		{
			System.out.print(Parent.Children.get(i));
		}
		System.out.println();
	}
	
	public void changeParent(AST_Node Original, AST_Node Replacement)
	{
		System.out.println("CHANGE PARENT");
		
		AST_Node Temp = new AST_Node();
		
		System.out.println("INSIDE REPLACE PARENT");
		
		Replacement.printBottom();
		
		/*System.out.println(Original);
		System.out.println(Replacement);
		System.out.println(Original.Left_Most_Child);*/
		
		if(Replacement == Original.Left_Most_Child)
		{
			Original = Original.Left_Most_Child;
		}
		
		if(Original.Left_Most_Child != null)
		{
			Temp = Original.Left_Most_Child;
			AST_Node LS = Temp.L_Most_Sibling;
			Temp.setParent(Replacement);
			
			while(Temp.R_Sibling != null)
			{
				if(Temp.R_Sibling != null)
				{
					//System.out.println(Temp);
					Temp = Temp.R_Sibling;
					Temp.setParent(Replacement);
					Temp.L_Most_Sibling = LS;
				}
				
				
			}
		}
		
		
	}
	
	public void replaceNode(AST_Node Parent, AST_Node Original, AST_Node Replacement)
	{
		
		String r = "AST_Tree.AST_Node@2f2c9b19";
		
		if(Original.toString().equals(r)||Replacement.toString().equals(r))
		{
			System.out.println("replace the wrong guy");
			
			/*Original.printBottom();
			System.out.println(Original);
			Replacement.printBottom();
			System.out.println(Replacement);*/
			
		
		}
		
		
		System.out.println("TOP");
		//System.out.println("Parent " + Parent.getName() + " Original " + Original.getName() + " Replacement " + Replacement.getName());
		
		
		//Replacement.printBottom();
		//Replacement.printSiblings();
		
		if(Parent == Replacement)
		{
			System.out.println("Parent is replacement");
			AST_Node Temp3 = Parent.getParent();
			AST_Node Temp4 = Parent.getLSib();
			
			Replacement.Parent = Temp3;
			Replacement.L_Most_Sibling = Temp4;
			
			if(Temp4.getName().equals(Replacement.getName()))
			{
				Replacement.R_Sibling = Temp4.R_Sibling;
				return;
			}
			
			while(Temp4.R_Sibling != null)
			{
				if(Temp4.R_Sibling != null)
				{
					if(Temp4.R_Sibling.getName().equals(Replacement.getName()))
					{
						System.out.println("TOP WHILE");
						Temp3 = Temp4.R_Sibling;
						Temp4.R_Sibling = Replacement;
						Replacement.R_Sibling = Temp3.R_Sibling;
						Replacement.L_Most_Sibling = Temp3.L_Most_Sibling;
						
						
						return;
					}
					Temp4 = Temp4.R_Sibling;
				}
			}
			
			
		}
		
		if(Parent != null)
		{
			if(Parent.getName() == Replacement.getName())
			{
				System.out.println("DO NOTHING");
				//System.out.println(Replacement.getParent().getName() + " " + Replacement.getParent());
				System.out.println(Parent.getParent().getName() + " " + Parent.getParent());
				
				if(Parent.getParent() == Replacement.getParent())
				{
					AST_Node Temp2 = Parent.getParent();
					System.out.println("Abandon child");
					Temp2.Left_Most_Child = null;
					Temp2.Children = new ArrayList<AST_Node>();
					
					System.out.println("Post Abandon child");
					Replacement = Replacement.L_Most_Sibling;
					Temp2.Left_Most_Child = Replacement;
					Replacement.Parent = Temp2;
					
					
					while(Replacement.getRSib() != null)
					{
						if(Replacement.getRSib() != null)
						{
							Replacement = Replacement.R_Sibling;
							Replacement.Parent = Temp2;
							Replacement.L_Most_Sibling = Temp2.Left_Most_Child;
						}
					}
					
					
					Parent = Temp2;
					
					Parent.printSiblings();
					Replacement.printSiblings();
					
					//System.exit(0);
					
					return;
					
					
				}
				
				
			}
		
		
			for(int i=0; i<Parent.Children.size();i++)
			{
				if(Parent.Children.get(i) == Original)
				{	
					System.out.println("BEFORE " + Parent.Children.get(i));
					Parent.Children.remove(i);
					Parent.Children.add(i, Replacement);
					System.out.println("AFTER " + Parent.Children.get(i));
					
					
					
					if(Parent == Replacement || Parent == Original)
					{
						System.out.println("FOR LOOP");
						System.out.println("Parent " + Parent.getName() + " Original " + Original.getName() + " Replacement " + Replacement.getName());
					}
					
					break;
				}
			}
			
			
			
			AST_Node LC = new AST_Node();
			LC = Parent.getLC();
			AST_Node Temp = new AST_Node();
			
			if(LC == null)
			{
				if(Original.getName().equals("ID"))
				{
					Replacement.Parent = Parent.getParent();
					LC = Parent.getParent().getLC();
					
					Temp = LC.R_Sibling;
					LC.R_Sibling = Replacement;
					Temp = Temp.R_Sibling;
					Replacement.R_Sibling = Temp;
					Replacement.L_Most_Sibling = LC.L_Most_Sibling;
					return;
				}
			}
			
			
			if(LC == Original)
			{
				
				System.out.println("Inside if");
				Temp = LC;
				
				System.out.println("MAJOR TEST");
				
				Parent.printBottom();
				
				System.out.println("before " + LC);
				LC = Replacement;
				System.out.println("after " + LC);
				LC.Parent = Temp.Parent;
				LC.R_Sibling = Temp.R_Sibling;
				LC.L_Most_Sibling = Temp.L_Most_Sibling;
				Parent.Left_Most_Child = LC;
				
				changeParent(Original, LC);
				
				
				
				if(Parent == Replacement || Parent == Original)
				{
					System.out.println("Parent " + Parent.getName() + " Original " + Original.getName() + " Replacement " + Replacement.getName());
				}
				
				
				return;
			}
			else
			{
				
				while(LC.getRSib() != null)
				{
					if(LC.getRSib() == Original)
					{
						Parent.printBottom();
						System.out.println("HHHHH");
						System.out.println("Parent " + Parent.getName() + " Original " + Original.getName() + " Replacement " + Replacement.getName());
						
						//System.out.println("TEST FOR SAME PARENT " + Replacement.Parent.getName());
						Temp = new AST_Node(LC);
						
						AST_Node Temp2 = Parent.Left_Most_Child.L_Most_Sibling.R_Sibling.R_Sibling;
						
						Temp.printSiblings();
						
						LC.R_Sibling = Replacement;
						Replacement.Parent = Parent;
						Temp = Temp.R_Sibling;
						
						
						System.out.println(Temp.getName());
						Temp.printSiblings();
						System.out.println("Right child of replacement " + Temp.getRSib());
						
						if(Temp.getRSib() != null)
						{
							System.out.println("Right child of replacement " + Temp.getRSib().getName());
						}
						
						Replacement.R_Sibling = Temp.R_Sibling;
						Replacement.L_Most_Sibling = Temp.L_Most_Sibling;
						
						System.out.println("before ERROR");
						Parent.printBottom();
						
						//Parent.Left_Most_Child = Replacement.L_Most_Sibling;
						
						System.out.println("after error");
						Parent.printBottom();
						
						System.out.println("TEST CHILDREN");
						
						Replacement.printBottom();
						
						System.out.println("parent print");
						Parent.printBottom();
						
						System.out.println("HHHHHHHHHHHH " + Parent);
						System.out.println("HHHHHHHHHHHH " +Replacement.getParent());
						
						changeParent(Original, Replacement);
						
						System.out.println("HHHHHHHHHHHH " +Parent);
						System.out.println("HHHHHHHHHHHH " +Replacement.getParent());
						
						Replacement.printBottom();
						
						System.out.println("FINAL CHECK");
						System.out.println(Replacement.Parent.getChild("Asiz"));
						
						Parent.printBottom();
						
						
						if(Parent == Replacement || Parent == Original)
						{
							System.out.println("Parent " + Parent.getName() + " Original " + Original.getName() + " Replacement " + Replacement.getName());
						}
						
						return;
					}
					else
					{
						LC = LC.getRSib();
					}
				}
			}
		
		
			LC = Parent.Left_Most_Child;
			
			if(LC.getName().equals(Replacement.getName()))
			{
				System.out.println("FINAALLLLLY");
				Replacement.Parent = Parent;
				Replacement.L_Most_Sibling = Replacement;
				Replacement.R_Sibling = LC.R_Sibling;
				Parent.Left_Most_Child = Replacement;
				
				changeParent(LC, Replacement);
				
			}
			else
			{
				while(LC.R_Sibling != null)
				{
					System.out.println("FINAALLL2222LLY");
					
					if(LC.R_Sibling.Name.equals(Replacement.Name))
					{
						Temp = LC.R_Sibling;
						LC.R_Sibling = Replacement;
						System.out.println(Temp.R_Sibling);
						Replacement.R_Sibling = Temp.R_Sibling;
						Replacement.Parent = Temp.Parent;
						Replacement.L_Most_Sibling = Temp.L_Most_Sibling;
						
						Temp.printBottom();
						Temp.printSiblings();
						
						changeParent(Temp.R_Sibling, Replacement);
						
						return;
					}
					else
					{
						LC = LC.R_Sibling;
					}
					
				}
			}
		}
	}
	
	public void setIntNum(int Num)
	{
		this.Type = "INT";
		this.IntNum = Num;
	}
	
	public void setFloatNum(double Num)
	{
		this.Type = "FLOAT";
		this.FloatNum = Num;
	}
	
	public void setID(ID id)
	{
		this.Type = "ID";
		this.Id = id;
		this.Value = id.getName();
	}
	
	public void setIDName(String name)
	{
		if(this.Type.equals("ID"))
		{
			Id.setName(name);
		}
	}
	
	public void setOperation(Operation op)
	{
		this.Type = "OP";
		Op = op;
	}
	
	public void setOperationName(String name)
	{
		if(this.Type.equals("OP"))
		{
			Op.setName(name);
		}
	}
	
	public void setOperationValue(String value)
	{
		if(this.Type.equals("OP"))
		{
			Op.setValue(value);
		}
	}
	
	public AST_Node makeNode(int Num)
	{
		AST_Node temp = new AST_Node();
		temp.setIntNum(Num);
		temp.Name = "INT";
		temp.Value = Num+"";
		temp.L_Most_Sibling = temp;
		
		temp = new NumNode(temp);
		
		return temp;
	}
	
	public AST_Node makeNode(double Num)
	{
		AST_Node temp = new AST_Node();
		temp.setFloatNum(Num);
		temp.Name = "FLOAT";
		temp.L_Most_Sibling = temp;
		
		temp = new NumNode(temp);
		
		return temp;
	}

	public AST_Node makeNode(ID id)
	{
		AST_Node temp = new AST_Node();
		temp.setID(id);
		temp.Name = "ID";
		
		temp = new IdNode(temp);
		
		return temp;
	}
	
	public AST_Node makeNode(Operation op)
	{
		AST_Node temp = new AST_Node();
		temp.setOperation(op);
		temp.Name = op.getValue();
		temp.L_Most_Sibling = temp;
		
		return temp;
	}
	
	
	public AST_Node makeNode()
	{
		AST_Node temp = new AST_Node();
		temp.L_Most_Sibling = temp;
		
		temp.Type = "NULL";
		
		return temp;
	}
	
	public AST_Node makeSibling(AST_Node Sib)
	{
		AST_Node Temp = new AST_Node();
		Temp = this;
		
		while(Temp.R_Sibling != null)
		{
			if(Temp.R_Sibling != null)
			{
				Temp = Temp.R_Sibling;
			}
		}
		
		Sib = Sib.L_Most_Sibling;
		Temp.R_Sibling = Sib;
		
		
		Sib.L_Most_Sibling = Temp.L_Most_Sibling;
		Sib.Parent = Temp.Parent;
		
		System.out.println("LAAAASSSSTTTT CALLLL");
		//Sib.printSiblings();
		
				
		while(Sib.R_Sibling != null)
		{
			if(Sib.R_Sibling != null)
			{
				System.out.println("NAME " + Sib.Name);
				System.out.println("MAKE SIB " + Sib.R_Sibling.Name);
				Sib = Sib.R_Sibling;
				Sib.L_Most_Sibling = Temp.L_Most_Sibling;
				Sib.Parent = Temp.Parent;
				
				if(Temp.Parent != null)
				{
					Temp.Parent.Children.add(Sib);
				}
			}
		}
		return Sib;
		
	}
	
	public void addChildren(AST_Node Child)
	{
		Child = Child.L_Most_Sibling;
		
		this.Children = new ArrayList<AST_Node>();
		
		this.Children.add(Child);
		
		while(Child.R_Sibling != null)
		{
			Child = Child.R_Sibling;
			
			this.Children.add(Child);
		}
		
		
	}
	
	public void printChildren()
	{
		for(int i=0; i<this.Children.size(); i++)
		{
			System.out.print(this.Children.get(i).getName());
		}
		System.out.println();
	}
	
	public AST_Node adoptChildren(AST_Node Child)
	{
		System.out.println("ADOPT");
		
		if(this.Left_Most_Child != null)
		{
			return this.Left_Most_Child.makeSibling(Child);
			
		}
		else
		{
			System.out.println("ADOPTING without child");
			Child = Child.L_Most_Sibling;
			this.Left_Most_Child = Child;
			System.out.println("ADOPT");
			while(Child != null)
			{
				if(Child != null)
				{
					Child.Parent = this;
					this.Children.add(Child);
					Child = Child.R_Sibling;
				}
			}
		}
		return this;
	}
	
	
	
	public AST_Node makeProg(AST_Node Prog)
	{
		Prog.Type = "Program";
		
		AST_Node ClassList = isolateWithChild(Prog.getChildren().get(0));
		
		AST_Node FuncList = isolateWithChild(Prog.getChildren().get(1));
		AST_Node statBlock = isolateWithChild(isolateWithChild(Prog.getChildType("ProgBlock")));
		
		ClassList.makeSibling(FuncList);
		ClassList.makeSibling(statBlock);
		ClassList.makeSibling(makeNode());
		
		Prog.abandonChild();
		Prog.adoptChildren(ClassList);
		
		Prog.printBottom();
		
		
		return Prog;
	}
	
	public ArrayList<AST_Node> RetrieveClasses(AST_Node Class, String name, ArrayList<AST_Node> Vec)
	{
		AST_Node Fill = Class;
		AST_Node Store = null;
		
		Class.printBottom();
		
		while(Fill.getChild(name) != null)
		{
			if(Fill.getChild(name) != null)
			{
				System.out.println("FOUND ONE");
				Store = Fill.getChild(name);
				Fill.removeChild(Fill, name);
				Vec.add(Fill);
				Fill = Store;
				Store = null;
			}
			
			if(Fill.getChild(name) == null)
			{
				Vec.add(Fill);
			}
		}
		
		if(Vec.isEmpty())
		{
			Vec.add(Class);
		}
		
		return Vec;
	}
	
	public void removeChild(AST_Node Parent, String name)
	{
		AST_Node Child = Parent.getChild(name);
		AST_Node Temp = Parent.Left_Most_Child;
		AST_Node Temp2 = null;
		
		if(Child != null)
		{
			while(Temp.R_Sibling != null)
			{
				if(Temp.R_Sibling == Child)
				{
					Temp2 = Temp.R_Sibling.R_Sibling;
					Temp.R_Sibling = Temp2;
					break;
				}
				else
				{
					Temp = Temp.R_Sibling;
				}
			}
		}
		
		for(int i=0; i<Parent.Children.size(); i++)
		{
			if(Parent.Children.get(i) == Child)
			{
				Parent.Children.remove(i);
			}
		}
		
	}
	
	public void removeChildType(AST_Node Parent, String name)
	{
		AST_Node Child = Parent.getChildType(name);
		AST_Node Temp = Parent.Left_Most_Child;
		AST_Node Temp2 = null;
		
		if(Child != null)
		{
			while(Temp.R_Sibling != null)
			{
				if(Temp.R_Sibling == Child)
				{
					Temp2 = Temp.R_Sibling.R_Sibling;
					Temp.R_Sibling = Temp2;
					break;
				}
				else
				{
					Temp = Temp.R_Sibling;
				}
			}
		}
		
		for(int i=0; i<Parent.Children.size(); i++)
		{
			if(Parent.Children.get(i) == Child)
			{
				Parent.Children.remove(i);
			}
		}
		
	}
	
	public AST_Node makeClassList(AST_Node Class, ArrayList<AST_Node> ClassDec)
	{
		AST_Node ClassList = new AST_Node();
		ClassList.Type = "ClassList";
		ClassList.Name = "Cdcl";
		
		ClassDec = new ArrayList<AST_Node>();
		
		ClassDec = RetrieveClasses(Class, Class.Name, ClassDec);
		System.out.println(ClassDec.size());
		AST_Node Temp = ClassDec.get(0);
		AST_Node Temp2 = null;
		
		Temp = isolateWithChild(Temp);
		
		Temp.printBottom();
		
		for(int i=1; i<ClassDec.size(); i++)
		{
			Temp2 = ClassDec.get(i);
			
			if((!Temp2.getType().equals("ClassDcl")))
			{
				return null;
			}
			
			ClassDec.get(i).printBottom();
			
			Temp2 = isolateWithChild(Temp2);
			Temp = Temp.makeSibling(Temp2);
		}
		
		
		
		
		ClassList.adoptChildren(Temp);
		
		ClassList.printBottom();
		
		return ClassList;
	}
	
	public AST_Node makeFuncDefList(AST_Node FdefL, ArrayList<AST_Node> Fdef)
	{
		
		AST_Node Temp = isolateWithChild(Fdef.get(0));
		AST_Node Temp2 = null;
		
		FdefL.printBottom();
		
		if(Fdef.get(0).getType().equals("FuncDef"))
		{
			ArrayList<AST_Node> SB = new ArrayList<AST_Node>();
			
			AST_Node SSB = new AST_Node();
			
			SB = RetrieveStatOVar(FdefL, "Fdef", SB);
			
			System.out.println(SB.size());
			
			Temp = isolateWithChild(SB.get(0));
			
			Temp.printBottom();
			
			for(int i=1; i<SB.size(); i++)
			{
				if(SB.get(i).getChild("Fdef") == null)
				{
					System.out.println("Bottom " + i);
					SB.get(i).printBottom();
					Temp2 = isolateWithChild(SB.get(i));
					Temp.makeSibling(Temp2);
				}
			}
		}
		
		Temp.makeSibling(makeNode());
		
		FdefL.abandonChild();
		FdefL.adoptChildren(Temp);
		
		FdefL.Type = "FuncDefList";
		FdefL.printBottom();
		
		
		return FdefL;
	}
	
	public AST_Node makeClassDcl(AST_Node Cdcl, AST_Node ID, AST_Node InhL, AST_Node MemL)
	{
		Cdcl.Type = "ClassDcl";
		
		ID = isolate(ID);
		InhL = isolateWithChild(InhL);
		MemL = isolateWithChild(MemL);
		
		ID.makeSibling(InhL);
		ID.makeSibling(MemL);
		
		
		AST_Node Class = Cdcl.getChild("Cdcl");
		
		if(Class.getType().equals("ClassDcl"))
		{
			Class = isolateWithChild(Class);
			ID.makeSibling(Class);
		}
		
		ID.makeSibling(makeNode());
		
		Cdcl.abandonChild();
		Cdcl.adoptChildren(ID);
		
		Cdcl.printBottom();
		
		return Cdcl;
		
	}
	
	public AST_Node makeInherList(AST_Node InhL, ArrayList<AST_Node> ID)
	{
		InhL.Type = "InherList";
		
		AST_Node Temp = ID.get(0);
		AST_Node Temp2 = null;
		
		Temp = isolate(Temp);
		
		System.out.println("Insider makeInher " + ID.size());
		for(int i=1; i<ID.size(); i++)
		{
			if(ID.get(i).getName().equals("ID"))
			{
				Temp2 = ID.get(i);
				Temp2 = isolate(Temp2);
				Temp = Temp.makeSibling(Temp2);
			}
			else
			{
				return null;
			}
		}
		Temp.makeSibling(makeNode());
		InhL.abandonChild();
		InhL.adoptChildren(Temp);
		
		return InhL;
	}
	
	public void replaceChild(AST_Node Parent, AST_Node Replacement)
	{
		AST_Node Temp = null;
		AST_Node Temp2 = null;
		
		if(Parent.Left_Most_Child != null)
		{
			Temp = Parent.Left_Most_Child;
			
			if(Temp.getName().equals(Replacement.getName()))
			{
				Temp.abandonChild();
				Replacement.R_Sibling = Temp.R_Sibling;
				Temp = Replacement;
				Temp.setParent(Parent);
				return;
			}
			
		}
		
		while(Temp.R_Sibling != null)
		{
			if(Temp.R_Sibling != null)
			{
				if(Temp.R_Sibling.getName().equals(Replacement.getName()))
				{
					Temp2 = Temp;
					Temp = Temp.R_Sibling;
					Temp = Temp.R_Sibling;
					
					
					
					Temp2.R_Sibling = Replacement;
					
					
					
					Temp2 = Temp2.R_Sibling;
					Temp2.R_Sibling = Temp;
					
					System.out.println("hello");
					Temp2.setParent(Parent);
					Temp2.L_Most_Sibling = Temp.L_Most_Sibling;
					
					for(int i=0;i<Parent.Children.size();i++)
					{
						if(Parent.Children.get(i).getName().equals(Temp2.getName()))
						{
							Parent.Children.remove(i);
							Parent.Children.add(i, Temp2);
						}
					}
					return;
					
				}
				Temp = Temp.R_Sibling;
			}
		}
		
		
		
	}
	
	public AST_Node makeInherList(AST_Node InhL)
	{
		
		AST_Node Parent = null;
		
		String name = InhL.getName();
		
		System.out.println("INHERLIST");
		
		Parent = InhL.getParent();
		
		InhL = makeNode();
		InhL.Type = "InherList";
		InhL.Name = name;
		
		replaceChild(Parent, InhL);
		
		Parent.printBottom();
		InhL.printSiblings();
		
		
		
		return InhL;
		
	}
	
	
	public ArrayList<AST_Node> RetrieveMembers(AST_Node Member, String name, ArrayList<AST_Node> Vec)
	{
		Vec = new ArrayList<AST_Node>();
		AST_Node Fill = Member;
		AST_Node Store = null;
		AST_Node Right = null;
		
		Member.printBottom();
		
		while(Fill.getChild(name) != null)
		{
			if(Fill.getChild(name) != null)
			{
				System.out.println("FOUND ONE");
				Store = Fill.getChild(name);
				Fill.removeChild(Fill, name);
				Vec.add(Fill);
				
				Fill.printSiblings();
				
				if(Fill.getRSib().Type.equals("MembDecl"))
				{
					Fill = Fill;
				}
				else
				{
					Fill = Store;
				}
				
				Store = null;
				
			}
			
			
		}
		
		if(Fill != null)
		{
			Vec.add(Fill);
		}
		
		
		while(Fill.R_Sibling != null)
		{
			if(Fill.R_Sibling != null)
			{
				if(Fill.getRSib().Type.equals("MembDcl"))
				{
					System.out.println("Found one right");
					Fill.printBottom();
					
					Right = Fill.getRSib();
					Fill.R_Sibling = makeNode();
					
					Vec.add(Right);
				}
				if(Right == null)
				{
					Fill = Fill.getRSib();
				}
				else
				{
					Fill = Right;
				}
				
			}
		}
		
		
		
		
		if(Vec.isEmpty())
		{
			Vec.add(Member);
		}
		
		return Vec;
	}
	
	public AST_Node makeMemberList(AST_Node MemL, ArrayList<AST_Node> MemDcl)
	{
		
		System.out.println("Insider makeInher " + MemDcl.size());
		
		MemDcl = RetrieveMembers(MemDcl.get(0), "AD", MemDcl);
		
		AST_Node Temp = MemDcl.get(0);
		AST_Node Temp2 = null;
		
		Temp = isolateWithChild(Temp);
		
		System.out.println(MemDcl.size());
		
		for(int i=1; i<MemDcl.size(); i++)
		{
			Temp2 = MemDcl.get(i);
			
			System.out.println("bottom " + i);
			Temp2.printBottom();
			
			Temp2 = isolateWithChild(Temp2);
			Temp = Temp.makeSibling(Temp2);
			
			
		}
		MemL.Type = "MemberList";
		Temp.makeSibling(makeNode());
		MemL.abandonChild();
		MemL.adoptChildren(Temp);
		MemL.addChildren(Temp);
		MemL.printBottom();
		
		MemL.getLC().getRSib().printBottom();
		
		
		
		return MemL;
	
	}
	
	public AST_Node makeFuncDef(AST_Node Fdef, AST_Node Fhea, AST_Node Fbod)
	{
		
		AST_Node Hold = Fdef.getChildType("FuncDef");
		
		Fdef.printBottom();
		Fhea.printBottom();
		Fbod.printBottom();
		
		AST_Node Type = isolate(Fhea.getChildType("Type"));
		AST_Node Id = isolate(Fhea.getChild("ID"));
		AST_Node Param = isolateWithChild(Fhea.getChildType("ParamList"));
		AST_Node Hed = null;
		if(Fhea.getChild("Hed") != null)
		{
			if(!Fhea.getChild("Hed").getLC().getType().equals("NULL")) 
			{
				Fhea.getChild("Hed").printBottom();
				
				Hed = Fhea.getChild("Hed").getLC();
				
				Hed.Name = "ScopeSpec";
				Hed.Type = "ScopeSpec";
				
			}
		}
		Fbod.printBottom();
		
		Fhea = isolateWithChild(Fhea);
		Fbod = isolateWithChild(Fbod);
		
		if(Param != null)
		{
			
			Fbod = isolateWithChild(Fbod);
			
			Type.makeSibling(Id);
			
			if(Hed != null)
			{
				Type.makeSibling(Hed);
			}
			
			Type.makeSibling(Param);
			Type.makeSibling(Fbod);
			
			Fbod.printBottom();
			
			if(Hold != null)
			{
				Hold = isolateWithChild(Hold);
				Type.makeSibling(Hold);
				
			}
			
			
			Type.makeSibling(makeNode());
			
			Fdef.abandonChild();
			Fdef.adoptChildren(Type);
			
			Fdef.Type = "FuncDef";
			Fdef.printBottom();
			
			return Fdef;
			
		}
		
		
		Fhea.makeSibling(Fbod);
		
		if(Hold != null)
		{
			Hold = isolateWithChild(Hold);
			Fhea.makeSibling(Hold);
			
		}
		
		
		Fhea.makeSibling(makeNode());
		
		Fdef.abandonChild();
		Fdef.adoptChildren(Fhea);
		
		Fdef.Type = "FuncDef";
		Fdef.printBottom();
		
		//System.exit(0);
		
		return Fdef;
	}
	
	public AST_Node makeScopeSpec(AST_Node Hed, ArrayList<AST_Node> Vec)
	{
		AST_Node Temp = null;
		AST_Node Temp2 = new AST_Node();
		AST_Node Scope = new AST_Node();
		
		Scope.Type = "ScopeSpec";
		Scope.Name = "Hed";
		
		Hed.Type = "ScopeSpec";
		
		System.out.println("making scope Node");
		
		System.out.println("making ID SCOPE");
		
		Temp = isolate(Vec.get(0));
		
		System.out.println(Temp.getName() + " " + Temp.Id);
		
		if(Vec.size() == 2)
		{
			System.out.println("MAKING SCOPE + ID HED");
			
			Temp.Name = "ScopeID";
			Temp.Type = "ScopeID";
			
			Temp.makeSibling(makeNode());
			
			Scope.adoptChildren(Temp);
			
			Temp2 = isolate(Vec.get(1));
			
			Scope.makeSibling(Temp2);
			
			Scope.makeSibling(makeNode());
			
			Hed.abandonChild();
			Hed.adoptChildren(Scope);
			
			Hed.printBottom();
			
			return Hed;
			
		}
		else
		{
			System.out.println("MAKING ID HED");
			
			Hed.abandonChild();
			Temp.makeSibling(makeNode());
			
			Hed.adoptChildren(Temp);
			
			Hed.printBottom();
			
			
			//System.exit(0);
			
			return Hed;
		}
		
		
	}
	
	public AST_Node makeMemberDecl(AST_Node Memd, AST_Node Vard)
	{
		Vard.Type = "MembDcl";
		return Vard;
	}
	
	/*public AST_Node makeMemberDecl(AST_Node Memdcl, AST_Node Fdcl, AST_Node Type, AST_Node ID, AST_Node Fpara)
	{
		
			if(ID.Type.equals("ID"))
			{
				Fdcl.Type = "MembDcl";
				
				Type = Type.makeSibling(ID);
				Type = Type.makeSibling(Fpara);
				
				Fdcl.abandonChild();
				
				return Fdcl.adoptChildren(Type);
			}
			else 
			{
				return null;
			}
		
	}*/
	
	public AST_Node makeFparam(AST_Node Fpara)
	{
		String Name = Fpara.getName();
		Fpara = makeNode();
		Fpara.Type = "Fparam";
		return Fpara;
	}
	
	public AST_Node isolate(AST_Node node)
	{
		AST_Node New = new AST_Node();
		
		New.Name = node.Name;
		New.Type = node.Type;
		New.Value = node.Value;
		New.L_Most_Sibling = New;
		New.IntNum = node.IntNum;
		New.FloatNum = node.FloatNum;
		New.Id = node.Id;
		
		
		return New;
	}
	
	public AST_Node isolateWithChild(AST_Node node)
	{
		AST_Node New = new AST_Node();
		
		New.Name = node.Name;
		New.Type = node.Type;
		New.Value = node.Value;
		New.L_Most_Sibling = New;
		New.IntNum = node.IntNum;
		New.FloatNum = node.FloatNum;
		New.Id = node.Id;
		
		if(node.Left_Most_Child != null)
		{
			New.adoptChildren(node.Left_Most_Child);
		}
		return New;
	}
	
	public AST_Node makeFparam(AST_Node Fpara, AST_Node Type, AST_Node ID, AST_Node DimL)
	{
		System.out.println(Type.getName() + " " + Type.getType());
		
		Fpara.printBottom();
		Fpara.printSiblings();
		
		AST_Node Hold = Fpara.getChild("Fparat");
		
		Fpara.Type = "Fparam";
		Fpara.abandonChild();
		
		Type = isolate(Type);
		ID = isolate(ID);
		DimL = isolateWithChild(DimL);
		
		Type.makeSibling(ID);
		Type.makeSibling(DimL);
		
		if(Hold != null)
		{
			Hold = isolateWithChild(Hold);
			
			if(Hold.getLC() != null)
			{
				Type.makeSibling(Hold);
			}
		}
		
		Type.makeSibling(makeNode());
		
		Fpara.adoptChildren(Type);
		
		Fpara.printBottom();
		
		return Fpara;
		
		
	
	}
	
	public AST_Node makeTID(AST_Node Tid)
	{
		Tid.printBottom();
		
		Tid.getChild("Type").printBottom();
		
		
		
		
		AST_Node Id = null;
		AST_Node Type = null;
		
		
		if(Tid.getChild("TidD").getLC() != null)
		{
			Tid.getChild("TidD").getLC().printBottom();
			
			Id = isolate(Tid.getChild("TidD").getLC());
			Type = isolate(Tid.getChild("Type"));
			

			ID temp = new ID(Id.Value.trim()); 
			
			Id.setID(temp);
			
			Id.printBottom();
			
			Type.Type = "Type";
			Type.Name = "Type";
			
			Id.Name = "ID";
			
			Id.printBottom();
			
			Type.makeSibling(Id);
			Type.makeSibling(makeNode());
			
			Tid.abandonChild();
			Tid.adoptChildren(Type);
			
			Tid.printBottom();
			
			
			return Tid;
			
			
		}
		else
		{
			Id = isolate(Tid.getChildType("Type"));
			
			
			ID temp = new ID(Id.Value.trim()); 
			
			Id.setID(temp);
			
			Id.printBottom();
			
			Id.Type = "ID";
			Id.Name = "ID";
			
			Id.printBottom();
			
			Id.makeSibling(makeNode());
			
			Tid.abandonChild();
			Tid.adoptChildren(Id);
			
			Tid.printBottom();
			
			
			return Tid;
		}
		
		
		
		
	}
	
	public AST_Node makeVarDecl(AST_Node Vardcl, AST_Node Type, AST_Node ID, AST_Node DimL)
	{
		Vardcl.printSiblings();
		Vardcl.printBottom();
		
		AST_Node Hold = DimL.getSibling("AD");
		
		
		if(ID.Type.equals("ID"))
		{
			System.out.println(Type.getName() + " " + Type.getType());
			
			System.out.println("ID proper");
			
			System.out.println(DimL.getType());
			
			if((!DimL.Type.equals("DimList")))
			{
				return null;
			}
			
			Vardcl.Type = "VarDecl";
			Vardcl.abandonChild();
			
			Type = isolate(Type);
			ID = isolate(ID);
			DimL = isolateWithChild(DimL);
			
			Type.makeSibling(ID);
			Type.makeSibling(DimL);
			
			if(Hold != null)
			{
				Hold = isolateWithChild(Hold);
				
				if(Hold.getLC() != null)
				{
					Type.makeSibling(Hold);
				}
			}
			
			Type.makeSibling(makeNode());
			
			Vardcl.adoptChildren(Type);
			Vardcl.addChildren(Type);
			
			
			Vardcl.printBottom();
			
			
			return Vardcl;
		}
		else if(Vardcl.getChild("TidD") != null)
		{
			Type = isolate(Type);
			ID = isolate(ID);
			
			DimL = isolateWithChild(DimL);
			
			
			Type = makeSibling(ID);
			Type = makeSibling(DimL);
			Type = makeSibling(makeNode());
			
			Vardcl.abandonChild();
			Vardcl.adoptChildren(Type);
			
			Vardcl.Type = "VarDecl";
			
			Vardcl.printBottom();
			
			//System.exit(0);
			
			return null;
		}
		else 
		{
			return null;
		}
		
		
		
	}
	
	public AST_Node makeVarEl(AST_Node VarEl, AST_Node ID)
	{
		ID.Type = "VarEl";
		VarEl.printBottom();
		
		ID.printBottom();
		
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return ID;
	}
	
	public AST_Node makeVarDecl(AST_Node VarDcl, AST_Node Type, AST_Node sub)
	{
		if(!(VarDcl.getType().equals("")))
		{
			System.out.println("Ipary");
			//System.exit(0);
		}
		VarDcl.Type = "VarDecl";
		VarDcl.abandonChild();
		Type = Type.makeSibling(sub);
		
		return (VarDcl.adoptChildren(Type));
	}
	
	public AST_Node makeVarDecl(AST_Node VarDcl, AST_Node memb)
	{
		VarDcl.Type = "VarDcl";
		
		if(!(VarDcl.getType().equals("")))
		{
			System.out.println("Ipary");
			//System.exit(0);
		}
		
		return (VarDcl);
	}
	
	public AST_Node makeT(AST_Node type)
	{
		
		if(type.Name.equals("ID"))
		{
			System.out.println(type.Value);
			
		}
		
		else if(type.Name.equals("INTDC"))
		{
			type.Value = "INTDC";
			
		}
		else if(type.Name.equals("FLOATDC"))
		{
			type.Value = "FLOATDC";
		}
		
		type.Type = "Type";
		type.Name = this.Name;
		
		type = isolateWithChild(type);
		
		return type;
	}
	
	public AST_Node makeFDecl(AST_Node VarDcl, AST_Node Type, AST_Node ID, AST_Node Plist)
	{
		VarDcl.Type = "FuncDcl";
		
		VarDcl.printSiblings();
		VarDcl.printBottom();
		System.out.println("helMAKEFUNCTIONDEClo");
		
		AST_Node Hold = null;
		
		if((!Plist.getType().equals("ParamList")))
		{
			return null;
		}
		else if(Plist.getParent().getType().equals("MembDcl"))
		{
			return null;
		}
		
		Hold = VarDcl.getChild("BD");
		
		if(Hold.getChildType("MembDcl") != null)
		{
			Hold = isolateWithChild(Hold.getChildType("MembDcl"));
			System.out.println(Hold.Type);
			
		}
		
		if(!(Hold.Type.equals("MembDcl")))
		{
			Hold = null;
		}
		
		Type = isolate(Type);
		ID = isolate(ID);
		Plist = isolateWithChild(Plist);
		
		Type.makeSibling(ID);
		Type.makeSibling(Plist);
		
		if(Hold != null)
		{
			Type.makeSibling(Hold);
		}
		
		Type.makeSibling(makeNode());
		
		VarDcl.abandonChild();
		
		VarDcl.adoptChildren(Type);
		
		VarDcl.printBottom();
		VarDcl.printSiblings();
		
		/*try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		return VarDcl;
		
	}
	
	public AST_Node makeParamList(AST_Node FpL)
	{
		FpL.Type = "ParamList";
		FpL.abandonChild();
		FpL.adoptChildren(makeNode());
		
		return FpL;
	}
	
	
	public AST_Node makeParamList(AST_Node FpL, ArrayList<AST_Node> Fpara)
	{
		
		System.out.println(Fpara.size());

		FpL.printBottom();
		
		Fpara = RetrieveMembers(FpL, "Fparat", Fpara);
		
		AST_Node Temp = isolateWithChild(Fpara.get(0));
		AST_Node Temp2 = new AST_Node();
		
		System.out.println(Fpara.size());
		
		for(int i=1; i<Fpara.size(); i++)
		{
			
			Temp2 = isolateWithChild(Fpara.get(i));
				
			Temp = Temp.makeSibling(Temp2);
			
		}
		
		Temp.makeSibling(makeNode());
		
		FpL.abandonChild();
		
		FpL.adoptChildren(Temp);
		
		FpL.Type = "ParamList";
		
		FpL.printBottom();
		FpL.printSiblings();
		
		
		return FpL;
	}
	
	public AST_Node makeDimList(AST_Node Asiz)
	{
		Asiz = makeNode();
		Asiz.Type = "DimList";
		Asiz.Name = "Asiz";
		return Asiz;
	}
	
	public AST_Node makeDimList(AST_Node DimL, ArrayList<AST_Node> Num)
	{
		
		AST_Node DimList = new AST_Node();
		DimList.Type = "DimList";
		DimList.Name = "Asiz";
		
		DimL.printSiblings();
		
		System.out.println(Num.size());
		
		AST_Node Temp = Num.get(0);
		AST_Node Temp2 = null;
		
		Temp = isolateWithChild(Temp);
		
		Temp.printBottom();
		
		for(int i=1; i<Num.size(); i++)
		{
			System.out.println(Num.get(i).getName() + " " + Num.get(i).getValue());
			
		}
		
		for(int i=1; i<Num.size(); i++)
		{
			Temp2 = Num.get(i);
			
			System.out.println(Temp2.getName());
			
			if((!Temp2.getType().equals("INT")))
			{
				
			}
			
			Num.get(i).printBottom();
			
			Temp2 = isolateWithChild(Temp2);
			Temp = Temp.makeSibling(Temp2);
		}
		
		Temp.makeSibling(makeNode());
		
		DimList.adoptChildren(Temp);
		
		DimList.printBottom();
		
		
		return DimList;
	}
	
	public AST_Node makeStatBlock(AST_Node Temp)
	{
		String name = Temp.getName();
		Temp = makeNode();
		
		if((!this.getName().equals("Fbod")))
		{
			Temp.Name = name;
		}
		else
		{
			Temp.Name = "Fbod";
		}
		
		Temp.Type = "StatBlock";
		
		Temp.abandonChild();
		Temp.adoptChildren(makeNode());
		
		if(this.getParent().getName().equals("Prog"))
		{
			Temp.Type = "ProgBlock";
		}
		
		Temp.printBottom();
		
		
		
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return Temp;
	}
	
	public ArrayList<AST_Node> RetrieveStatOVar(AST_Node Head, String name, ArrayList<AST_Node> Vec)
	{
		AST_Node Fill = Head;
		AST_Node Store = null;
		AST_Node Right = null;
		Vec = new ArrayList<AST_Node>();
		
		Head.printBottom();
		
		while(Fill.getChild(name) != null)
		{
			if(Fill.getChild(name) != null)
			{
				Fill.printSiblings();
				System.out.println("FOUND ONE");
				Store = Fill.getChild(name);
				
				Fill.removeChild(Fill, name);
				Vec.add(Fill);
				
				
				if(Store.getRSib().Name.equals(name))
				{
					System.out.println("FOUND ONE RIGHT");
					Right = Store.getRSib();
					Store.R_Sibling = makeNode();
				}
				
				
				if(Right == null)
				{
					Fill = Store;
					Store = null;
				}
				else
				{
					Fill = Right;
					Store = null;
				}
				
			}
			
			
		}
		if(Fill.Type.equals("StatORVar") || Fill.Type.equals("FuncDef"))
		{
			if(Vec.isEmpty())
			{
				Vec.add(Fill);
			}
			else if(Fill != Vec.get(Vec.size()-1))
			{
				Vec.add(Fill);
			}
			
		}
		
		if(Vec.isEmpty())
		{
			Vec.add(Head);
		}
		
		return Vec;
	}
	
	
	public ArrayList<AST_Node> RetrieveStatOVarType(AST_Node Head, String name, ArrayList<AST_Node> Vec)
	{
		AST_Node Fill = Head;
		AST_Node Store = null;
		AST_Node Right = null;
		Vec = new ArrayList<AST_Node>();
		
		Head.printBottom();
		
		while(Fill.getChildType(name) != null)
		{
			if(Fill.getChildType(name) != null)
			{
				Fill.printSiblings();
				System.out.println("FOUND ONE");
				Store = Fill.getChildType(name);
				
				Fill.removeChildType(Fill, name);
				Vec.add(Fill);
				
				
				if(Store.getRSib().Type.equals(name))
				{
					System.out.println("FOUND ONE RIGHT");
					Right = Store.getRSib();
					
					Right.printBottom();
					
					Store.R_Sibling = makeNode();
				}
				
				
				if(Right == null)
				{
					Fill = Store;
					Store = null;
				}
				else
				{
					
					Fill = Right;
					Store = null;
					Right = null;
				}
				
			}
			
			
		}
		if(Fill.Type.equals("StatORVar") || Fill.Type.equals("FuncDef"))
		{
			if(Vec.isEmpty())
			{
				Vec.add(Fill);
			}
			else if(Fill != Vec.get(Vec.size()-1))
			{
				Vec.add(Fill);
			}
			
		}
		
		if(Vec.isEmpty())
		{
			Vec.add(Head);
		}
		
		return Vec;
	}
	
	public AST_Node makeStatBlock(AST_Node StatB, ArrayList<AST_Node> StatO)
	{
		System.out.println("making StatB");
		
		AST_Node StatBlock = new AST_Node();
		StatBlock.Type = "StatBlock";
		
		if(StatB.getName().equals("Sblok"))
		{
			StatBlock.Name = "Sblok";
		}
		else if(StatB.getName().equals("Fbod"))
		{
			StatBlock.Name = "Fbod";
		}
		else if(StatB.getName().equals("SblokEL"))
		{
			StatBlock.Name = "SblokEL";
		}
		else
		{
			StatBlock.Name = "Stmt";
		}
		
		AST_Node Temp = null;
		AST_Node Temp2 = null;
		
		AST_Node PB = null;
		
		StatO = RetrieveStatOVar(StatB, "StatORVar", StatO);
		
		
		
		System.out.println(StatO.size());
		
		if(StatO.size()>1)
		{
			Temp = isolateWithChild(StatO.get(1));
			Temp.printBottom();
			
			System.out.println("1");
			
		}
		else
		{
			Temp = isolateWithChild(StatO.get(0));
			
			if((!Temp.Type.equals("StatORVar")))
			{
				Temp.printBottom();
				
				if(!Temp.Name.equals("Fbod"))
				{
					StatO = RetrieveStatOVarType(StatB, "StatORVar", StatO);
				}
				else
				{
					
					
					StatO = RetrieveStatOVarType(StatB, "StatORVar", StatO);
					
					System.out.println(StatO.size());
					
					//System.exit(0);
				}
				
				
				if(StatO.size()>1)
				{
					Temp = isolateWithChild(StatO.get(1));
					Temp.printBottom();
					System.out.println("Bottom");
				}
				
				Temp.printBottom();
				
				System.out.println(StatO.size());
				
				Temp.printBottom();
				
				System.out.println("2");
				Temp.printBottom();
				
				System.out.println("Stop");
//				try {
//					System.in.read();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				
			}
			
			System.out.println("3");
			
		}
		
		for(int i=2; i<StatO.size(); i++)
		{
			System.out.println("bottom " + i);
			StatO.get(i).printBottom();
			Temp2 = isolateWithChild(StatO.get(i));
			
			Temp.makeSibling(Temp2);
			
		}
		
		
		
		Temp.printBottom();
		
		Temp.makeSibling(makeNode());
		
		StatBlock.abandonChild();
		StatBlock.adoptChildren(Temp);
		
		
		
		if(StatB.Name.equals("Fbod"))
		{
			StatBlock.Name = "Fbod";
		}
		
		if(this.getParent().getName().equals("Prog"))
		{
			StatBlock.Type = "ProgBlock";
		}
		
		StatBlock.printBottom();
		
		System.out.println(StatB.getName());
		
		
		
		System.out.println("Stoooop");
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		//System.exit(0);
		
		return StatBlock;
		
	}
	
	public AST_Node makeFhea(AST_Node Fhea, AST_Node Type, AST_Node Scope, AST_Node FparaL)
	{
		AST_Node Temp = null;
		AST_Node Temp2 = new AST_Node();
		
		Fhea.Type = "Fhea";
		
		Type = isolate(Type);
		
		Scope.printBottom();
		
		Temp2 = Scope.getChild("Hed");
		
		if(Temp2 != null)
		{
			if(Temp2.getChild("ScopeID") != null)
			{
				System.out.println("MAKING SCOPE + ID");
				
				Temp2 = isolate(Temp2.getChild("ScopeID"));
				
				Temp = isolate(Scope.getChild("ID"));
				
				Temp2.makeSibling(Temp);
				
				Scope = Temp2;
			}
		}
		else
		{
			if(Scope.Type.equals("ID"))
			{
				Scope = isolate(Scope);
			}
		}
		
		FparaL = isolateWithChild(FparaL);
		
		Type.makeSibling(Scope);
		Type.makeSibling(FparaL);
		Type.makeSibling(makeNode());
		
		Fhea.abandonChild();
		Fhea.adoptChildren(Type);
		
		
		Fhea.printBottom();
		
		return Fhea;
		
		
	}
	
	public AST_Node makeStatORVar(AST_Node StatOVar, AST_Node Var ,AST_Node Asop, AST_Node Expr)
	{
		StatOVar.printBottom();
		Var.printBottom();
		Asop.printBottom();
		Expr.printBottom();
		
		AST_Node Hold = null;
		
		if(StatOVar.getChildType("StatORVar") != null)
		{
			Hold = isolateWithChild(StatOVar.getChildType("StatORVar"));
		}
		
		if(StatOVar.getChild("Type") != null)
		{
			AST_Node V = isolate(StatOVar.getChild("Type"));
			
			AST_Node Inl = null;
			
			if(Var.getChild("IDD") != null)
			{
				Inl = isolateWithChild(Var.getChild("IDD").getChildType("InL"));
			}
			
			if(Var.getChild("Asiz") == null)
			{
				
				V.Name = "ID";
				V.Type = "ID";
				
				Inl.printSiblings();
				
				V.makeSibling(Inl);
				V.makeSibling(makeNode());
				
				Var = isolate(Var);
				
				Var.Type = "Vari";
				
				Var.abandonChild();
				Var.adoptChildren(V);
				
				
				Expr.printBottom();
				
				if(Expr.getChild("IDD") != null)
				{
					if(Expr.getChild("IDD").getChildType("Aparam") == null)
					{
						AST_Node Eid = isolate(Expr.getChild("IDD").getChild("ID"));
						
						Expr.printBottom();
						//System.exit(0);
						Expr = Eid;
						
						Expr.Name = "Expr";
						Expr.Type = "Expr";
					}
					
				}
				
				Expr = isolateWithChild(Expr);
				
				Var.makeSibling(Expr);
				
				if(Hold != null)
				{
					Var.printSiblings();
					
					Hold.printSiblings();
					
					Var.makeSibling(Hold);
					
					
					
				}
				
				Var.makeSibling(makeNode());
				
				Var.printBottom();
				
				StatOVar = isolate(Asop);
				
				StatOVar.abandonChild();
				StatOVar.adoptChildren(Var);
				
				StatOVar.Name = "StatOVar";
				StatOVar.Type = "StatORVar";
				
				StatOVar.printBottom();
				
				return StatOVar;
			}
		}
		
		if(Var.getChildType("DimList") != null)
		{
			StatOVar.printBottom();
			Var.printBottom();
			Asop.printBottom();
			Expr.printBottom();
			
			AST_Node ID = isolate(Var.getChild("ID"));
			AST_Node Dim = isolateWithChild(Var.getChild("Asiz"));
			AST_Node Type = isolate(StatOVar.getChild("Type"));
			
			Type.makeSibling(ID);
			Type.makeSibling(Dim);
			
			if(Hold != null)
			{
				Type.makeSibling(Hold);
			}
			
			Type.makeSibling(makeNode());
			
			
			StatOVar.abandonChild();
			StatOVar.adoptChildren(Type);
			StatOVar.Type = "StatORVar";
			
			StatOVar.printBottom();
			
			
			return StatOVar;
		}
		
		if(Var.getChild("Astat") != null)
		{
			AST_Node ID = isolate(StatOVar.getChild("Type"));
			Asop = isolate(Asop);
			Expr = isolateWithChild(Expr);
			
			StatOVar = Asop;
			
			ID.makeSibling(Expr);
			if(Hold != null)
			{
				ID.makeSibling(Hold);
			}
			ID.makeSibling(makeNode());
			
			StatOVar.abandonChild();
			StatOVar.adoptChildren(ID);
			
			StatOVar.Type = "StatORVar";
			StatOVar.Name = "StatOVar";
			
			StatOVar.printBottom();
			
			return StatOVar;
			
		}
		else
		{
			Asop = isolate(StatOVar.searchFunction(StatOVar, "Type"));
			Expr = isolateWithChild(Var.searchFunction(Var, "Asiz"));
			Var = isolate(Var.searchFunction(Var, "ID"));
			
			Asop.makeSibling(Var);
			Asop.makeSibling(Expr);
			Asop.makeSibling(Hold);
			Asop.makeSibling(makeNode());
			
			StatOVar.abandonChild();
			StatOVar.adoptChildren(Asop);
			
			StatOVar.Type = "StatORVar";
			StatOVar.Name = "StatOVar";
			
			StatOVar.printBottom();
			
			
		}
		
		
		
		
		return StatOVar;
	}
	
	public AST_Node makeStatORVar(AST_Node StatOVar, AST_Node St)
	{
		AST_Node Temp = null;
		AST_Node Temp2 = null;
		
		if(StatOVar.getName().equals("Sblok"))
		{
			St.Name = "Sblok";
			
			System.out.println("Changed name " + St.Name);
		}
		else if(StatOVar.getName().equals("Stmt"))
		{
			St.Name = "Stmt";
		}
		else if(StatOVar.getName().equals("StatOVar"))
		{
			St.Name = "StatOVar";
		}
		StatOVar.printSiblings();
		StatOVar.printBottom();
		
		System.out.println(" THe name " + StatOVar.Name + " " + St.Name);
		System.out.println(" THe type " + StatOVar.Type + " " + St.Type);
		
		if(StatOVar.getChildType("StatORVar") != null)
		{
			Temp = isolateWithChild(StatOVar.getChildType("StatORVar"));
			Temp.printBottom();
			
		}
		
		AST_Node Type = null;
		AST_Node ID = null;
		AST_Node Asiz = null;
		AST_Node Var = null;
		
		if(St.Type.equals("Stat"))
		{
			Temp = St;
			while(Temp.R_Sibling != null)
			{
				if(Temp.R_Sibling != null)
				{
					if(Temp.R_Sibling.Type.equals("StatORVar"));
					{
						Temp2 = isolateWithChild(Temp.R_Sibling);
						
						if(Temp2.Left_Most_Child == null)
						{
							Temp2 = null;
						}
						
						break;
					}
					
				}
				
				Temp = Temp.R_Sibling;
			}
			
			
			if(Temp2 != null)
			{
				Temp2.Name = "StatOVar";
				Temp2.Type = "StatORVar";
				
				St.printSiblings();
				Temp.printSiblings();
				
				St = isolateWithChild(St);
				
				St.makeSibling(Temp2);
				St.makeSibling(makeNode());
				
				St.Name = "StatOVar";
				St.Type = "StatORVar";
				
				StatOVar.Name = "StatOVar";
				StatOVar.Type = "StatORVar";
				
				StatOVar.abandonChild();
				StatOVar.adoptChildren(St);
				
				StatOVar.printSiblings();
				StatOVar.printBottom();
				
				System.out.println("heehrrer");
				
				
				return StatOVar;
			}
			else
			{
				St = isolateWithChild(St);
				
				St.Type = "StatORVar";
				
				return St;
			}
		}
		else if(Temp != null)
		{
			St.Type = "StatORVar";
			Temp.Type = "StatORVar";
			
			St.printBottom();
			
			if(St.getChild("Var") != null)
			{
				Type = isolateWithChild(this.searchFunction(St, "Type"));
				
				Var = isolateWithChild(St.getChild("Var"));
				
				ID = isolateWithChild(Var.getChild("ID"));
				
				Asiz = isolateWithChild(Var.getChild("Asiz"));
				
				ID id = new ID(ID.Value);
				
				ID.setID(id);
				
				ID.Type = "ID";
				
				Type.printBottom();
				Var.printBottom();
				ID.printBottom();
				Asiz.printBottom();
				
				Type.makeSibling(ID);
				Type.makeSibling(Asiz);
				Type.makeSibling(makeNode());
				
				Var.abandonChild();
				Var.adoptChildren(Type);
				
				Var.printBottom();
				
				
				
				
				Var.Type = "StatORVar";
				
				Var.printBottom();
				
				if(Temp.getLC() == null)
				{
					return Var;
				}
			}
			
			Var.makeSibling(Temp);
			Var.makeSibling(makeNode());
			
			StatOVar.abandonChild();
			StatOVar.adoptChildren(Var);
			
			StatOVar.printBottom();
			
			System.out.println("oho");
			return StatOVar;
		}
		else if(St.Type.equals("StatORVar"))
		{
			return St;
		}
		else if(St.getChild("Var") != null)
		{
			System.out.println("here");
			
			AST_Node T = isolate(St.getChildType("Type"));
			
			Var  = isolateWithChild(St.getChild("Var"));
			
			AST_Node Id = isolate(Var.getChild("ID"));
			AST_Node As = isolateWithChild(St.getChild("Var").getChild("Asiz"));
			AST_Node Hold = null;
			
			if(St.getChildType("StatORVar") != null)
			{
				Hold = isolateWithChild(St.getChildType("StatORVar"));
			}
			
			T.makeSibling(Id);
			T.makeSibling(As);
			
			if(Hold != null)
			{
				T.makeSibling(Id);
			}
			
			T.makeSibling(makeNode());
			
			StatOVar.abandonChild();
			
			StatOVar.adoptChildren(T);
			
			StatOVar.Type = "StatORVar";
			
			StatOVar.printBottom();
			
			return StatOVar;
		}
		
		
		return null;
		
	}
	
	
	public AST_Node makeStat(AST_Node Stat, AST_Node Stmt)
	{
		System.out.println("makeStat " + Stmt.Type);
		
		
		if(Stmt.Type.equals("AssStat"))
		{
			Stmt.Type = "Stat";
			return Stmt;
		}
		else if(Stmt.Type.equals("StatIF"))
		{
			Stmt.Type = "Stat";
			return Stmt;
		}
		else if(Stmt.Type.equals("StatFOR"))
		{
			Stmt.Type = "Stat";
			return Stmt;
		}
		else if(Stmt.Type.equals("StatGET"))
		{
			Stmt.Type = "Stat";
			return Stmt;
		}
		else if(Stmt.Type.equals("StatPUT"))
		{
			Stmt.Type = "Stat";
			return Stmt;
		}
		else if(Stmt.Type.equals("StatRETURN"))
		{
			Stmt.Type = "Stat";
			return Stmt;
		}
		else if(Stmt.Type.equals("Stat"))
		{
			Stmt.Type = "Stat";
			return Stmt;
		}
		else if(Stmt.Type.equals("StatORVar"))
		{
			return Stmt;
		}
		else if(Stmt.Type.equals("StatBlock"))
		{
			return Stmt;
		}
		
		return null;
		
	}
	
	public AST_Node makeAssStat(AST_Node Stat, AST_Node AsSt, AST_Node Var, AST_Node Expr)
	{
		
		AsSt = isolate(AsSt);
		
		Var = isolateWithChild(Var);
		Expr = isolateWithChild(Expr);
		
		Expr.Location = Var.Location;
		
		Var.makeSibling(Expr);
		Var.makeSibling(makeNode());
		
		AsSt.adoptChildren(Var);
		
		AsSt.printBottom();
		
		if(!(Stat.getName().equals("Sblok")))
		{
			AsSt.Name = "Stmt";
		}
		else
		{
			AsSt.Name = "Sblok";
		}
		
		AsSt.Type = "AssStat";
		
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return AsSt;
	}
	
	public AST_Node makeStatIF(AST_Node Stat, AST_Node relExr)
	{
		int count = 0;
		
		AST_Node Temp = Stat.getLC();
		AST_Node Temp2 = null;
		
		
		Stat.printBottom();
		relExr.printBottom();
		
		Stat.getChildType("StatBlock").printBottom();
		Stat.getChild("SblokEL").printBottom();
		
		AST_Node SB = isolateWithChild(Stat.getChildType("StatBlock"));
		AST_Node EL = isolateWithChild(Stat.getChild("SblokEL"));
		
		AST_Node Hold = null;
		
		if(Stat.getChild("SblokEL").getRSib().Type.equals("StatBlock"))
		{
			Hold = isolateWithChild(Stat.getChild("SblokEL").getRSib());
			
		}
		
		relExr = isolateWithChild(relExr);
		
		relExr.makeSibling(SB);
		relExr.makeSibling(EL);
		
		if(Hold != null)
		{
			relExr.makeSibling(Hold);
		}
		
		relExr.makeSibling(makeNode());
		
		Stat.Type = "StatIF";
		Stat.Value = "if";
		
		Stat.abandonChild();
		Stat.adoptChildren(relExr);
		
		Stat.printBottom();
		
		
		return Stat;

	}
	
	public AST_Node makeStatFOR(AST_Node Stat, AST_Node Type, AST_Node ID, AST_Node Exr, AST_Node relEx)
	{
		
		System.out.println("Making For");
		
		Stat.printBottom();
		
		Type = isolate(Type);
		ID = isolate(ID);
		Exr = isolateWithChild(Exr);
		relEx = isolateWithChild(relEx);
		
		Stat.printBottom();
		
		AST_Node Temp = isolateWithChild(getChildType("Stat"));
		
		AST_Node Temp2 = isolateWithChild(getChildType("StatBlock"));
		
		
		Type.makeSibling(ID);
		Type.makeSibling(Exr);
		Type.makeSibling(relEx);
		Type.makeSibling(Temp);
		Type.makeSibling(Temp2);
		Type.makeSibling(makeNode());
		
		Stat.Type = "StatFOR";
		Stat.Value = "for";
		
		Stat.abandonChild();
		Stat.adoptChildren(Type);
		
		Stat.printBottom();
		
		
		return Stat;
	}
	
	public AST_Node makeStatKW(AST_Node Stat, AST_Node KW)
	{
		System.out.println("Making last Stmt");
		
		AST_Node check = Stat.getLC();
		AST_Node Hold = null;
		
		if(Stat.getChildType("StatORVar") != null)
		{
			Hold = isolateWithChild(Stat.getChildType("StatORVar"));
		}
		
		if(check.Name.equals("GET"))
		{
			if(KW.Type.equals("Vari"))
			{
				Stat.abandonChild();
				
				Stat.Type = "StatGET";
				Stat.Value = "get";
				KW = isolateWithChild(KW);
				
				if(Hold != null)
				{
					KW.makeSibling(Hold);
				}
				
				KW.makeSibling(makeNode());
				
				Stat.abandonChild();
				Stat.adoptChildren(KW);
				Stat.printBottom();
				
				
				return Stat;
			}
			else
			{
				return null;
			}
			
		}
		else if(check.Name.equals("PUT"))
		{
			if(KW.Type.equals("Expr"))
			{
				Stat.abandonChild();
				
				Stat.Type = "StatPUT";
				Stat.Value = "put";
				
				KW.printBottom();
				KW = isolateWithChild(KW);
				
				if(Hold != null)
				{
					KW.makeSibling(Hold);
				}
				
				KW.makeSibling(makeNode());
				
				Stat.adoptChildren(KW);
				
				return Stat;
			}
			else
			{
				return null;
			}
			
		}
		else if(check.Name.equals("RETURN"))
		{
			if(KW.Type.equals("Expr"))
			{
				Stat = isolate(Stat);
				Stat.abandonChild();
				
				Stat.Type = "StatRETURN";
				Stat.Value = "return";
				KW = isolateWithChild(KW);
				
				if(Hold != null)
				{
					KW.makeSibling(Hold);
				}
				
				KW.makeSibling(makeNode());
				
				Stat.adoptChildren(KW);
				Stat.printBottom();
				
				return Stat;
				
			}
			else
			{
				return null;
			}
			
		}
		else
		{
			return null;
		}
	}
	
	public AST_Node makeExr(AST_Node Exr, AST_Node secon)
	{
		
		System.out.println("MADE EXPR");
		
		if(Exr.Type.equals("Rexpr"))
		{
			return Exr;
		}
		
		
		Exr.Type = "Expr";
		Exr.printBottom();
		secon.Type = "Expr";
		secon.printBottom();
		
		
		secon.Name = "Expr";
		System.out.println("Secon");
		secon.printBottom();
		
		if(secon.getChild("TermD") != null)
		{
			secon.getChild("TermD").printBottom();
			
			AST_Node Mul = secon.getChild("TermD").getLC();
			AST_Node Term = Mul.getRSib();
			AST_Node Add = null;
			
			if(Term.getRSib() != null)
			{
				if(AddTest(Term.getRSib().getValue()))
				{
					Add = isolateWithChild(Term.getRSib());
				}
			}
			
			Mul = isolate(Mul);
			Term = isolateWithChild(Term);
			
			secon.getLC().printBottom();
			AST_Node Fact = isolateWithChild(secon.getLC().getLC());
			
			Fact.Type = "Term";
			Fact.Name = "Term";
			
			Fact.printBottom();
			
			Fact.makeSibling(Term);
			Fact.makeSibling(makeNode());
			Mul.adoptChildren(Fact);
			
			if(Add != null)
			{
				AST_Node Hold = isolateWithChild(Add.getLC());
				
				Add = isolate(Add);
				
				Mul.makeSibling(Hold);
				Mul.makeSibling(makeNode());
				
				Mul.Type = "Term";
				
				Add.Name = "Expr";
				Add.Type = "Expr";
				
				Add.adoptChildren(Mul);
				Add.printBottom();
				
				
				return Add;
				
			}
				
			
			Mul.printBottom();
			
			
			
			return Mul;
		}
		
		
		secon = isolateWithChild(secon);
		
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		if(secon.getChildType("VarEl") != null)
		{
			secon.printBottom();
			
			AST_Node Left =  isolateWithChild(secon.getChildType("VarEl").getLC());
			
			AST_Node Right = isolateWithChild(secon.getChildType("VarEl").getLC().getRSib());
			
			Left.makeSibling(Right);
			
			Left.makeSibling(makeNode());
			
			Exr.abandonChild();
			Exr.adoptChildren(Left);
			
			
			Exr.printBottom();
			
			
			return Exr;
		}
		
		return secon;
			
		
	}
	
	public AST_Node makeAexr(AST_Node Aexr, AST_Node Add, AST_Node Aepr, AST_Node Term)
	{
		Add.Type = "Add";
		Add.Name = "Aexpr";
		
		Aexr.printBottom();
		Add.printBottom();
		Aepr.printBottom();
		Term.printBottom();
		
		if(Add.getChild("Fact") != null)
		{
			if(Add.AddTest(Add.getChild("Fact").getValue()))
			{
				
				AST_Node Left = isolateWithChild(Term.getChild("Fact"));
				AST_Node Hold = isolateWithChild(Add.getLC());
				
				AST_Node Right = isolateWithChild(Hold.getChild("Term"));
				AST_Node Mult = isolateWithChild(Hold.getChildType("Mult"));
				
				if(Left.getLC() != null)
				{
					if(Left.getLC().getType().equals("VarEl"))
					{
						Left = isolateWithChild(Left.getLC());
					}
				}
				
				Left.printBottom();
				Right.printBottom();
				Mult.printBottom();
				
				Hold.abandonChild();
				
				Left.makeSibling(Mult);
				Left.makeSibling(makeNode());
				
				Hold.adoptChildren(Left);
				
				
				Add.abandonChild();
				
				
				Hold.makeSibling(makeNode());
				
				Add.adoptChildren(Hold);
				
				Add.Type = "Aexpr";
				
				Add.printBottom();
				
				
				return Add;
			}
		}
		
		
		//Aepr = isolateWithChild(Aepr.searchFunction(Aepr, "Term"));
		Term = isolateWithChild(Term);
		
		if(Aepr.getName().equals("AexprD"))
		{
			
			Aexr.printBottom();
			Add.printBottom();
			Aepr.printBottom();
			Term.printBottom();
			
			Aexr = isolateWithChild(Add.getRSib());
			Add= isolate(Add);
			Term = isolateWithChild(Term);
			
			Term.Name = "Aexpr";
			Term.Type = "Aexpr";
			
			Term.makeSibling(Aexr);
			Term.makeSibling(makeNode());
			
			Add.adoptChildren(Term);
			
			return Add;
			
		}
		
		Term.Name = "Aexpr";
		Term.Type = "Aexpr";
		
		Term.makeSibling(Aepr);
		Term.makeSibling(makeNode());
		
		Add.abandonChild();
		Add.adoptChildren(Term);
		
		Add.printBottom();
		Term.printBottom();
		
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//System.exit(0);
		return Add;
		
	}
	
	public AST_Node makeAexr(AST_Node Aexr, AST_Node Term)
	{
		
		
		if(Term.getChild("Add") != null)
		{
			AST_Node Add = isolate(Term.getChild("Add"));
			AST_Node Mult = isolateWithChild(Aexr.getChildType("Mult"));
			AST_Node Right = isolateWithChild(Term.getChild("Term"));
			
			Mult.makeSibling(Right);
			Mult.makeSibling(makeNode());
			
			Add.adoptChildren(Mult);
			
			Add.Name = "Aexpr";
			
			Add.printBottom();
			
//			try {
//				System.in.read();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			System.exit(0);
			
			return Add;
		}
		
		
		
		Aexr.printBottom();
		Term.printBottom();
		
		if(Aexr.getChild("AexprD") != null)
		{
			System.out.println("AexrD");
			Aexr.getChild("AexprD").printBottom();
			Aexr.getChild("Term").printBottom();
			
			if(Aexr.getChild("Term") != null)
			{
				System.out.println("Term");
				if(Term.getChild("TermD") != null)
				{
					System.out.println("HHHHH");
					Term.getChild("TermD").printBottom();
					
					
					//System.exit(0);
				}
				
				if(Term.getChildType("Fact") == null)
				{
					System.out.println("Fact");
					AST_Node T = isolateWithChild(Aexr.getChild("Term"));
					
					if(T.getLC() != null)
					{
						System.out.println("T");
						Aexr.getChild("Term").getRSib().printBottom();
						T.printSiblings();
						T.getLC().printBottom();
						System.out.println("T2");
						
						if(T.getLC().getRSib() != null)
						{
							if(T.getLC().getRSib().getChild("Fact") != null)
							{	
								if(AddTest(T.getLC().getRSib().getChild("Fact").Value))
								{
									System.out.println("Second");
									AST_Node P = isolateWithChild(T.getLC().getRSib().getChild("Fact"));
									
									P.printBottom();
									
									AST_Node Hold = null;
									
									
									if(P.getLC().getChild("Term") != null)
									{
										Hold = isolateWithChild(P.getLC().getChild("Term"));
									}
									else
									{
										Hold = P.getLC();
									}
									
									
									if(T.getLC().getChild("Fact") != null)
									{
										T = isolateWithChild(T.getLC().getChild("Fact"));
									}
									
									T.printBottom();
									P.printBottom();
									Hold.printBottom();
									
									if(AddTest(Hold.getValue().trim()))
									{
										Hold = isolateWithChild(Hold.getLC());
									}
									
									T.makeSibling(Hold);
									T.makeSibling(makeNode());
									
									P.abandonChild();
									P.adoptChildren(T);
									
									P.Name = "Aexpr";
									
									P.printBottom();
									
									System.out.println("P");
									
									
									System.out.println("Stop");
//									
									
									return P;
								}
							}
						}
						
						if(T.getLC().getChild("Fact") != null)
						{
							System.out.println("F");
							AST_Node F = isolateWithChild(T.getLC().getChild("Fact"));
							
							F.printBottom();
							
							F.Name = "Aexpr";
							
							System.out.println("F");
							
//							try {
//								System.in.read();
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
							
							return F;
						}
					}
					
					System.out.println("5");
					T.Name = "Aexpr";
					System.out.println("6");
					T.printBottom();
					
//					try {
//						System.in.read();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
					
					System.out.println("T");
					
					return T;
				}
			}
		}
		
//		if(Aexr.getChild("Term") != null)
//		{
//			System.exit(0);
//		}
		
		
		Term.Type = "Aexpr";
		Term.Name = "Aexpr";
		
		Term = isolateWithChild(Term);
		
		
		
		if(Aexr.getChildType("Mult") != null)
		{
			System.out.println("Mult");
			Aexr.getChildType("Mult").Name = "Aexpr";
			
			Aexr.getChildType("Mult").printBottom();
			
			
//			try {
//				System.in.read();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			return Aexr.getChildType("Mult");
		}
		
		
		if(Term.getChild("TermD") != null)
		{
			System.out.println("TermD");
			if(this.AddTest(Term.getChild("TermD").getChild("Fact").getValue()))
			{
				Term.printBottom();
				
				
				AST_Node Mult = isolateWithChild(Term.getChild("Fact").getChild("Fact"));
				AST_Node Add = isolateWithChild(Term.getChild("TermD").getChild("Fact"));
				
				AST_Node Hold = isolateWithChild(Term.getChild("TermD").getChild("Fact").Left_Most_Child);
				
				Mult.printBottom();
				Add.printBottom();
				Hold.printBottom();
				
				Mult.makeSibling(Hold);
				Mult.makeSibling(makeNode());
				
				Add.abandonChild();
				Add.adoptChildren(Mult);
				
				Add.Type = "Add";
				Add.Name = "Aexpr";
				
				Add.printBottom();
				Mult.printBottom();
				
				
				
				
				return Add;
			}
			
		}
			
		return Term;
	}
	
	public AST_Node makeRelExr(AST_Node relExr, AST_Node Rop)
	{
		
		relExr.printBottom();
		System.out.println("Making relexpr");
		
		AST_Node Temp = null;
		AST_Node Temp2 = null;
		
		Rop.printBottom();
				
		if(relExr.getChild("ExprD") != null)
		{
			AST_Node Left = isolateWithChild(relExr.getChild("Aexpr"));
			AST_Node Right = isolateWithChild(relExr.getChild("ExprD").getChild("Aexpr"));
			AST_Node Add = null;
			AST_Node Mul = null;
			
			Left.printBottom();
			
			if(Left.getChild("TermD") != null)
			{
				Left.getChild("TermD").printBottom();
				
				Mul = Left.getChild("TermD").getLC();
				AST_Node Term = Mul.getRSib();
				
				
				if(Term.getRSib() != null)
				{
					if(AddTest(Term.getRSib().getValue()))
					{
						Add = isolateWithChild(Term.getRSib());
					}
				}
				
				Mul = isolate(Mul);
				Term = isolateWithChild(Term);
				
				Left.getLC().printBottom();
				AST_Node Fact = isolateWithChild(Left.getLC().getLC());
				
				Fact.Type = "Term";
				Fact.Name = "Term";
				
				Fact.printBottom();
				
				Fact.makeSibling(Term);
				Fact.makeSibling(makeNode());
				Mul.adoptChildren(Fact);
				
				if(Add != null)
				{
					AST_Node Hold = isolateWithChild(Add.getLC());
					
					Add = isolate(Add);
					
					Mul.makeSibling(Hold);
					Mul.makeSibling(makeNode());
					
					Mul.Type = "Term";
					
					Add.Name = "Expr";
					Add.Type = "Expr";
					
					Add.adoptChildren(Mul);
					Add.printBottom();
					
					
				}
				Mul.printBottom();
				
			}
			
			if(Add != null)
			{
				Left = Add;
			}
			else if(Mul != null)
			{
				Left = Mul;
			}
			
			
			Rop = isolate(Rop);
			
			Left.makeSibling(Rop);
			Left.makeSibling(Right);
			Left.makeSibling(makeNode());
			
			relExr.abandonChild();
			relExr.adoptChildren(Left);
			
			relExr.Type = "Rexpr";
			
			relExr.printBottom();
			Left.printBottom();
			Right.printBottom();
			
			relExr.Value = "";
			
			return relExr;
		}
		
		if(relExr.getLC() != null)
		{
			AST_Node Right = isolateWithChild(relExr.getLC());
			AST_Node Left = isolateWithChild(Rop.getRSib());
			
			Right.printBottom();
			Left.printBottom();
			
			if(Right.getChildType("VarEl") != null)
			{
				Right = isolateWithChild(Right.getChildType("VarEl"));
			}
			
			if(Left.getChildType("VarEl") != null)
			{
				Left = isolateWithChild(Left.getChildType("VarEl"));
			}
			
			Rop = isolate(Rop);
			
			Right.makeSibling(Rop);
			Right.makeSibling(Left);
			Right.makeSibling(makeNode());
			
			relExr.abandonChild();
			
			relExr.adoptChildren(Right);
			
			relExr.printBottom();
			
			
		}
		
		
		
		Rop.getRSib().printBottom();
		relExr.Type = "Rexpr";
		relExr.Value = "";
		
		relExr.printBottom();
		
		return relExr;
		
		
		
	}
	
	public AST_Node makeTerm(AST_Node Term, AST_Node Mult, AST_Node Fact)
	{
		AST_Node Right = null;
		
		
		System.out.println("Make term");
		
		Term.printBottom();
		Mult.printBottom();
		
		Fact.printBottom();
		
		
		
		if(Fact.Type.equals("Add"))
		{
			Fact.Name = "Term";
			
			return Fact;
			
		}
		
		if(Term.Type.equals("Add"))
		{
			return Term;
			
		}
		
		if(Term.getType().equals("Mult"))
		{
			return Term;
		}
		
		if(Term.Name.equals("Term"))
		{
			if(Term.MultTest(Term.getLC().Value))
			{
				
				Term.getLC().Name = "Term";
				Term.getLC().Type = "Mult";
				
				return Term;
			}
			
			AST_Node RS = isolateWithChild(Term.getChild("TermD").getChild("Fact"));
			
			RS.printBottom();
			
			Mult = isolate(Mult);
			
			Fact = isolateWithChild(Fact);
			
			Fact.makeSibling(RS);
			Fact.makeSibling(makeNode());
			
			Mult.adoptChildren(Fact);
			
			Mult.Name = "Term";
			
			Mult.printBottom();
			
			return Mult;
			
		}
		
		if(Term.Name.equals("TermD"))
		{
			Mult = isolate(Mult);
			Fact = isolateWithChild(Fact);
			
			
			Mult.makeSibling(Fact);
			Mult.makeSibling(makeNode());
			
			Term.Type = "TermDed";
			
			Term.abandonChild();
			Term.adoptChildren(Mult);
			
			Term.printBottom();
			
			
			if(Term.getChild("Fact").getChild("TermD") != null)
			{
				Fact.printBottom();
				AST_Node Temp4 = isolateWithChild(Term.getChild("Fact").getChild("TermD").getChild("Fact"));
				AST_Node Hold = isolateWithChild(Temp4.getChild("Term"));
				AST_Node Temp5 = isolateWithChild(Fact.getChild("Fact"));
				
				Hold.printBottom();
				
				Temp5.makeSibling(Hold);
				Temp5.makeSibling(makeNode());
				
				Temp4.abandonChild();
				Temp4.adoptChildren(Temp5);
				
				Mult = isolate(Mult);
				
				Mult.Type = "Mult";
				
				Mult.makeSibling(Temp4);
				Mult.makeSibling(makeNode());
				
				Term.abandonChild();
				Term.adoptChildren(Mult);
				
				Term.printBottom();
				Term.getChild("Fact").printBottom();
				
			}
			
			
			if(Term.getChild("Fact") != null)
			{	
				
				Term.getChild("Fact").printBottom();
				
				
				
			}
			
			return Term;
			
		}
		
		if(Term.getChildType("TermDed")!= null)
		{
			Term.getChildType("TermDed").printBottom();
			Term.getChildType("TermDed").getChild("Fact").printBottom();
			

		}
		
		
		
		if(Mult.getRSib() != null)
		{
			Right = isolateWithChild(Mult.getRSib());
		}
		
		Fact = isolate(Fact);
		
		Fact.makeSibling(Right);
		Fact.makeSibling(makeNode());
		
		Mult.abandonChild();
		Mult.adoptChildren(Fact);
		
		
		Mult.Type = "Mult";
		
		Mult.R_Sibling = Term.R_Sibling;
		
		if(Mult.getChild("Fact")!= null)
		{
			Mult.getChild("Fact").Name = "Term";
			Mult.getChild("Fact").printBottom();
		}
		
		Mult.printBottom();
		
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return Mult;
	}
	
	public AST_Node makeTerm(AST_Node Term, AST_Node Fact)
	{
		Term.printBottom();
		Fact.printBottom();
		
		//Term.getChild("TermD").printBottom();
		
		if(Fact.getLC() != null)
		{
			if(Fact.getLC().Name.equals("Aexpr"))
			{
				Term.printBottom();
				Fact.printBottom();
				
				Fact.getLC().printBottom();
				
				System.exit(0);
			}
		}
		
		System.out.println("MakeTerm");
		
		Fact = isolateWithChild(Fact);
		
		System.out.println("STOP");
		
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
		
		Fact.Name = "Term";
		
		Fact.printBottom();
		
		
		return Fact;
	}
	
	
	public AST_Node makeFact(AST_Node Fact, AST_Node Second)
	{
		if(Fact.Name.equals("TermD"))
		{
			Fact.printBottom();
			Second.printBottom();
			
			AST_Node Mult = isolate(Fact.getChild("Mult"));
			AST_Node Add = isolateWithChild(Second);
			AST_Node Hold = isolateWithChild(Second.getChild("Fact"));
			
			Add.abandonChild();
			Add.adoptChildren(Hold);
			
			Mult.makeSibling(Add);
			Mult.makeSibling(makeNode());
			
			Fact.abandonChild();
			Fact.adoptChildren(Mult);
			
			Mult.Name = "Term";
			Mult.Type = "Mult";
			Fact.printBottom();
			Mult.printBottom();
			Add.printBottom();
			
			System.out.println("Fact");
			
//			try {
//				System.in.read();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			return Fact;
			
		}
		else if(Second.Type.equals("Vari"))
		{
			
			Second.printBottom();
			
			//Second = isolateWithChild(Second.getChildType("VarEl"));
			
			Second.Name = "Fact";
			Second.Type = "Fact";
			
			Second.printBottom();
			
			return Second;
		}
		else if(Second.Type.equals("INT") || Second.Type.equals("FLOAT") || Second.Type.equals("ID"))
		{
			Second.Type = "Fact";
			Second.Name = "Fact";
			
			Fact.printBottom();
			
			String hold = Fact.Left_Most_Child.getValue();
			
			System.out.println(hold);
			
			Second.printNode();
			System.out.println();
			
			Second = isolate(Second);
			
			Second.printNode();
			System.out.println();
			Second.printBottom();
			
			
			
			return Second;
		}
		else if(Second.Name.equals("FuncCall"))
		{
			Second.Type = "Fact";
			Second.Name = "Fact";
			
			Second = isolateWithChild(Second);
			
			return Second;
		}
		else if(Second.Name.equals("Aexpr"))
		{
			Second.Type = "Fact";
			Second.Name = "Fact";
			
			Second = isolateWithChild(Second);
			
			Second.printBottom();
			
//			try {
//				System.in.read();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			return Second;
		}
		else if(Second.Name.equals("Sign"))
		{
			AST_Node Temp = isolateWithChild(Fact.getChild("Fact"));
			
			Temp.makeSibling(makeNode());
			
			Second = isolate(Second);
			
			Second.abandonChild();
			Second.adoptChildren(Temp);
			
			Second.Type = "Fact";
			Second.Name = "Fact";
			
			
			return Second;
		}
		else
		{
			return null;
		}
	}
	
	public boolean valueComp(String Value, String Test)
	{
		if(Test.equals("+"))
		{
			if((int)Value.charAt(0) == 43)
			{
				return true;
			}
		}
		else if(Test.equals("not"))
		{
			if((int)Value.charAt(0) == 110)
			{
				return true;
			}
		}
		else if(Test.equals("-"))
		{
			if((int)Value.charAt(0) == 45)
			{
				return true;
			}
		}
		else if(Test.equals("*"))
		{
			if((int)Value.charAt(0) == 42)
			{
				return true;
			}
		}
		
		
		return false;
	}
	
	public boolean AddTest(String Value)
	{
		if(Value.equals(""))
		{
			return false;
		}
		else if((int)Value.charAt(0) == 43)
		{
			return true;
		}
		else if((int)Value.charAt(0) == 111)
		{
			return true;
		}
		else if((int)Value.charAt(0) == 45)
		{
			return true;
		}
		
		return false;
	}
	
	public boolean MultTest(String Value)
	{
		if(Value.equals(""))
		{
			return false;
		}
		else if((int)Value.charAt(0) == 42)
		{
			return true;
		}
		else if((int)Value.charAt(0) == 47)
		{
			return true;
		}
		else if((int)Value.charAt(0) == 97)
		{
			return true;
		}
		
		
		return false;
	}
	
	public boolean NumTest(String Value)
	{
		if(Value.equals(""))
		{
			return false;
		}
		else if((int)Value.charAt(0) > 47 && (int)Value.charAt(0) < 61)
		{
			return true;
		}
		
		
		
		return false;
	}
	
	public boolean RelTest(String Value)
	{
		if(Value.equals(""))
		{
			return false;
		}
		else if((int)Value.charAt(0) == 61)
		{
			if((int)Value.charAt(1) != 0)
			{
				return true;
			}
		}
		else if((int)Value.charAt(0) == 60)
		{
			return true;
		}
		else if((int)Value.charAt(0) == 62)
		{
			return true;
		}
		
		
		return false;
	}
	
	public boolean EqTest(String Value)
	{
		if(Value.equals(""))
		{
			return false;
		}
		if((int)Value.charAt(0) == 61)
		{
			return true;
		}
		
		
		return false;
	}
	
	public boolean ReturnTest(String Value)
	{
		if(Value.equals(""))
		{
			return false;
		}
		if((int)Value.charAt(0) == 114)
		{
			return true;
		}
		
		
		return false;
	}
	
	public boolean GetTest(String Value)
	{
		if(Value.equals(""))
		{
			return false;
		}
		if((int)Value.charAt(0) == 103)
		{
			return true;
		}
		
		
		return false;
	}
	
	public boolean PutTest(String Value)
	{
		if(Value.equals(""))
		{
			return false;
		}
		if((int)Value.charAt(0) == 112)
		{
			return true;
		}
		
		
		return false;
	}
	
	public boolean IfTest(String Value)
	{
		if(Value.equals(""))
		{
			return false;
		}
		if((int)Value.charAt(0) == 105)
		{
			return true;
		}
		
		
		return false;
	}
	
	
	public boolean ForTest(String Value)
	{
		if(Value.equals(""))
		{
			return false;
		}
		if((int)Value.charAt(0) == 102)
		{
			return true;
		}
		
		
		return false;
	}
	

	public AST_Node makeFact(AST_Node Fact, AST_Node Top, AST_Node Second)
	{
		System.out.println("here");
		Fact.printBottom();
		Top.printBottom();
		Second.printBottom();
		
		System.out.println(Top.getValue());
		System.out.println(Top.Value.equals("+"));
		
		System.out.println(valueComp(Top.getValue(), "-"));
		
		
		if(Top.getRSib().Type.equals("Add"))
		{
			
			AST_Node Hold = Second.getLC();
			
			Second = isolate(Second);
			
			Top = isolate(Top);
			
			Top.adoptChildren(Hold);
			
			Top.Name = "Fact";
			Top.Type = "Fact";
			
			Second.Name = "Fact";
			Second.adoptChildren(Top);
			
			Second.printBottom();
			
			return Second;
		}
		
//		try {
//			System.in.read();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		if(valueComp(Top.getValue(), "not") || valueComp(Top.getValue(), "-")|| valueComp(Top.getValue(), "+"))
		{
			Top.Type = "Fact";
			Top.Name = "Fact";
			
			Second.Type = "Fact";
			Second.Name = "Fact";
			
			Top.isolate(Top);
			
			Second = isolateWithChild(Second);
			
			if(Second.Left_Most_Child != null)
			{
				Second.Left_Most_Child.Type = "Term";
				Second.Left_Most_Child.Name = "Term";
			}
			Top.abandonChild();
			Top.adoptChildren(Second);
			
			Top.printBottom();
			
//			try {
//				System.in.read();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			return Top;
		}
		else
		{
			return null;
		}
		
		
	}
	
	public AST_Node makeVar(AST_Node Var, ArrayList<AST_Node> Velm)
	{
		AST_Node Temp = isolateWithChild(Velm.get(0));
		AST_Node Temp2 = null;
		AST_Node N_Var = new AST_Node();
		
		N_Var.Type = "Vari";
		N_Var.Name = "IDD";
		
		Temp.printBottom();
		
		
		for(int i=1; i<Velm.size(); i++)
		{
			if((!Velm.get(i).getParent().Type.equals("Aexpr")))
			{
				if((!Velm.get(i).getLC().Type.equals("NULL")))
				{
					Temp2 = isolateWithChild(Velm.get(i));
					Temp2.printBottom();
					Temp.makeSibling(Temp2);
					
					
					
				}
				
			}
			
		}
		
		Temp.makeSibling(makeNode());
		
		
		N_Var.adoptChildren(Temp);
		
		N_Var.printBottom();
		
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		System.out.println("VARRII");
		
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return N_Var;
	}
	
	public AST_Node makeDmem(AST_Node Dmem, AST_Node ID, AST_Node Ili)
	{
		Dmem.Type = "DMemb";
		
		System.out.println("Making the List");
		System.out.println("ID Type" + ID.Type);
		
		if(Ili.getSibling("E") != null)
		{
			if(Ili.searchFunction(Ili.getSibling("E"), "IDn") != null)
			{
				Ili = Ili.searchFunction(Ili.getSibling("E"), "Aexpr");
				
				
			}
		}
		
		
		
		ID = isolate(ID);
		Ili = isolateWithChild(Ili);
		
		Ili.printBottom();
		
		ID.makeSibling(Ili);
		
		
		ID.makeSibling(makeNode());
		
		Dmem.abandonChild();
		
		Dmem.adoptChildren(ID);
		
		Dmem.printBottom();
		
		System.out.println("STTTOTOOTOTT");
		
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return Dmem;
	}
	
	public AST_Node makeFcall(AST_Node Fcall, AST_Node ID, AST_Node aPara)
	{
		Fcall.Type = "FuncCall";
		
		
		ID = isolate(ID);
		aPara = isolateWithChild(aPara);
		
		ID.makeSibling(aPara);
		ID.makeSibling(makeNode());
		
		Fcall.abandonChild();
		Fcall.adoptChildren(ID);
		
		Fcall.printBottom();
		
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		Fcall.getChild("Apara").printBottom();
		
		return Fcall;
	}
	
	public AST_Node makeInL(AST_Node InL, ArrayList<AST_Node> Aexr)
	{
		InL.Type = "InL";
		
		InL = isolateWithChild(InL);
		
		
		
		AST_Node Temp = isolateWithChild(Aexr.get(0));
		AST_Node Temp2 = null;
		
		InL.printBottom();
		
		Temp.printBottom();
		
		for(int i=1; i<Aexr.size(); i++)
		{
			
			Temp2 = isolateWithChild(Aexr.get(i));
			System.out.println("bottom " + i);
			Temp2.printBottom();
			
			Temp.makeSibling(Temp2);
		}
		
		Temp.makeSibling(makeNode());
		
		InL.abandonChild();
		
		InL.adoptChildren(Temp);
		
		InL.printBottom();
		
		if(InL.getLC().getType().equals("InL"))
		{
			InL.getLC().Type = "Aexpr";
			
		}
		
		
		
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		return InL;
	}
	
	public AST_Node makeInL(AST_Node InL)
	{
		InL.Type = "InL";
		
		InL.abandonChild();
		InL.adoptChildren(makeNode());
		
		InL.printBottom();
		
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return InL;
	}
	
	
	public AST_Node makeApara(AST_Node Apara, ArrayList<AST_Node> Exr)
	{
		Apara.printBottom();
		
		if(Exr.size()>1)
		{
			System.exit(0);
		}
		else
		{
			AST_Node Temp = null;
			AST_Node Left = isolateWithChild(Exr.get(0));
			
			
			if(Apara.getChild("Aparat") != null)
			{
				if(Apara.getChildren().get(1).getLC() != null)
				{
					Temp = Apara.getChild("Aparat");
					Exr = this.RetrieveClasses(Temp, "Aparat", Exr);
					
					System.out.println(Exr.size());
					
					for(int i=1; i<Exr.size(); i++)
					{
						System.out.println("bottom" + i);
						
						if(Exr.get(i).getChildType("Expr") != null)
						{
							Temp = isolateWithChild(Exr.get(i).getChildType("Expr"));
							if(Temp != null)
							{
								Left.makeSibling(Temp);
							}
						}
					}
				}
			}
			
			
			
			
			Left.makeSibling(makeNode());
			
			Apara.Type = "Aparam";
			Apara.abandonChild();
			Apara.adoptChildren(Left);
			
			Apara.printBottom();
			
			
			return Apara;
		}
		
		
		return null;
	}
	
	public AST_Node TreeParent()
	{
		AST_Node Parent = this;
		
		while(Parent.Parent != null)
		{
			if(Parent.Parent != null)
			{
				Parent = Parent.Parent;
			}
		}
		
		return Parent;
	}
	
	public void printNode()
	{
		if(this.Type.equals("IntNum"))
		{
			System.out.print(this.IntNum + " ");
		}
		else if(this.Type.equals("FloatNum"))
		{
			System.out.print(this.FloatNum + " ");
		}
		else if(this.Type.equals("ID"))
		{
			if(this.Id == null)
			{
				ID temp = new ID(this.Value.trim());
				this.setID(temp);
				
			}
			System.out.print(this.Id.getName() + " ");
		}
		else
		{
			System.out.print(this.getName() + " Type:" + this.getType() + " Value: " + this.getValue());
		}
		
	}
	
	public void printSiblings()
	{
		AST_Node Sib = this;
		Sib = Sib.L_Most_Sibling;
		
		//System.out.print("Siblings: ");
		
		while(Sib != null)
		{
			if(Sib != null)
			{
				Sib.printNode();
				Sib = Sib.R_Sibling;
			}
		}
		System.out.println();
	}
	
	public void printFamily()
	{
		AST_Node Parent = this;
		
		while(Parent.Parent != null)
		{
			if(Parent.Parent != null)
			{
				Parent = Parent.Parent;
			}
		}
		
		System.out.print("Head: ");
		Parent.printNode();
		System.out.println();
		
		while(Parent.Left_Most_Child != null)
		{
			if(Parent.Left_Most_Child != null)
			{
				Parent = Parent.Left_Most_Child;
				Parent.printSiblings();
			}
		}
		
	}
	
	public void printBottom()
	{
		AST_Node Parent = this;
		
		System.out.print("Head: ");
		Parent.printNode();
		System.out.println();
		
		while(Parent.Left_Most_Child != null)
		{
			if(Parent.Left_Most_Child != null)
			{
				Parent = Parent.Left_Most_Child;
				Parent.printSiblings();
			}
		}
	}
	
	public void printTree()
	{
		AST_Node Parent = this;
		
		while(Parent.Parent != null)
		{
			if(Parent.Parent != null)
			{
				Parent = Parent.Parent;
			}
		}
		
		System.out.print(Parent.Name + " ");
		
		while(Parent.Left_Most_Child.Left_Most_Child != null)
		{
			if(Parent.Left_Most_Child.Left_Most_Child != null)
			{
				Parent = Parent.Left_Most_Child;
				System.out.print(Parent.Name + " ");
			}
		}
		
		
	}
	
	public AST_Node searchFunction(AST_Node Head, String Target)
	{
		AST_Node Return = null;
		
		String Temp = "";
		int beg = 0;
		int end = 0;
		
		int count = 0;
		
		for(int i=0; i<Target.length(); i++)
		{
			if(Target.charAt(i) == '-'|| Target.length()-1 == i)
			{
				if(i != Target.length()-1)
				{
					end = i;
					Temp = Target.substring(beg, end);
					beg = end+1;
				}
				else
				{
					end = i+1;
					Temp = Target.substring(beg, end);
					beg = end;
				}
				
				System.out.println("SEarch Function " + Temp+"x");
				
				Return = searchFunction2(Head, Temp, count);
				count += 1;
				
				if(count > 100)
				{
					System.exit(0);
				}
				
				if(Return != null)
				{
					return Return;
				}
				
			}
		}
		
		
		return null;
	}
	
	
	public AST_Node searchFunction2(AST_Node Head, String Target, int count)
	{
		
		System.out.println("Inside the search func " + Head.getName() + " " + Head + " " + Head.getType());
		
		if(Head.getName().equals(Target) || Head.getType().equals(Target))
		{
			System.out.println("Found as Head " + Head.getName() );
			return Head;
		}
		
		/*if(Head.getSibling(Target)!= null && Head.getSibling(Target).getType().equals(Target))
		{
			Head = Head.getSibling(Target);
			System.out.println("Found as Sibling " + Head.getName() );
			System.out.println(Target);
			System.exit(0);
			
			return Head;
		}*/
		
		if(Head.getLC() != null)
		{
			count += 1;
			System.out.println("Travel Left down: " + Head.getName());
			Head = Head.getLC();
			return searchFunction(Head, Target);
			
		}
		
		
		if(Head.getRSib() != null)
		{
			System.out.println("Travel Right: " + Head.getName());
			Head = Head.getRSib();
			return searchFunction(Head, Target);
			
		}
		else
		{
			while(Head.getRSib() == null)
			{
				Head = Head.Parent;
				//System.out.println("WHILE Head " + Head.getName());
				if(Head == null)
				{
					break;
				}
				
				if(Head.getRSib() != null)
				{
					//System.out.println("Traveling Up and to the right " + Head.getName());
					Head = Head.getRSib();
					return searchFunction(Head, Target);
				}
				
			}
		}
		
		return null;
	}
	
	public ArrayList<AST_Node> searchVector(AST_Node Head, String Target, ArrayList<AST_Node> Vec)
	{
		
		
		System.out.println("Head VecS " + Head.getName() +" " + Head + " " + " Type " + Head.getType());
		
		if(Head.getName().equals(Target) || Head.getType().equals(Target))
		{
			System.out.println("Found as Head " + Head.getName() );
			Vec.add(Head);
			
		}
		
		if(Head.getLC() != null)
		{
			System.out.println("Travel Left down: " + Head.getName());
			Head = Head.getLC();
			return searchVector(Head, Target, Vec);
			
		}
		
		
		if(Head.getRSib() != null)
		{
			System.out.println("Travel Right: " + Head.getName());
			Head = Head.getRSib();
			return searchVector(Head, Target, Vec);
			
		}
		
		
		
		return Vec;
	}
	
	public ArrayList<AST_Node> searchVectorUP(AST_Node Head, String Target, ArrayList<AST_Node> Vec)
	{
		
		
		while(Head.getParent() != null)
		{
			System.out.println("Vector going up " + Target + " " + Head.getName() + " "+ " " + Head + " " + Head.getType());
			
			Head = Head.getParent();
			
			
			if(Head.getType().equals(Target))
			{
				System.out.println("Found head " + Head.getName() + " " + Head.getType());
				Vec.add(Head);
			}
			
			
		}
		
		
		return Vec;
	}
	
	public AST_Node moveUP(AST_Node Head, String Target)
	{
		AST_Node Temp = new AST_Node(Head);
		
		int beg = 0;
		int end = 0;
		String store = "";
		String store2 = "";
		for(int i=0; i<Target.length(); i++)
		{
			if(Target.charAt(i) == '-'|| Target.length()-1 == i)
			{
				if(i != Target.length()-1)
				{
					end = i;
					store = Target.substring(beg, end);
					beg = end+1;
				}
				else
				{
					end = i+1;
					store2 = Target.substring(beg, end);
					beg = end;
				}
				
				
			}
		}
		
		
		while(Temp.getParent() != null)
		{
			System.out.println("Search Up going up " + Target + " " + Temp.getName() + " "+ " " + Temp + " " + Temp.getType());
			System.out.println("Searching " + store + " " + store2);
			Temp = Temp.getParent();
			
			
			if(Temp.getName().equals(store) || Temp.getName().equals(store2))
			{
				System.out.println("Found head " + Temp.getName() + " " + Temp.getType());
				return Temp;
			}
			
			
		}
		
		
		return Head;
	}
	
	public AST_Node moveUPTerm(AST_Node Head, String Target)
	{
		AST_Node Temp = new AST_Node(Head);
		AST_Node Temp2 = new AST_Node(Head);
		
		
		
		while(Temp.getName().equals(Temp2.getName()))
		{
			System.out.println("Search Up going up " + Target + " " + Temp.getName() + " "+ " " + Temp + " " + Temp.getType());
			
			if(Temp.getParent() != null)
			{
				Temp = Temp.getParent();
				
				if(Temp.getName().equals(Temp2.getName()))
				{
					Temp2 = Temp;
				}
				else
				{
					System.out.println("Reached " + Temp.getName() + " " + "returned " + Temp2.getName());
					return Temp2;
				}
				
			}
			
			
		}
		
		
		return Head;
	}
	
	public AST_Node searchUP(AST_Node Head, String Target)
	{
		AST_Node Temp = new AST_Node(Head);
		
		
		while(Temp.getParent() != null)
		{
			System.out.println("Srach Up going up " + Target + " " + Head.getName() + " "+ " " + Head + " " + Head.getType());
			
			Temp = Temp.getParent();
			
			if(Temp.getType().equals(Target))
			{
				System.out.println("Found head " + Head.getName() + " " + Head.getType());
				return Temp;
			}
			
			
		}
		
		
		return Head;
	}
	
	public SymTabl getGlobal(AST_Node p_node)
	{
		SymTabl Temp = p_node.m_symtab;
		
		while(Temp.m_uppertable != null)
		{
			if(Temp.m_uppertable != null)
			{
				Temp = Temp.m_uppertable;
			}
		}
		
		return Temp;
	}
	
	public boolean ClassCheck(AST_Node p_node)
	{
		for(int i=0; i<p_node.m_symtab.m_uppertable.m_symlist.size(); i++)
		{
			if(p_node.m_symtabentry.m_type.equals(p_node.m_symtab.m_uppertable.m_symlist.get(i).m_name))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isClass(AST_Node p_node)
	{
		
		SymTabl Temp = getGlobal(p_node);
		
		String check = p_node.getValue().trim();
		
		for(int i=0; i<Temp.m_symlist.size(); i++)
		{
			if(check.equals(Temp.m_symlist.get(i).m_name))
			{
				return true;
			}
		}
		return false;
	}
	
	public int getClassSize(AST_Node p_node)
	{
		
		SymTabl Temp = getGlobal(p_node);
		
		String check = p_node.getValue().trim();
		
		for(int i=0; i<Temp.m_symlist.size(); i++)
		{
			if(check.equals(Temp.m_symlist.get(i).m_name))
			{
				return -Temp.m_symlist.get(i).m_subtable.m_size;
			}
		}
		return 0;
	}
	
	public int ClassSize(AST_Node p_node)
	{
		for(int i=0; i<p_node.m_symtab.m_uppertable.m_symlist.size(); i++)
		{
			if(p_node.m_symtabentry.m_type.equals(p_node.m_symtab.m_uppertable.m_symlist.get(i).m_name))
			{
				return -p_node.m_symtab.m_uppertable.m_symlist.get(i).m_subtable.m_size;
			}
		}
		
		return 0;
	}
	
	public int getDimSize(AST_Node Dim)
	{
		String numb = "";
		int number = 1;
		
		for(AST_Node num : Dim.getChildren())
		{
			numb = num.getValue().trim();
			
			if(!numb.equals(""))
			{
				if((int)numb.charAt(0) > 48 && (int) numb.charAt(0) < 58)
				{
					number *= Integer.parseInt(numb);
				}
			}
		}
		
		
		return number;
	}
	
	public int getDimSize2(AST_Node Dim)
	{
		
		int number = 1;
		
		Vector<Integer> st = Dim.m_symtab.m_symlist.get(0).m_dims;
		
		for(Integer num : st)
		{
			
			
			number *= num;
			
			
		}
		
		
		return number;
	}
	
	public int getInLSize(AST_Node InL)
	{
		String numb = "";
		int number = 0;
		int count = 0;
		
		for(AST_Node num : InL.getChildren())
		{
			numb = num.getValue().trim();
			
			//System.out.println("NUmb " + numb);
			
			if(!numb.equals(""))
			{
				if((int)numb.charAt(0) > 47 && (int) numb.charAt(0) < 58)
				{
					//System.out.println(numb);
					number += Integer.parseInt(numb);
				}
			}
		}
		
		
		return number;
	}
	
	
	public static void main(String args[])
	{
		AST_Node a = new AST_Node();
		AST_Node b = new AST_Node();
		AST_Node d = new AST_Node();
		AST_Node s = new AST_Node();
		AST_Node w = new AST_Node();
		AST_Node a1 = new AST_Node();
		AST_Node a2 = new AST_Node();
		AST_Node a3 = new AST_Node();
		AST_Node a4 = new AST_Node();
		AST_Node a5 = new AST_Node();
		
		ID i = new ID("hello");
		ID j = new ID("Bye");
		
		a = a.makeNode(1);
		a.setName("1");
		
		b = b.makeNode(0.0);
		b.setName("0.0");
		
		d = d.makeNode(i);
		d.setName("hello");
		
		s = s.makeNode(j);
		s.setName("Bye");
		
		w = w.makeNode(5);
		w.setName("5");
		
		a1 = a1.makeNode(10);
		a1.setName("10");
		
		a2 = a2.makeNode(20);
		a2.setName("20");
		
		a3 = a3.makeNode(30);
		a3.setName("20");
		
		a4 = a4.makeNode(40);
		a4.setName("20");
		
		a5 = a5.makeNode(50);
		a5.setName("20");
		
		a = a.adoptChildren(a1);
		
		a1 = a1.makeSibling(a2);
		
		a = a.makeSibling(a3);
		
		w = w.adoptChildren(a);
		
		a1 = a1.adoptChildren(a4);
		
		a4 = a4.makeSibling(a5);
		
		a3 = a3.makeSibling(s);
		
		a3 = a3.adoptChildren(d);
		
		w.printBottom();
		
		System.out.println("SYATEY");
		System.out.println("Search " + a.searchFunction(w, "5").getName());
		
		ArrayList<AST_Node> vec = new ArrayList<AST_Node>();
		
		vec = a.searchVector(w, "20", vec);
		
		for(int k=0; k<vec.size(); k++)
		{
			System.out.println(vec.get(k).getName());
		}
		
	}
}

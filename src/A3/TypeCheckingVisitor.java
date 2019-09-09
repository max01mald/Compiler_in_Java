package A3;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Vector;

import org.omg.CORBA.SystemException;

import AST_Tree.AST_Node;
import Element_Node.*;
import Visitor.Visitor;

/**
 * Visitor to compute the type of subexpressions and assignment statements. 
 * 
 * This applies only to nodes that are part of expressions and assignment statements i.e.
 * AddOpNode, MultOpNode, and AssignStatp_node. 
 * 
 */

public class TypeCheckingVisitor extends Visitor {

	public String m_outputfilename = new String();
	public String m_errors         = new String();
    
	public TypeCheckingVisitor() {
	}
	
	public TypeCheckingVisitor(String p_filename) {
		this.m_outputfilename = p_filename; 
	}

	public void visit(ProgNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
		if (!this.m_outputfilename.isEmpty()) {
			File file = new File(this.m_outputfilename);
			try (PrintWriter out = new PrintWriter(file)){ 
			    out.println(this.m_errors);
			    if(!this.m_errors.equals(""))
			    {
			    	 System.out.print("ERROROROROROOR");
			    	p_node.m_symtab.valid = false;
			    }
			    else
			    {
			    	p_node.m_symtab.valid = true;
			    }
			    
			   
			}
			catch(Exception e){
				e.printStackTrace();}
		}
	};
	
	public String NumberType(String Value)
	{
		boolean check = true;
		
		for(int i=0; i<Value.length(); i++)
		{
			if((int)Value.charAt(i) == 46)
			{
				return "float";
			}
		}
		
		return "int";
	}
	
	public boolean addCheck(AST_Node p_node)
	{
		boolean Check = true;
		
		String Test1 = "";
		String Test2 = "";
		int count = 0;
		
		System.out.println("Start");
		for(AST_Node child : p_node.getChildren())
		{
			while(child.getLC() != null)
			{
				child = child.getLC();
			}
			
			child.printBottom();
			
			if(!child.getType().equals("NULL")&&child.getRSib() != null)
			{
				if(child.getRSib().getType().equals("Aparam"))
				{
					if(count == 0)
					{
					
						Test1 = this.funcType(p_node, child.getValue().trim());
					}
					else
					{
						Test2 = this.funcType(p_node, child.getValue().trim());
					}
				}
				else if(child.NumTest(child.getValue().trim()))
				{
					if(count == 0)
					{
						Test1 = NumberType(child.getValue().trim());
					}
					else
					{
						Test2 = NumberType(child.getValue().trim());
					}
				}
				else
				{
					if(count == 0)
					{
						Test1 = p_node.m_symtab.lookupName(child.getValue().trim()).m_type;
						
					}
					else
					{
						Test2 = p_node.m_symtab.lookupName(child.getValue().trim()).m_type;
					}
					
				}
				
				
				count +=1;
			}
		}
		
		System.out.println(Test1 + " " + Test2);
		
		if(Test1 == null)
		{
			this.m_errors += "Addition Type Miss Match Error: Adding " + Test1 + " Location " + p_node.getLocation() + "\n";
			return false;
		}
		
		if(Test2 == null)
		{
			this.m_errors +="Addition Type Miss Match Error: Adding " + Test2 + " Location " + p_node.getLocation()+ "\n";
			return false;
		}
		
		if(!Test1.equals(Test2))
		{
			this.m_errors +="Addition Type Miss Match Error: Adding " + Test1 + " with " + Test2 + " Location " + p_node.getLocation()+ "\n";
			return false;
		}
		
		
		
		return Check;
	}
	
	public void visit(AddOpNode p_node){ 
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		
		if(!addCheck(p_node))
		{
			p_node.m_symtab.valid = false;
		}
		
		for (AST_Node child : p_node.getChildren() ){
			child.accept(this);
			child.printBottom();
		}
		
		
	}
	
	public void visit(MultOpNode p_node){ 
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		
		if(!addCheck(p_node))
		{
			System.out.println("Add");
			p_node.m_symtab.valid = false;
		}
		
		
		
	}
	
	public boolean eqqCheck(AST_Node p_node)
	{
		AST_Node Left = p_node.getChildren().get(0);
		AST_Node Right = p_node.getChildren().get(1);
		
		while(Left.getLC() != null)
		{
			Left = Left.getLC();
		}
		
		while(Right.getLC() != null)
		{
			Right = Right.getLC();
		}
		
		
		Left.printBottom();
		
		Right.printBottom();
		
		String name1 = Left.getValue().trim();
		String name2 = Right.getValue().trim();
		
		String type1 = p_node.m_symtab.lookupName(name1).m_type;
		
		String type2 = p_node.getGlobal(p_node).lookupName(name2).m_type;
		
		if(type2 == null)
		{
			type2 = p_node.m_symtab.lookupName(name2).m_type;
		}
		
		if(type1 == null)
		{
			type1 = p_node.getGlobal(p_node).lookupName(name1).m_type;
		}
		
		System.out.println(type1);
		System.out.println(type2);
		
		if(type1 == null)
		{
			this.m_errors +="Assignment Error Variable " + type1 + " has not been declared " + " Location " + p_node.getLocation()+ "\n";
			return false;
		}
		
		if(type2 == null)
		{
			this.m_errors +="Assignment Error Variable " + type2 + " has not been declared " + " Location " + p_node.getLocation()+ "\n";
			return false;
		}
		
		if(!type1.equals(type2))
		{
			this.m_errors +="Assignment Error Variable " + type1 + "Value " + type2 + " Location " + p_node.getLocation()+ "\n";
			return false;
		}
		
		
		return true;
	}
	
	public void visit(AssignStatNode p_node){ 
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		
		if(!eqqCheck(p_node))
		{
			p_node.m_symtab.valid = false;
		}
		
		for (AST_Node child : p_node.getChildren() ){
			child.accept(this);
			
			child.printBottom();
			
		}
		
		
		
	}
	
	public boolean funcTestGl(AST_Node p_node)
	{
		boolean check = true;
		SymTabl Global = p_node.getGlobal(p_node);
		
		System.out.println(p_node.getLC().getValue());
		System.out.println(Global);
		
		if(Global.lookupName(p_node.getLC().getValue().trim()).m_name == null)
		{
			this.m_errors +="Undeclared FCall Error "  + p_node.getLC().getValue().trim() + " not Declared Location: " + p_node.getLC().getLocation() + "\n";
			check = false;
			return check;
		}
		
		SymTabEntry func = Global.lookupName(p_node.getLC().getValue().trim());
		
		if(func.m_subtable.m_symlist.size() != p_node.getChildren().size()-1)
		{
			int provided = p_node.getChildren().size()-1;
			this.m_errors +="Parameter Miss Match FCall Error "  + p_node.getLC().getValue().trim() + ": Declared: " + func.m_subtable.m_symlist.size() + " Provided: " + provided + " Location: " + p_node.getLC().getLocation()+ "\n";
			check = false;
		}
		
		return check;
	}
	
	public boolean funcTest(AST_Node p_node)
	{
		boolean check = true;
		SymTabl Global = p_node.getGlobal(p_node);
		
		AST_Node Param = p_node.getChildType("Aparam");
		Param.printBottom();
		
		SymTabEntry func = Global.lookupName(p_node.getLC().getValue().trim());
		
		System.out.println(func);
		
		ArrayList<SymTabEntry> SL = func.m_subtable.m_symlist;
		
		int i = 0;
		for(AST_Node child : Param.getChildren())
		{
			func = SL.get(i);
			String Type = func.m_type; 
			
			child.printBottom();
			
			if(Type == null)
			{
				return false;
			}
			
			
			if(!child.getType().equals("NULL"))
			{
				while(child.getLC() != null)
				{
					child = child.getLC();
					
				}
				
				System.out.println(p_node.m_symtab);
				SymTabEntry Check = p_node.m_symtab.lookupName(child.getValue().trim());
				
				String testType = Check.m_type;
				
				System.out.println("Type Checking");
				System.out.println(testType);
				System.out.println(Type);
				if (!testType.equals(Type))
				{
					this.m_errors +="Wrong Type used in FCall Parameter Error "  + Check.m_name + ": Provided: " + testType + " Expected: " + Type + " Location: " + child.getLocation()+ "\n";
					check = false;
				}
				i +=1;
			}
			child.printBottom();
		}
		return check;
	}
	
	public String funcType(AST_Node p_node, String name)
	{
		return p_node.getGlobal(p_node).lookupName(name).m_type;
	}
	
	public void visit(FuncCallNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		if(!this.funcTestGl(p_node))
		{
			p_node.m_symtab.valid = false;
		}
		else
		{
			if(!this.funcTest(p_node))
			{
				p_node.m_symtab.valid = false;
			}
			else
			{
				p_node.m_declty = this.funcType(p_node, p_node.getValue().trim());
			}
		}
		
		
		
		for (AST_Node child : p_node.getChildren() ){
			child.accept(this);
			
			child.printBottom();			
			
		}
		
		p_node.Type = p_node.getChildren().get(0).Type;
		p_node.setValue(p_node.getChildren().get(0).getValue().trim());
	}; 
	
	// Below are the visit methods for node types for which this visitor does not apply
	// They still have to propagate acceptance of the visitor to their children.
	
	public boolean ClssCheck(AST_Node p_node)
	{
		Vector<AST_Node> Inher = new Vector<AST_Node>();
		Vector<AST_Node> DM = new Vector<AST_Node>();
		
		boolean check = true;
		
		for(AST_Node child : p_node.getChildren())
		{
			child.printBottom();
			
			if(child.getType().equals("NULL"))
			{
				return true;
			}
			
			AST_Node Id = child.getChildren().get(0);
			AST_Node In = child.getChildren().get(1);
			AST_Node dm = child.getChildren().get(2);
			
			if(!child.equals("NULL"))
			{
				Inher.add(In);
				DM.add(dm);
			}
		}
		int x = 0;
		
		
		System.out.println(Inher.size());
		
		for(int i=0; i<Inher.size(); i++)
		{
			for(int j=1; j<Inher.size(); j++)
			{
				for(AST_Node in : Inher.get(j).getChildren())
				{
					String Name = Inher.get(i).getLSib().getValue().trim();
					
					String Check = in.getValue().trim();
					
					if(Name.equals(Check))
					{
						
						String Name2 = Inher.get(j).getLSib().getValue().trim();
						
						for(AST_Node in2 : Inher.get(i).getChildren())
						{
							String Check2 = in2.getValue().trim();
							
							if(Name2.equals(Check2))
							{
								this.m_errors +="Circular Dependency With the Classes " + Name + " " + Name2 + " Location: " +in2.getLocation()+ "\n";
								check = false;
							}
						}
					}
					
					System.out.println(Check);
					
					
					
				}
			}
		}
		
		System.out.println(DM.size());
		
		
		
		for(int i=0; i<DM.size(); i++)
		{
			for(int j=1; j<DM.size(); j++)
			{
				for(AST_Node in : DM.get(j).getChildren())
				{
					//in.printBottom();
					if(!in.getType().equals("NULL"))
					{
						String Name = DM.get(i).getLC().getChildren().get(1).getValue().trim();
						
						String Check = in.getChildren().get(1).getValue().trim();
						
						System.out.println(Name + " " + Check);
						System.out.println(i + " " + j);
						if(Name.equals(Check))
						{
							
							
							
							String Name2 = DM.get(j).getLC().getChildren().get(1).getValue().trim();
							
							for(AST_Node in2 : DM.get(i).getChildren())
							{
								if(!in2.getType().equals("NULL"))
								{
									
									String Check2 = in2.getChildren().get(1).getValue().trim();
									
									if(Name2.equals(Check2))
									{
										if(i != j)
										{
											this.m_errors +="Circular Dependency With the Class Datamembers " + Name + " " + Name2 + " Location: " +in2.getLocation()+ "\n";
											check = false;
										}
									}
								}
							}
						}
						
						System.out.println(Check);
					}
					
					
				}
			}
		}
		
		
		return check;
	}
	
	
	
	public void visit(ClassListNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		
		p_node.printBottom();
		
		if(!ClssCheck(p_node))
		{
			System.out.println("CLAASSSS");
			p_node.m_symtab.valid = false;
		}
		
		
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
	};

	public boolean memFCheck(AST_Node p_node)
	{
		p_node.printBottom();
		boolean check = true;
		
		System.out.println(p_node.m_symtab);
		
		ArrayList<SymTabEntry> vec = p_node.m_symtab.m_symlist;
		
		SymTabl global = p_node.getGlobal(p_node);
		
		for(int i=0; i<vec.size(); i++)
		{
			if(vec.get(i).m_kind.equals("Function"))
			{
				SymTabEntry temp = global.lookupName(vec.get(i).m_name);
				
				if(temp.m_name == null)
				{
					this.m_errors +="Non implemented Member Function Error " + vec.get(i).m_name + " Location " + p_node.Location+ "\n";
					check = false;
				}
			}
		}
		
		System.out.println();
		
		return check;
	}
	
	public void visit(ClassNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		
		if(this.memFCheck(p_node))
		{
			p_node.m_symtab.valid = false;
		}
		
		
		
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
		
	};
	
	

	public void visit(DimListNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
	};

	public void visit(FuncDefListNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
	};

	public void visit(FuncDefNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
		p_node.setValue(p_node.getChildren().get(1).getValue().trim());
		p_node.setType(p_node.getChildren().get(1).getType());
	};

	public void visit(IdNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
	};

	public void visit(AST_Node p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
	};

	public void visit(NumNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
		
		
	};

	public void visit(ProgramBlockNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
	};

	public void visit(PutStatNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
	};

	public void visit(StatBlockNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
	};

	public void visit(TypeNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
		p_node.Type = p_node.getValue().trim();
	};

	public void visit(VarDeclNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		
		p_node.printBottom();
		AST_Node Var = p_node.getChildren().get(0);
		
		if(!TypeCheck(Var.getValue().trim()))
		{
			if(!p_node.isClass(Var))
			{
				this.m_errors +="Undeclared Class Error: " + p_node.getChildren().get(0).getValue().trim() + " Location: " + p_node.getLocation()+ "\n";
				p_node.m_symtab.valid = false;
			}
		}
		
		
		
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
	 }; 

	 public boolean TypeCheck(String Value)
	 {
		 
		 if((int)Value.charAt(0) == 73)
		 {
			 return true;
		 }
		 else if((int)Value.charAt(0) == 70)
		 {
			 return true;
		 }
		 
		 return false;
	 }
	 
	public void visit(ParamListNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
	 }

	public void visit(DimNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
	}
	
	public boolean arrCheck(AST_Node p_node)
	{
		int i=0;
		for(AST_Node child : p_node.getChildren())
		{
			while(child.getLC() != null)
			{
				child = child.getLC();
			}
			
			child.printBottom();
			
			if(p_node.NumTest(child.getValue().trim()))
			{
				if(NumberType(child.getValue().trim()).equals("float"))
				{
					this.m_errors +="Array type Error float found Location " + p_node.Location+ "\n";
				}
				else if(NumberType(child.getValue().trim()).equals("int"))
				{
					int temp = Integer.parseInt(child.getValue().trim());
					
					//System.out.println(p_node.m_symtab);
					
					p_node.getLSib().printBottom();
					
					int dim = p_node.m_symtab.lookupName(p_node.getLSib().getValue().trim()).m_dims.get(i);
					
					System.out.println(dim);
					
					if(dim <= temp)
					{
						this.m_errors +="Array Index Error Max Size: " + dim + " Given: " + temp + " Location: " + child.getLocation()+ "\n";
					}
					
				}
			}
			else if(child.getType().equals("ID")||child.getName().equals("ID"))
			{
				System.out.println(child.getValue().trim());
				
				String type = p_node.m_symtab.lookupName(child.getValue().trim()).m_type;
				
				if(type == null)
				{
					this.m_errors +="Undeclared Array index variable " + child.getValue() + " Location: " + p_node.Location+ "\n";
					return false;
				}
				
				if(NumberType(child.getValue().trim()) == "float")
				{
					this.m_errors +="Array type Error float found Location " + p_node.Location+ "\n";
				}
				else if(!NumberType(child.getValue().trim()).equals("int"))
				{
					this.m_errors +="Array type Error Class found Location " + p_node.Location+ "\n";
				}
				
				
				System.out.println(p_node.m_symtab);
				
			}
			
			i += 1;
		}
		
		
		return true;
	}
	
	public void visit(IndexListNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		
		p_node.printBottom();
		
		if(!p_node.getChildren().get(0).getType().equals("NULL"))
		{
			arrCheck(p_node);
		}
		
		
		//System.exit(0);
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
	}

	public boolean returnTest(AST_Node p_node)
	{
		String name = p_node.m_symtab.m_name;
		SymTabEntry se = p_node.m_symtab.m_uppertable.lookupName(name);
		
		AST_Node child = p_node;
		
		while(child.getLC() != null)
		{
			child = child.getLC();
		}
		
		String type = p_node.m_symtab.lookupName(child.getValue().trim()).m_type;
		
		if(!se.m_type.equals(type))
		{
			this.m_errors +="Return Type Error: Expected " + type + " Provided: " + se.m_type + " Location " + p_node.Location+ "\n";
			return false;
		}
		
		return true;
	}
	
	public void visit(ReturnStatNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		
		p_node.printBottom();
		
		if(!this.returnTest(p_node))
		{
			p_node.m_symtab.valid = false;
		}
		
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
	}; 
	
	public void visit(IfNode p_node)
	{
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		 for (AST_Node child : p_node.getChildren()) {
			 child.m_symtab = p_node.m_symtab;
			 child.accept(this);
		 }
	}
	
	public void visit(ForNode p_node)
	{
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		 for (AST_Node child : p_node.getChildren()) {
			 child.m_symtab = p_node.m_symtab;
			 child.accept(this);
		 }
	}
	
	public void visit(RelNode p_node)
	{
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		 for (AST_Node child : p_node.getChildren()) {
			 child.m_symtab = p_node.m_symtab;
			 child.accept(this);
		 }
	}
	
	public void visit(GetStatNode p_node)
	{
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		 for (AST_Node child : p_node.getChildren()) {
			 child.m_symtab = p_node.m_symtab;
			 child.accept(this);
		 }
	}
}

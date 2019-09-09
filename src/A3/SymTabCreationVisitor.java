package A3;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import AST_Tree.AST_Node;
import Element_Node.*;
import Visitor.Visitor;

/**
 * Visitor to create symbol tables and their entries.  
 * 
 * This concerns only nodes that either:  
 * 
 * (1) represent identifier declarations/definitions, in which case they need to assemble 
 * a symbol table record to be inserted in a symbol table. These are:  VarDeclNode, ClassNode,  
 * and FuncDefp_node. 
 * 
 * (2) represent a scope, in which case they need to create a new symbol table, and then 
 * insert the symbol table entries they get from their children. These are:  ProgNode, ClassNode, 
 * FuncDefNode, and StatBlockp_node.   
 */

public class SymTabCreationVisitor extends Visitor {
	
    public Integer m_tempVarNum     = 0;
	public String  m_outputfilename = new String(); 
    public String m_errorOut = "";
	public String m_error = "";
	
	public SymTabCreationVisitor() {
	}
	
	public SymTabCreationVisitor(String p_filename) {
		this.m_outputfilename = p_filename; 
	}
    
    public String getNewTempVarName(){
    	m_tempVarNum++;
    	return "t" + m_tempVarNum.toString();  
    }
    
    public boolean doubleName(AST_Node p_node)
	{
		boolean check = true;
		boolean check2 = true;
		int count = 0;
		String Hold = "";
		String Test = "";
		
		if(p_node.getType().equals("NULL"))
		{
			return true;
		}
		
		p_node.getParent().printBottom();
		
		AST_Node child = p_node;
		
		while(child.getLC() != null)
		{
			child = child.getLC();
		}
		
		System.out.println(p_node.getParent().m_symtab);
		
		if(p_node.m_symtab.m_symlist == null)
		{
			return true;
		}
		
		for(int i=0; i< p_node.m_symtab.m_symlist.size(); i++)
		{
			for(int j=1; j< p_node.m_symtab.m_symlist.size(); j++)
			{
				if(check2)
				{
				Hold = p_node.m_symtab.m_symlist.get(i).m_name;
				Test = p_node.m_symtab.m_symlist.get(j).m_name;
				
				System.out.println(p_node.m_symtab.m_symlist.get(i));
				System.out.println(p_node.m_symtab.m_symlist.get(j));
				
				System.out.println(Hold + " " + Test);
				
				
					System.out.println(i + " " + j);
				
					if(Hold.equals(Test))
					{
						if(i != j)
						{
							check2 = false;
							this.m_error += "Double Decleration Error at Duplicate is " + Test + " " + child.getLocation()+ "\n";
						}
						
					}
				}
				
			}
			if(!check2)
			{
				check = false;
			}
		}
		
		System.out.println(check);
		
		
		
		return check;
	}
    
    public void setErrorFile(String file)
    {
    	this.m_errorOut = file;
    }
    
	public void visit(ProgNode p_node){
		p_node.m_symtab = new SymTabl(0,"global", null);
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() ) {
			//make all children use this scopes' symbol table
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
		}
		
		if (!this.m_errorOut.isEmpty()) {
			File file = new File(this.m_errorOut);
			try (PrintWriter out = new PrintWriter(file)){ 
			    out.println(this.m_error);
			   
				if(m_error.equals(""))
				{
					p_node.m_symtab.valid = true;
					
					
				}
				else
				{
					System.out.print(this.m_error);
					System.out.print(this.m_errorOut);
					
					p_node.m_symtab.valid = false;
				}
			   
			}
			catch(Exception e){
				e.printStackTrace();}
	    }
		
	};

	public void visit(StatBlockNode p_node){
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		
		//p_node.printBottom();
		
		System.out.println(p_node.m_symtab);
		System.out.println(p_node.getChildren().get(0).getClass());
		
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		for (AST_Node child : p_node.getChildren() ) {
			child.m_symtab = p_node.m_symtab;
			if(p_node.m_symtab.m_rel)
			{
				
			}
			child.accept(this);
		}
	};
	
	public void visit(ProgramBlockNode p_node){
        SymTabl localtable = new SymTabl(1,"program", p_node.m_symtab);
        p_node.m_symtabentry = new FuncEntry("null","program", new Vector<VarEntry>(), localtable);
		p_node.m_symtab.addEntry(p_node.m_symtabentry);
		p_node.m_symtab = localtable;
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() ) {
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
		}
		
		this.doubleName(p_node);
		
	};

	public void visit(ClassNode p_node){
		String classname = p_node.getChildren().get(0).getValue().trim();
		SymTabl localtable = new SymTabl(1,classname, p_node.m_symtab);
		p_node.m_symtabentry = new ClassEntry(classname, localtable);
		System.out.println(p_node.m_symtab);
		
		
		p_node.m_symtab.addEntry(p_node.m_symtabentry);
		p_node.m_symtab = localtable;
		
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() ) {
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
			this.doubleName(child);
			child.printBottom();

		}
		if(!this.doubleName(p_node))
		{
			System.out.println(p_node.m_symtab);
			p_node.m_symtab.valid = false;
		}
		
		//System.exit(0);
		
	};
	
	public void visit(FuncDefNode p_node){
		String ftype = p_node.getChildren().get(0).getValue().trim();
		String fname = p_node.getChildren().get(1).getValue().trim();
		
		System.out.println(ftype + " " + fname);
		
		this.doubleName(p_node);
		
		SymTabl localtable = new SymTabl(1,fname, p_node.m_symtab);
		Vector<VarEntry> paramlist = new Vector<VarEntry>();
		for (AST_Node param : p_node.getChildren().get(2).getChildren()){
			// parameter dimension list
			Vector<Integer> dimlist = new Vector<Integer>();
			
			
			if(!param.getType().equals("NULL"))
			{
				String type = param.getChildren().get(0).getValue().trim();
				String id = param.getChildren().get(1).getValue().trim();
				
				System.out.println(type + " " + id);
				
				for (AST_Node dim : param.getChildren().get(2).getChildren()){
					// parameter dimension
					if(!dim.getType().equals("NULL"))
					{
						Integer dimval = Integer.parseInt(dim.getValue().trim()); 
						dimlist.add(dimval); 
					}
				}
				p_node.m_symtabentry = new VarEntry("Parameter", type, id, dimlist);
				paramlist.add((VarEntry) p_node.m_symtabentry);
			}
			
		}
		
		p_node.m_symtabentry = new FuncEntry(ftype, fname, paramlist, localtable);
		
		p_node.printBottom();
		p_node.m_symtab.addEntry(p_node.m_symtabentry);
		p_node.m_symtab = localtable;
		
		
		
		
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() ) {
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
		}
	};

	public void visit(VarDeclNode p_node){
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() ) {
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
		}
		// Then, do the processing of this nodes' visitor
		// aggregate information from the subtree
		// get the type from the first child node and aggregate here 
		
		//p_node.printBottom();
		
		String vartype = p_node.getChildren().get(0).getValue().trim();
		// get the id from the second child node and aggregate here
		String varid = p_node.getChildren().get(1).getValue().trim();
		// loop over the list of dimension nodes and aggregate here 
		Vector<Integer> dimlist = new Vector<Integer>();
		
		//p_node.printBottom();
		
		for (AST_Node dim : p_node.getChildren().get(2).getChildren()){
			// parameter dimension
			
			if(!dim.getValue().equals(""))
			{
				Integer dimval = Integer.parseInt(dim.getValue().trim());
				dimlist.add(dimval);
			}
			 
		}
		// create the symbol table entry for this variable
		// it will be picked-up by another node above later
		p_node.m_symtabentry = new VarEntry("Variable", vartype, varid, dimlist);
		p_node.m_symtab.addEntry(p_node.m_symtabentry);
		
	}
	
	public void visit(AddOpNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren()) {
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
		}		
		String tempvarname = this.getNewTempVarName();
		p_node.m_moonVarName = tempvarname;
		p_node.m_symtabentry = new VarEntry("tempvar", p_node.getType(), p_node.m_moonVarName, p_node.m_symtab.lookupName(p_node.getChildren().get(0).m_moonVarName).m_dims);
		p_node.m_symtab.addEntry(p_node.m_symtabentry);
	};

	public void visit(MultOpNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren()) {
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
		}		
		String tempvarname = this.getNewTempVarName();
		p_node.m_moonVarName = tempvarname;
		p_node.m_symtabentry = new VarEntry("tempvar", p_node.getType(), p_node.m_moonVarName, p_node.m_symtab.lookupName(p_node.getChildren().get(0).m_moonVarName).m_dims);
		p_node.m_symtab.addEntry(p_node.m_symtabentry);
	};

	public void visit(NumNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren()) {
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
		}
		
		if(p_node.getType().equals("VarEl"))
		{
			String tempvarname = p_node.getLC().getValue().trim();
			p_node.m_moonVarName = tempvarname;
			String vartype = p_node.getType();
			p_node.m_symtabentry = new VarEntry("litval", vartype, p_node.m_moonVarName, new Vector<Integer>());
			
		}
		else
		{
			p_node.printBottom();
			p_node.getParent().printBottom();
			
			System.out.println(p_node.getParent().m_symtab);
			
			String tempvarname = this.getNewTempVarName();
			p_node.m_moonVarName = tempvarname;
			String vartype = p_node.getType();
			p_node.m_symtabentry = new VarEntry("litval", vartype, p_node.m_moonVarName, new Vector<Integer>());
			p_node.m_symtab.addEntry(p_node.m_symtabentry);
		}
		p_node.printBottom();
		p_node.getParent().printBottom();
		
		
		
	};
	
	// Below are the visit methods for node types for which this visitor does
	// not apply. They still have to propagate acceptance of the visitor to
	// their children.

	public void visit(AssignStatNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren()) {
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
		}
	};

	public void visit(ClassListNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren()) {
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
			
			child.printBottom();
			
			System.out.print(child.m_symtab);
		}
		if(this.doubleName(p_node))
		{
			p_node.m_symtab.valid = false;
		}
		
	};

	public void visit(DimListNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren()) {
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
		}
	};

	public boolean FdefCheck(AST_Node p_node)
	{
		
		System.out.println(p_node.m_symtab);
		
		p_node.printBottom();
		
		//this.doubleName(p_node);
		
		
		
		
		
		return true;
	}
	
	public void visit(FuncDefListNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren()) {
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
			
			System.out.println("FUNNNNNCCCCC");
			
			child.printBottom();
			
			//System.out.print(child.m_symtab);
			this.doubleName(child);
		
		}
		
		
	};
	
	
	public void visit(IndexListNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren()) {
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
		}
	};

	public void visit(IdNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren()) {
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
		}
		p_node.m_moonVarName = p_node.getValue().trim();
	};

	public void visit(AST_Node p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren()) {
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
		}
		
		if(p_node.m_symtab == null)
		{
			p_node.printBottom();
			
			//System.exit(0);
		}
		
	};

	public void visit(PutStatNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren()) {
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
		}
	};

	public void visit(TypeNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren()) {
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
		}
	 };
	 public void visit(ParamListNode p_node) {
			// propagate accepting the same visitor to all the children
			// this effectively achieves Depth-First AST Traversal
		 for (AST_Node child : p_node.getChildren()) {
			 child.m_symtab = p_node.m_symtab;
			 child.accept(this);
			 
		 }
	 }

	public void visit(DimNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		 for (AST_Node child : p_node.getChildren()) {
			 child.m_symtab = p_node.m_symtab;
			 child.accept(this);
		 }
	}; 
	
	public boolean funcTest(AST_Node p_node)
	{
		boolean check = true;
		
		p_node.printBottom();
		
		for(AST_Node child : p_node.getChildren())
		{
			System.out.println("PARAM FCALL");
			//child.printBottom();
			
			while(child.getLC() != null)
			{
				child = child.getLC();
				
			}
			
			if(!child.getType().equals("NULL"))
			{
				SymTabEntry table = p_node.m_symtab.lookupName(child.getValue().trim());
				
				System.out.println(table.m_name);
				
				if (table.m_name == null)
				{
					m_error += "Missing Decleration for Variable used in FunctionCall Error " + p_node.getLocation()+ "\n";
					check = false;
				}
				
			}
			child.printBottom();
		}
		
		
		
		return check;
	}
	
	
	public void visit(FuncCallNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() ){
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
			
			System.out.println("FCALL");
			child.printBottom();
			//System.out.println(child.m_symtab);
			this.funcTest(child);
		}
		
		String tempvarname = this.getNewTempVarName();
		p_node.m_moonVarName = tempvarname;
		String vartype = p_node.getType();
		p_node.m_symtabentry = new VarEntry("retval", vartype, p_node.m_moonVarName, new Vector<Integer>());
		p_node.m_symtab.addEntry(p_node.m_symtabentry);
	}; 
	
	public void visit(ReturnStatNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() ){
			child.m_symtab = p_node.m_symtab;
			child.accept(this);}
	};
	
	public void visit(IfNode p_node)
	{
		p_node.m_symtab.m_if += 1;
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		 for (AST_Node child : p_node.getChildren()) {
			 child.m_symtab = p_node.m_symtab;
			 child.accept(this);
		 }
	}
	
	public void visit(ForNode p_node)
	{
		 p_node.m_symtab.m_for += 1;
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		 for (AST_Node child : p_node.getChildren()) {
			 child.m_symtab = p_node.m_symtab;
			 child.accept(this);
		 }
		
	}
	
	public void visit(RelNode p_node)
	{
		p_node.m_symtab.m_rel = true;
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

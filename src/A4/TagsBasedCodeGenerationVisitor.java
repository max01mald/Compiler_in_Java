package A4;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;

import A3.SymTabEntry;
import A3.SymTabl;
import A3.VarEntry;
import AST_Tree.AST_Node;
import Element_Node.*;
import Visitor.Visitor;

/**
 * Visitor to generate moon code for simple expressions and assignment and put 
 * statements, and function calls. Uses a label-based model. Note that the 
 * label-based model bring limitations and is inefficient. 
 *
 */

public class TagsBasedCodeGenerationVisitor extends Visitor {
	
    public Stack<String> m_registerPool   = new Stack<String>();
    public Integer       m_tempVarNum     = 0;
    public String        m_moonExecCode   = new String();               // moon code instructions part
    public String        m_moonDataCode   = new String();               // moon code data part
    public String        m_mooncodeindent = new String("           ");
    public String        m_outputfilename = new String(); 
    
    public TagsBasedCodeGenerationVisitor() {
       	// create a pool of registers as a stack of Strings
    	// assuming only r1, ..., r12 are available
    	for (Integer i = 12; i>=1; i--)
    		m_registerPool.push("r" + i.toString());
    }
    
    public TagsBasedCodeGenerationVisitor(String p_filename) {
    	this.m_outputfilename = p_filename; 
       	// create a pool of registers as a stack of Strings
    	// assuming only r1, ..., r12 are available
    	for (Integer i = 12; i>=1; i--)
    		m_registerPool.push("r" + i.toString());
    }

	public void visit(ProgNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		
		if(p_node.m_symtab.m_relgl)
		{
			m_moonDataCode += m_mooncodeindent + "% space for " + p_node.getChildren().get(0).m_moonVarName + " + " + p_node.getChildren().get(2).m_moonVarName + "\n";
			m_moonDataCode += String.format("%-10s", "rel") + " res 4\n";
			m_moonDataCode += String.format("%-10s", "inl") + " res 4\n";
		}
		
		for (AST_Node child : p_node.getChildren()){
			child.accept(this);	
			if(p_node.m_symtab.m_for > p_node.m_symtab.m_forgl)
			{
				BroadFor(child.getRSib());
			}
			if(p_node.m_symtab.m_if > p_node.m_symtab.m_ifgl)
			{
				BroadIf(child.getRSib());
			}
		}
		
		
		
		if (!this.m_outputfilename.isEmpty()) {
			File file = new File(this.m_outputfilename);
			try (PrintWriter out = new PrintWriter(file)) {
			    out.println(this.m_moonExecCode);
			    out.println(this.m_moonDataCode);}		
			catch(Exception e){
				e.printStackTrace();}
		}
	
	};
	
	public void visit(VarDeclNode p_node){
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
		// Then, do the processing of this nodes' visitor
		
		AST_Node Dim = p_node.getChildren().get(2);
		
		int dimSize = p_node.getDimSize(Dim);
		
		int size = 0;
		
		if (p_node.getChildren().get(0).getValue().trim() == "INTDC"){
			m_moonDataCode += m_mooncodeindent + "% space for variable " + p_node.getChildren().get(1).getValue().trim() + "\n";
			size = 4;
			size = (dimSize * size);
			
			m_moonDataCode += String.format("%-10s" ,p_node.getChildren().get(1).getValue().trim()) + " res " + size + "\n";
		}
		else if(p_node.getChildren().get(0).getValue().trim() == "FLOATDC"){
			size = 8;
			size = (dimSize * size);
			
			m_moonDataCode += m_mooncodeindent + "% space for variable " + p_node.getChildren().get(1).getValue().trim() + "\n";
			m_moonDataCode += String.format("%-10s" ,p_node.getChildren().get(1).getValue().trim()) + " res " + size + "\n";
		}
		else if(p_node.isClass(p_node.getChildren().get(0)))
		{
			size = p_node.getClassSize(p_node.getChildren().get(0));
			size = (dimSize * size);
			
			m_moonDataCode += m_mooncodeindent + "% space for variable " + p_node.getChildren().get(1).getValue().trim() + "\n";
			m_moonDataCode += String.format("%-10s" ,p_node.getChildren().get(1).getValue().trim()) + " res " + size + "\n";
		}
	}

	public void visit(NumNode p_node){
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
		// Then, do the processing of this nodes' visitor
		// create a local variable and allocate a register to this subcomputation 
		
		AST_Node Var = null;
		
		
		String localRegister2 = this.m_registerPool.pop();
		String localRegister = this.m_registerPool.pop();
		String secondRegister = this.m_registerPool.pop();
		
		p_node.printBottom();
		
		p_node.printBottom();
		p_node.printSiblings();
		
		if(p_node.NumTest(p_node.getValue().trim()))
		{
			if(!p_node.getParent().getType().equals("InL"))
			{
				m_moonDataCode += m_mooncodeindent + "% space for constant " + p_node.getValue().trim() + "\n";
				m_moonDataCode += String.format("%-10s",p_node.m_moonVarName) + " res 4\n";
				
				m_moonExecCode += m_mooncodeindent + "% processing: " + p_node.m_moonVarName  + " := " + p_node.getValue().trim() + "\n";
				m_moonExecCode += m_mooncodeindent + "addi " + localRegister + ",r0," + p_node.getValue().trim() + "\n"; 
				m_moonExecCode += m_mooncodeindent + "sw " + p_node.m_moonVarName + "(r0)," + localRegister + "\n";
			}
			else
			{
				m_moonDataCode += m_mooncodeindent + "% space for constant " + p_node.getValue().trim() + "\n";
				m_moonDataCode += String.format("%-10s",p_node.m_moonVarName) + " res 4\n";
				
				m_moonExecCode += m_mooncodeindent + "% processing: " + p_node.m_moonVarName  + " := " + p_node.getValue().trim() + "\n";
				m_moonExecCode += m_mooncodeindent + "addi " + localRegister + ",r0," + p_node.getValue().trim() + "\n"; 
				m_moonExecCode += m_mooncodeindent + "sw " + p_node.m_moonVarName + "(r0)," + localRegister + "\n";
			}
		}
		else if(p_node.getRSib().getType().equals("Aparam"))
		{
			m_moonDataCode += m_mooncodeindent + "% space for constant " + p_node.getValue().trim() + "\n";
			m_moonDataCode += String.format("%-10s",p_node.m_moonVarName) + " res 4\n";
			
			System.out.println(p_node.m_moonVarName);
			
			
			
		}
		else if(p_node.getLC() == null)
		{
			m_moonDataCode += m_mooncodeindent + "% space for constant " + p_node.getValue().trim() + "\n";
			m_moonDataCode += String.format("%-10s",p_node.m_moonVarName) + " res 4\n";
			
			m_moonExecCode += m_mooncodeindent + "% processing else: " + p_node.m_moonVarName  + " := " + p_node.getValue().trim() + "\n";
			m_moonExecCode += m_mooncodeindent + "lw "+ localRegister + "," +p_node.getValue().trim() + "(r0) \n";
			m_moonExecCode += m_mooncodeindent + "add " + localRegister2 + ",r0," + localRegister + "\n"; 
			m_moonExecCode += m_mooncodeindent + "sw " + p_node.m_moonVarName + "(r0)," + localRegister2 + "\n";
		}
		else if (p_node.getChildren().get(0) != null)
		{
			Var = p_node.getChildren().get(0);
			
			if(!Var.getRSib().getLC().getType().equals("NULL"))
			{
				Var.getRSib().printBottom();
				Var.printBottom();
				
				//System.exit(0);
				
				m_moonExecCode += m_mooncodeindent + "% processing else: " + p_node.m_moonVarName  + " := " + Var.getValue().trim() + "\n";
				m_moonExecCode += m_mooncodeindent + "lw "+ localRegister + "," +Var.getValue().trim() + "(r0) \n";
				m_moonExecCode += m_mooncodeindent + "add " + localRegister2 + ",r0," + localRegister + "\n"; 
				m_moonExecCode += m_mooncodeindent + "sw " + p_node.m_moonVarName + "(r0)," + localRegister2 + "\n";
			}
			
		}
		
		// deallocate the register for the current node
		this.m_registerPool.push(localRegister);
		this.m_registerPool.push(localRegister2);
		this.m_registerPool.push(secondRegister);
	}
	
	public void BroadFor(AST_Node p_node)
	{
		for(AST_Node child : p_node.getChildren())
		{
			if(child.getClass().getName().equals("Element_Node.ForNode"))
			{
				child.m_symtab.m_for +=1;
			}
			
			BroadFor(child);
		}
	}
	
	public void BroadIf(AST_Node p_node)
	{
		for(AST_Node child : p_node.getChildren())
		{
			if(child.getClass().getName().equals("Element_Node.IfNode"))
			{
				child.m_symtab.m_if +=1;
			}
			
			BroadFor(child);
		}
	}
	
	public void visit(AddOpNode p_node){
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
		// Then, do the processing of this nodes' visitor
		// create a local variable and allocate a register to this subcomputation 
		String localRegister      = this.m_registerPool.pop();
		String leftChildRegister  = this.m_registerPool.pop();
		String rightChildRegister = this.m_registerPool.pop();
		String secondRegister = this.m_registerPool.pop();
		// generate code
		
		System.out.println(localRegister);
		System.out.println(leftChildRegister);
		System.out.println(rightChildRegister);
		
		AST_Node Var = null;
		AST_Node Var2 = null;
		AST_Node Inl2 = null;
		AST_Node Inl = null;
		
		if( p_node.getChildren().get(0).getLC() != null)
		{
			if(!p_node.MultTest(p_node.getChildren().get(0).getValue()))
			{
				Var = p_node.getChildren().get(0).getChildren().get(0);
				
				if(Var.getLC() != null)
				{
					if(Var.getChildren().get(1) != null)
					{
						Inl = Var.getChildren().get(1);
					}
				}
			}
			else
			{
				Var = p_node.getChildren().get(0);
			}
			
			if(p_node.getChildren().get(1).getLC() != null)
			{
				Var2 = p_node.getChildren().get(1).getChildren().get(0);
				if(Var2.getChildren().get(1) != null)
				{
					Inl2 = Var2.getChildren().get(1);
				}
			}
		}
		else
		{
			Var = p_node.getChildren().get(0);
		}
		
		
		String Op = "";
		
		Op = addOP(p_node.getValue().trim());
		
		
		
		if(Inl != null)
		{
			if(!Inl.getLC().getType().equals("NULL"))
			{
				Var.printBottom();
				Inl.printBottom();
				p_node.printBottom();
				
				System.out.println("INL ADD");
				
//				try {
//					System.in.read();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				if(!Inl.getLC().getType().equals("NULL"))
				{
					if(Var != null && Var2 != null)
					{
						int InSize = -Var.m_symtab.m_size;
						InSize = InSize/(Var.getDimSize2(Inl));
						
						int InSize2 = -Var2.m_symtab.m_size;
						InSize2 = InSize2/(Var.getDimSize2(Inl2));
						
						int pos = Var.getInLSize(Inl);
						int pos2 = Var.getInLSize(Inl2);
						
						m_moonExecCode += m_mooncodeindent + "% processing add array"
								+ ": " + p_node.m_moonVarName + " := " + Var.m_moonVarName + " + " + Var2.m_moonVarName + "\n";
						
						m_moonExecCode += m_mooncodeindent + "addi " + secondRegister + "," + "r0" + "," + pos + "\n";
						m_moonExecCode += m_mooncodeindent + "muli " + secondRegister + "," + secondRegister + "," + InSize + "\n";
						m_moonExecCode += m_mooncodeindent + "sw " + "inl(r0),"+ secondRegister + "\n";
						m_moonExecCode += m_mooncodeindent + "lw " + leftChildRegister + "," + Var.m_moonVarName + "("+ secondRegister +")\n";
						
						m_moonExecCode += m_mooncodeindent + "addi " + secondRegister + "," + "r0" + "," + pos2 + "\n";
						m_moonExecCode += m_mooncodeindent + "muli " + secondRegister + "," + secondRegister + "," + InSize2 + "\n";
						m_moonExecCode += m_mooncodeindent + "sw " + "inl(r0),"+ secondRegister + "\n";
						m_moonExecCode += m_mooncodeindent + "lw " + rightChildRegister + "," + Var.m_moonVarName + "("+ secondRegister +")\n";
						
						m_moonExecCode += m_mooncodeindent + Op + " " + secondRegister + "," + rightChildRegister + "," + leftChildRegister + "\n";
						
						m_moonExecCode += m_mooncodeindent + "sw " + p_node.m_moonVarName + "(r0)," + secondRegister +"\n";
						m_moonDataCode += String.format("%-10s",p_node.m_moonVarName) + " res " + InSize + "\n";
						
						this.m_registerPool.push(leftChildRegister);
						this.m_registerPool.push(rightChildRegister);
						this.m_registerPool.push(localRegister);
						this.m_registerPool.push(secondRegister);
						return;
					}
					else if(Var != null && Var2 == null)
					{
						
					}
				}
				
				
			}
			else if(!Inl.getLC().getType().equals("NULL"))
			{
				
			}
		}
		this.m_registerPool.push(secondRegister);
		
		m_moonExecCode += m_mooncodeindent + "lw "  + leftChildRegister +  "," + Var.m_moonVarName + "(r0)\n";
		m_moonExecCode += m_mooncodeindent + "lw "  + rightChildRegister + "," + p_node.getChildren().get(1).m_moonVarName + "(r0)\n";
		m_moonExecCode += m_mooncodeindent + Op + " " + localRegister + "," + leftChildRegister + "," + rightChildRegister + "\n"; 
		m_moonDataCode += m_mooncodeindent + "% space for " + p_node.getChildren().get(0).m_moonVarName + " + " + p_node.getChildren().get(1).m_moonVarName + "\n";
		m_moonDataCode += String.format("%-10s",p_node.m_moonVarName) + " res 4\n";
		m_moonExecCode += m_mooncodeindent + "sw " + p_node.m_moonVarName + "(r0)," + localRegister + "\n";
		
		
		
		this.m_registerPool.push(leftChildRegister);
		this.m_registerPool.push(rightChildRegister);
		this.m_registerPool.push(localRegister);
		
		
	}

	public void visit(MultOpNode p_node){ 
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
		// Then, do the processing of this nodes' visitor		
		// create a local variable and allocate a register to this subcomputation 
		String localRegister      = this.m_registerPool.pop();
		String leftChildRegister  = this.m_registerPool.pop();
		String rightChildRegister = this.m_registerPool.pop();
		String secondRegister = this.m_registerPool.pop();
		// generate code
		
		System.out.println(localRegister);
		System.out.println(leftChildRegister);
		System.out.println(rightChildRegister);
		
		AST_Node Var = null;
		AST_Node Var2 = null;
		AST_Node Inl2 = null;
		AST_Node Inl = null;
		
		if( p_node.getChildren().get(0).getLC() != null)
		{
			Var = p_node.getChildren().get(0).getChildren().get(0);
			
			
			if(Var.getChildren().get(1) != null)
			{
				Inl = Var.getChildren().get(1);
			}
			
			if(p_node.getChildren().get(1).getLC() != null)
			{
				Var2 = p_node.getChildren().get(1).getChildren().get(0);
				if(Var2.getChildren().get(1) != null)
				{
					Inl2 = Var2.getChildren().get(1);
				}
			}
		}
		else
		{
			Var = p_node.getChildren().get(0);
		}
		
		
		String Op = "";
		
		Op = mulOP(p_node.getValue().trim());
		
		
		
		if(Inl != null)
		{
			if(!Inl.getLC().getType().equals("NULL"))
			{
				Var.printBottom();
				Inl.printBottom();
				p_node.printBottom();
				
				System.out.println("INL ADD");
				
				try {
					System.in.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(!Inl.getLC().getType().equals("NULL"))
				{
					if(Var != null && Var2 != null)
					{
						int InSize = -Var.m_symtab.m_size;
						InSize = InSize/(Var.getDimSize2(Inl));
						
						int InSize2 = -Var2.m_symtab.m_size;
						InSize2 = InSize2/(Var.getDimSize2(Inl2));
						
						int pos = Var.getInLSize(Inl);
						int pos2 = Var.getInLSize(Inl2);
						
						m_moonExecCode += m_mooncodeindent + "% processing add array"
								+ ": " + p_node.m_moonVarName + " := " + Var.m_moonVarName + " + " + Var2.m_moonVarName + "\n";
						
						m_moonExecCode += m_mooncodeindent + "addi " + secondRegister + "," + "r0" + "," + pos + "\n";
						m_moonExecCode += m_mooncodeindent + "muli " + secondRegister + "," + secondRegister + "," + InSize + "\n";
						m_moonExecCode += m_mooncodeindent + "sw " + "inl(r0),"+ secondRegister + "\n";
						m_moonExecCode += m_mooncodeindent + "lw " + leftChildRegister + "," + Var.m_moonVarName + "("+ secondRegister +")\n";
						
						m_moonExecCode += m_mooncodeindent + "addi " + secondRegister + "," + "r0" + "," + pos2 + "\n";
						m_moonExecCode += m_mooncodeindent + "muli " + secondRegister + "," + secondRegister + "," + InSize2 + "\n";
						m_moonExecCode += m_mooncodeindent + "sw " + "inl(r0),"+ secondRegister + "\n";
						m_moonExecCode += m_mooncodeindent + "lw " + rightChildRegister + "," + Var.m_moonVarName + "("+ secondRegister +")\n";
						
						m_moonExecCode += m_mooncodeindent + Op + " " + secondRegister + "," + rightChildRegister + "," + leftChildRegister + "\n";
						
						m_moonExecCode += m_mooncodeindent + "sw " + p_node.m_moonVarName + "(r0)," + secondRegister +"\n";
						m_moonDataCode += String.format("%-10s",p_node.m_moonVarName) + " res " + InSize + "\n";
						
						this.m_registerPool.push(leftChildRegister);
						this.m_registerPool.push(rightChildRegister);
						this.m_registerPool.push(localRegister);
						this.m_registerPool.push(secondRegister);
						return;
					}
					else if(Var != null && Var2 == null)
					{
						
					}
				}
				
				
			}
			else if(!Inl.getLC().getType().equals("NULL"))
			{
				
			}
		}
		this.m_registerPool.push(secondRegister);
		
		m_moonExecCode += m_mooncodeindent + "lw "  + leftChildRegister +  "," + Var.m_moonVarName + "(r0)\n";
		m_moonExecCode += m_mooncodeindent + "lw "  + rightChildRegister + "," + p_node.getChildren().get(1).m_moonVarName + "(r0)\n";
		m_moonExecCode += m_mooncodeindent + Op + " " + localRegister + "," + leftChildRegister + "," + rightChildRegister + "\n"; 
		m_moonDataCode += m_mooncodeindent + "% space for " + p_node.getChildren().get(0).m_moonVarName + " + " + p_node.getChildren().get(1).m_moonVarName + "\n";
		m_moonDataCode += String.format("%-10s",p_node.m_moonVarName) + " res 4\n";
		m_moonExecCode += m_mooncodeindent + "sw " + p_node.m_moonVarName + "(r0)," + localRegister + "\n";
		
		
		
		this.m_registerPool.push(leftChildRegister);
		this.m_registerPool.push(rightChildRegister);
		this.m_registerPool.push(localRegister);
		
		
	}
	
	public void visit(AssignStatNode p_node){ 
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
		// Then, do the processing of this nodes' visitor
		// allocate local register
		String localRegister = this.m_registerPool.pop();
		String secondRegister = this.m_registerPool.pop();
		//generate code
		
		System.out.println("TAG ASSS");
		p_node.printBottom();
		
		AST_Node Var = p_node.getChildren().get(0).getChildren().get(0);
		
		Var.printBottom();
		
		if(!Var.m_moonVarName.equals(""))
		{
			Var.printBottom();
			
//			try {
//				System.in.read();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			String name = "";
			
			/*Var.printBottom();
			Var.getRSib().printBottom();
			//p_node.getChildren().get(1).getChildren().get(0).printBottom();
			
			String name = "";
			String name2 = "";
			if(p_node.getChildren().get(1).getChildren().get(0) != null)
			{
				if(p_node.getChildren().get(1).getChildren().get(0).getType().equals("ID"))
				{
					name = p_node.getChildren().get(1).getChildren().get(0).m_moonVarName;
					name2 = p_node.searchFunction(p_node.getChildren().get(1).getChildren().get(1), "ID").getValue().trim();
					
					
				}
				else
				{
					name = p_node.getChildren().get(1).m_moonVarName;
				}
				
			}*/
			if(true)
			{
				name = p_node.getChildren().get(1).m_moonVarName;
			}
			
			//p_node.getChildren().get(1).getChildren().get(1).printBottom();
			
			/*System.out.println(p_node.searchFunction(p_node.getChildren().get(1).getChildren().get(1), "IDn").m_moonVarName);
			
			AST_Node IDN = p_node.getChildren().get(1).getChildren().get(1);
			
			
			IDN.printBottom();
			
			
			
			IDN.getLC().printBottom();
			IDN.getLC().getLC().printBottom();
			IDN.getLC().getLC().getLC().printBottom();
			
			System.out.println(IDN.m_moonVarName);
			System.out.println(IDN.getLC().m_moonVarName);
			System.out.println(IDN.getLC().getLC().m_moonVarName);
			System.out.println(IDN.getLC().getLC().getLC().m_moonVarName);*/
			
			String Name = "";
			
			if(p_node.getChildren().get(1).getType().equals("Rexpr"))
			{
				Name = "rel";
			}
			else
			{
				Name = p_node.getChildren().get(1).m_moonVarName;
			}
			
			
			m_moonExecCode += m_mooncodeindent + "% processing: "  + Var.m_moonVarName + " := " + name + "\n";
			m_moonExecCode += m_mooncodeindent + "lw " + localRegister + "," + Name + "(r0)" + "\n";
		
			
			if(Var.getRSib().getType().equals("InL"))
			{
				if(!Var.getRSib().getLC().getType().equals("NULL"))
				{
					System.out.println("ADDDDINGNGNG");
					
//					try {
//						System.in.read();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
					AST_Node Inl = p_node.getChildren().get(0).getChildren().get(1);
					
					int InSize = -Var.m_symtab.m_size;
					InSize = InSize/(Var.getDimSize2(Var));
					
					String thirdRegister = this.m_registerPool.pop();
					//String fourthRegister = this.m_registerPool.pop();
					m_moonExecCode += m_mooncodeindent + "% processing: arr \n";
					
					int pos = Var.getInLSize(Var.getRSib());
					
					m_moonExecCode += m_mooncodeindent + "addi " + thirdRegister + "," + "r0," + pos + "\n";
					m_moonExecCode += m_mooncodeindent + "muli " + secondRegister + "," + thirdRegister + "," + InSize + "\n";
					//m_moonExecCode += m_mooncodeindent + "add " + secondRegister + "," + secondRegister + "," + thirdRegister + "\n";
					m_moonExecCode += m_mooncodeindent + "sw " + Var.m_moonVarName + "(" + secondRegister + ")" + "," + localRegister + "\n";
					
					this.m_registerPool.push(thirdRegister);
					//this.m_registerPool.push(fourthRegister);
				}
				else
				{
					m_moonExecCode += m_mooncodeindent + "sw " + Var.m_moonVarName + "(r0)" + "," + localRegister + "\n";
				}
			}
			else
			{
				m_moonExecCode += m_mooncodeindent + "sw " + Var.m_moonVarName + "(r0)" + "," + localRegister + "\n";
			}
			
			
			//deallocate local register
			this.m_registerPool.push(localRegister);
			this.m_registerPool.push(secondRegister);
		}
	}
	
	public void visit(IndexListNode p_node) 
	{
		for (AST_Node child : p_node.getChildren()){
			child.accept(this);
		}
		
		String localRegister      = this.m_registerPool.pop();
		String leftChildRegister  = this.m_registerPool.pop();
		String rightChildRegister = this.m_registerPool.pop();
		
		int count = 0;
		boolean check = false;
		
		for (AST_Node child : p_node.getChildren()){
			
			if(!child.getType().equals("NULL"))
			{
				if(!child.getType().equals("Aexpr"))
				{
					if(!child.getValue().trim().equals("0"))
					{
						if(!check)
						{
							check = true;
							
							m_moonExecCode += m_mooncodeindent + "addi " + localRegister + "," + "r0,1" + "\n";
							m_moonExecCode += m_mooncodeindent + "sw " + "inl" + "(r0)" + "," +localRegister + "\n";
							
						}
						
						m_moonExecCode += m_mooncodeindent + "lw " + leftChildRegister + "," + child.m_moonVarName + "(r0)" + "\n";
						m_moonExecCode += m_mooncodeindent + "lw " + rightChildRegister + ",inl(r0)" + "\n";
						m_moonExecCode += m_mooncodeindent + "mul " + localRegister + "," + leftChildRegister + "," + rightChildRegister + "\n";
						m_moonExecCode += m_mooncodeindent + "sw " + "inl" + "(r0)" + "," + localRegister + "\n";
					}
					else if(count == 0)
					{
						m_moonExecCode += m_mooncodeindent + "addi " + localRegister + "," + "r0,0" + "\n";
						m_moonExecCode += m_mooncodeindent + "sw " + "inl" + "(r0)" + "," +localRegister + "\n";
					}
					count += 1;
				}
			}
			else
			{
				if(count > 1)
				{
					System.out.println(p_node.m_moonVarName);
					
					p_node.printBottom();
					
				}
				else
				{
					
				}
			}
		}
		
		this.m_registerPool.push(leftChildRegister);
		this.m_registerPool.push(rightChildRegister);
		this.m_registerPool.push(localRegister);
		
		
	}
	
	public void visit(ProgramBlockNode p_node) {
		// generate moon program's entry point
		m_moonExecCode += m_mooncodeindent + "entry\n";
		m_moonExecCode += m_mooncodeindent + "addi r14,r0,topaddr\n";
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
		// generate moon program's end point
		m_moonDataCode += m_mooncodeindent + "% buffer space used for console output\n";
		m_moonDataCode += String.format("%-11s", "buf") + "res 20\n";
		m_moonExecCode += m_mooncodeindent + "hlt\n";
	}
	
	public void visit(PutStatNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
		// Then, do the processing of this nodes' visitor		
		// create a local variable and allocate a register to this subcomputation 
		
		//TEMPPP
		
		//this.m_registerPool.push(p_node.getChild("Expr").getChild("ID").getValue().trim());
		AST_Node Var = null;
		
		if(p_node.NumTest(p_node.getChildren().get(0).getValue()))
		{
			Var = p_node.getChildren().get(0);
		}
		else
		{
			Var = p_node.getChildren().get(0).getChildren().get(0);
		}
		
		String localRegister      = this.m_registerPool.pop();
		String secondRegister      = this.m_registerPool.pop();
		//generate code
		
		int InlSize = p_node.getInLSize(Var.getRSib());
		int size = InlSize * 4;
		
		
		m_moonExecCode += m_mooncodeindent + "% processing: put("  + Var.m_moonVarName + ")\n";
		
		if(Var.getRSib().getType().equals("InL"))
		{
			Var.getRSib().printBottom();
		
			
			if((!Var.getRSib().getLC().getType().equals("NULL"))&&(!Var.getRSib().getLC().getType().equals("Aexpr")))
			{
				int InSize = -Var.m_symtab.m_size;
				InSize = InSize/(Var.getDimSize2(Var));
				
				String thirdRegister      = this.m_registerPool.pop();
				m_moonExecCode += m_mooncodeindent + "lw " + thirdRegister + "," + "inl(r0)" + "\n";
				m_moonExecCode += m_mooncodeindent + "muli " + secondRegister + "," + thirdRegister + "," + InSize + "\n";
				
				m_moonExecCode += m_mooncodeindent + "lw " + localRegister + "," + Var.m_moonVarName + "("+ secondRegister +")\n";
				this.m_registerPool.push(thirdRegister);
			}
			else
			{
				m_moonExecCode += m_mooncodeindent + "lw " + localRegister + "," + Var.m_moonVarName + "(r0)\n";
			}
		}
		else
		{
			m_moonExecCode += m_mooncodeindent + "lw " + localRegister + "," + Var.m_moonVarName + "(r0)\n";
		}
		this.m_registerPool.push(secondRegister);
		
		m_moonExecCode += m_mooncodeindent + "% put value on stack\n";	
		m_moonExecCode += m_mooncodeindent + "sw -8(r14)," + localRegister + "\n";
		m_moonExecCode += m_mooncodeindent + "% link buffer to stack\n";	
		m_moonExecCode += m_mooncodeindent + "addi " + localRegister + ",r0, buf\n";
		m_moonExecCode += m_mooncodeindent + "sw -12(r14)," + localRegister + "\n";
		m_moonExecCode += m_mooncodeindent + "% convert int to string for output\n";	
		m_moonExecCode += m_mooncodeindent + "jl r15, intstr\n";	
		m_moonExecCode += m_mooncodeindent + "sw -8(r14),r13\n";
		m_moonExecCode += m_mooncodeindent + "% output to console\n";	
		m_moonExecCode += m_mooncodeindent + "jl r15, putstr\n";
		//deallocate local register
		this.m_registerPool.push(localRegister);		
	};
	
	
	public void visit(IfNode p_node)
	{
		p_node.getChildren().get(0).accept(this);
		
		String localRegister = this.m_registerPool.pop();
		//generate code
		
		System.out.println("TAG ASSS");
		p_node.printBottom();
		
		AST_Node Var = p_node.getChildren().get(0).getChildren().get(0);
		
		Var.printBottom();
		
		p_node.printBottom();
		
		int ifs = p_node.m_symtab.m_if;
		
		BroadIf(p_node);
		
		
		
			m_moonExecCode += m_mooncodeindent + "% processing: "  + "if statement\n";
			m_moonExecCode += m_mooncodeindent + "lw " + localRegister + "," + " rel(r0)\n";
			m_moonExecCode += m_mooncodeindent + "bz " + localRegister + "," + " else_" + ifs + "\n";
			
			p_node.getChildren().get(1).accept(this);
			
			m_moonExecCode += m_mooncodeindent + "% processing: "  + "if block\n";
			m_moonExecCode += m_mooncodeindent + "j " + "endif_" + ifs + "%jump out of the else block\n";
			m_moonExecCode += m_mooncodeindent + "% processing: "  + "else block\n";
			m_moonExecCode += "else_" + ifs + "\n";
			
			p_node.getChildren().get(2).accept(this);
			
			
			m_moonExecCode += "endif_" + ifs + " nop %end of if statement\n";
			
			
			//deallocate local register
			this.m_registerPool.push(localRegister);	
		
	}
	
	public int sizeOfEntry(AST_Node p_node) {
		int size = 0;
		
		if(p_node.getValue().trim().equals("INTDC"))
			size = 4;
		else if(p_node.getValue().trim().equals("FLOATDC"))
			size = 8;
		else if(p_node.ClassCheck(p_node))
			size = p_node.ClassSize(p_node);
		
		return size;
	}
	
	public void visit(ForNode p_node)
	{
		AST_Node Type = p_node.getChildren().get(0);
		AST_Node Id = p_node.getChildren().get(1);
		AST_Node Exr = p_node.getChildren().get(2);
		AST_Node Rel = p_node.getChildren().get(3);
		AST_Node Asop = p_node.getChildren().get(4);
		AST_Node Sb = p_node.getChildren().get(5);
		
		String name = Id.getValue().trim();
		
		String localRegister = this.m_registerPool.pop();
		//generate code
		int size = 0;
		
		Type.printBottom();
		Id.printBottom();
		
		int fors = p_node.m_symtab.m_for;
		
		System.out.println("FOOOOORRR");
		
		System.out.println(p_node.m_symtab.m_for);
		
		
		BroadFor(p_node);
		
		System.out.println(fors);
		
		
		
		if(!p_node.m_symtab.lookupVar(Id.getName()))
		{
			size = this.sizeOfEntry(Type);
			
			m_moonDataCode += m_mooncodeindent + "% for loop Var res\n";
			m_moonDataCode += String.format("%-11s", Id.getValue().trim()) + "res " + size + "\n";
		}
		
		int value = Integer.parseInt(Exr.getValue().trim());
		
		System.out.println(value);
		
		m_moonExecCode += m_mooncodeindent + "% processing: "  + "for allocation statement\n";
		m_moonExecCode += m_mooncodeindent + "addi " + localRegister + ", r0 ," + value +"\n";
		m_moonExecCode += m_mooncodeindent + "sw " + name + "(r0), " + localRegister + " \n";
		m_moonExecCode += "for_" + fors + "\n";
		
		Sb.accept(this);
		
		Asop.accept(this);
		
		Rel.accept(this);
		
		m_moonExecCode += m_mooncodeindent + "% processing: "  + "for condition\n";
		m_moonExecCode += m_mooncodeindent + "lw " + localRegister + "," + " rel(r0)\n";
		m_moonExecCode += m_mooncodeindent + "bnz " + localRegister + "," + " for_" + fors + "\n";
		
		m_moonExecCode += m_mooncodeindent + "j " + "endfor_" + fors + "%jump out of the else block\n";
		m_moonExecCode += m_mooncodeindent + "% processing: "  + "else block\n";
		m_moonExecCode += "endfor_" + fors + "\n";
		
		this.m_registerPool.push(localRegister);	
	
	}
	
	public void visit(RelNode p_node)
	{
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
		// Then, do the processing of this nodes' visitor
		// create a local variable and allocate a register to this subcomputation 
		String localRegister      = this.m_registerPool.pop();
		String leftChildRegister  = this.m_registerPool.pop();
		String rightChildRegister = this.m_registerPool.pop();
		// generate code
		
		System.out.println(localRegister);
		System.out.println(leftChildRegister);
		System.out.println(rightChildRegister);
		
		String rel = "";
		String operation = p_node.getChildren().get(1).getValue().trim();
		
		
		
		rel = relOP(p_node.getChildren().get(1).getValue());
		
		m_moonExecCode += m_mooncodeindent + "% processing: " + "rel" + " := " + p_node.getChildren().get(0).m_moonVarName + operation + p_node.getChildren().get(2).m_moonVarName + "\n";
		m_moonExecCode += m_mooncodeindent + "lw "  + leftChildRegister +  "," + p_node.getChildren().get(0).m_moonVarName + "(r0)\n";
		m_moonExecCode += m_mooncodeindent + "lw "  + rightChildRegister + "," + p_node.getChildren().get(2).m_moonVarName + "(r0)\n";
		m_moonExecCode += m_mooncodeindent + rel + localRegister +      "," + leftChildRegister + "," + rightChildRegister + "\n"; 
		m_moonExecCode += m_mooncodeindent + "sw " + "rel" + "(r0)," + localRegister + "\n";
		// deallocate the registers for the two children, and the current node
		this.m_registerPool.push(leftChildRegister);
		this.m_registerPool.push(rightChildRegister);
		this.m_registerPool.push(localRegister);
	}
	
	
	public AST_Node getTop(AST_Node p_node)
	{
		while(p_node.getParent() != null)
		{
			p_node = p_node.getParent();
		}
		
		return p_node;
	}
	
	public String relOP(String Value)
	{
		String rel = "";
		
		if((int)Value.charAt(0) == 61)
		{
			if((int)Value.charAt(1) == 61)
			{
				return "ceq ";
			}
		}
		else if((int)Value.charAt(0) == 60)
		{
			if((int)Value.charAt(1) == 61)
			{
				return "cle ";
			}
			else if((int)Value.charAt(1) == 62)
			{
				return "cne ";
			}
			return "clt ";
		}
		else if((int)Value.charAt(0) == 62)
		{
			if((int)Value.charAt(1) == 61)
			{
				return "cge ";
			}
			return "cgt ";
		}
		
		return rel;
	}
	
	
	public String addOP(String Value)
	{
		String add = "";
		
		if((int)Value.charAt(0) == 43)
		{
			return "add";
		}
		else if((int)Value.charAt(0) == 45)
		{
			return "sub ";
		}
		else if((int)Value.charAt(0) == 111)
		{
			return "or";
		}
		
		return add;
	}
	
	public String mulOP(String Value)
	{
		String add = "";
		
		if((int)Value.charAt(0) == 42)
		{
			return "mul";
		}
		else if((int)Value.charAt(0) == 47)
		{
			return "div ";
		}
		else if((int)Value.charAt(0) == 97)
		{
			return "and";
		}
		
		return add;
	}
	
	public void visit(GetStatNode p_node)
	{
		// propagate accepting the same visitor to all the children
				// this effectively achieves Depth-First AST Traversal
				for (AST_Node child : p_node.getChildren())
					child.accept(this);
				// Then, do the processing of this nodes' visitor		
				// create a local variable and allocate a register to this subcomputation 
				
				//TEMPPP
				
				//this.m_registerPool.push(p_node.getChild("Expr").getChild("ID").getValue().trim());
				AST_Node Var = null;
				
				if(p_node.NumTest(p_node.getChildren().get(0).getValue()))
				{
					Var = p_node.getChildren().get(0);
				}
				else
				{
					Var = p_node.getChildren().get(0).getChildren().get(0);
				}
				
				String localRegister      = this.m_registerPool.pop();
				//generate code
				m_moonExecCode += m_mooncodeindent + "% processing: get("  + Var.m_moonVarName + ")\n";
				m_moonExecCode += m_mooncodeindent + "jw " + localRegister +"\n";
				
				
				
				
				m_moonExecCode += m_mooncodeindent + "lw " + localRegister + "," + Var.m_moonVarName + "(r0)\n";
				m_moonExecCode += m_mooncodeindent + "% put value on stack\n";	
				m_moonExecCode += m_mooncodeindent + "sw -8(r14)," + localRegister + "\n";
				m_moonExecCode += m_mooncodeindent + "% link buffer to stack\n";	
				m_moonExecCode += m_mooncodeindent + "addi " + localRegister + ",r0, buf\n";
				m_moonExecCode += m_mooncodeindent + "sw -12(r14)," + localRegister + "\n";
				m_moonExecCode += m_mooncodeindent + "% convert int to string for output\n";	
				m_moonExecCode += m_mooncodeindent + "jl r15, intstr\n";	
				m_moonExecCode += m_mooncodeindent + "sw -8(r14),r13\n";
				m_moonExecCode += m_mooncodeindent + "% output to console\n";	
				m_moonExecCode += m_mooncodeindent + "jl r15, putstr\n";
				//deallocate local register
				this.m_registerPool.push(localRegister);
	}
	
	public void visit(FuncDefNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		m_moonExecCode += m_mooncodeindent + "% processing function definition: "  + p_node.m_moonVarName + "\n";
		//create the tag to jump onto 
		m_moonExecCode += String.format("%-10s",p_node.getValue().trim());
		// copy the jumping-back address value in a tagged cell named "fname" appended with "link"
		m_moonDataCode += String.format("%-11s", p_node.getValue().trim() + "link") + "res 4\n";
		m_moonExecCode += m_mooncodeindent + "sw " + p_node.getValue().trim() + "link(r0),r15\n";
		// tagged cell for return value
		// here assumed to be integer (limitation)
		m_moonDataCode += String.format("%-11s", p_node.getValue().trim() + "return") + "res 4\n";
		//generate the code for the function body
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
		// copy back the jumping-back address into r15
		m_moonExecCode += m_mooncodeindent + "lw r15," + p_node.getValue().trim() + "link(r0)\n";
		// jump back to the calling function
		m_moonExecCode += m_mooncodeindent + "jr r15\n";	
	};
	
	public void visit(FuncCallNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
		String localregister1 = this.m_registerPool.pop();
		// pass parameters directly in the function's local variables
		// it is assumed that the parameters are the first n entries in the 
		// function's symbol table 
		// here we assume that the parameters are the size of a word, 
		// which is not true for arrays and objects. 
		// In those cases, a loop copying the values e.g. byte-by-byte is necessary
		SymTabEntry tableentryofcalledfunction = p_node.m_symtab.lookupName(p_node.getValue().trim());
		int indexofparam = 0;
		
		
		
		m_moonExecCode += m_mooncodeindent + "% processing: function call to "  + p_node.getChildren().get(0).m_moonVarName + " \n";
		
		System.out.println("HEHEHEHEHEHHEHEH");
		p_node.printBottom();
		
		for(AST_Node param : p_node.getChildren().get(1).getChildren()){
			System.out.println("PARAM");
			param.printBottom();
			
			if(!param.getType().equals("NULL"))
			{
				if(!param.NumTest(param.getValue()))
				{
					AST_Node Var = param.getChildren().get(0);
					
					System.out.println("Param");
					
					param.printBottom();
					
					m_moonExecCode += m_mooncodeindent + "lw " + localregister1 + "," + Var.m_moonVarName + "(r0)\n";
				    String nameofparam = tableentryofcalledfunction.m_subtable.m_symlist.get(indexofparam).m_name;
					m_moonExecCode += m_mooncodeindent + "sw " + nameofparam + "(r0)," + localregister1 + "\n";
					indexofparam++;
				}
				
			}

		}
		// jump to the called function's code
		// here the name of the label is assumed to be the function's name
		// a unique label generator is necessary in the general case (limitation)
		m_moonExecCode += m_mooncodeindent + "jl r15," + p_node.getValue().trim() + "\n";
		// copy the return value in a tagged memory cell
		m_moonDataCode += m_mooncodeindent + "% space for function call expression factor\n";		
		m_moonDataCode += String.format("%-11s", p_node.m_moonVarName) + "res 4\n";
		m_moonExecCode += m_mooncodeindent + "lw " + localregister1 + "," + p_node.getValue().trim() + "return(r0)\n";
		
		
		m_moonExecCode += m_mooncodeindent + "sw " + p_node.m_moonVarName + "(r0)," + localregister1 + "\n";
		System.out.println(p_node.m_moonVarName);
		
		this.m_registerPool.push(localregister1);	
	}; 
	
	public void visit(ReturnStatNode p_node){
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		String localregister1 = this.m_registerPool.pop();
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
		// copy the result of the return value in a cell tagged with the name "function name" + "return", e.g. "f1return"
		// get the function name from the symbol table
		
		AST_Node Var = null;
		
		if(p_node.NumTest(p_node.getChildren().get(0).getValue()))
		{
			Var = p_node.getChildren().get(0);
		}
		else
		{
			Var = p_node.getChildren().get(0).getChildren().get(0);
		}
		
		m_moonExecCode += m_mooncodeindent + "% processing: return("  + Var.m_moonVarName + ")\n";
		m_moonExecCode += m_mooncodeindent + "lw " + localregister1 + "," + Var.m_moonVarName + "(r0)\n";
		m_moonExecCode += m_mooncodeindent + "sw "   + p_node.m_symtab.m_name + "return(r0)," + localregister1 + "\n";
		this.m_registerPool.push(localregister1);	
	}
	
	// Below are the visit methods for node types for which this visitor does
	// not apply. They still have to propagate acceptance of the visitor to
	// their children.
	
    public void visit(ParamListNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
    	
    	for(AST_Node fparam : p_node.getChildren())
    	{
    		if(fparam.getType().equals("NULL"))
    		{
    			break;
    		}
    		AST_Node Type = fparam.getChildren().get(0);
    		AST_Node ID = fparam.getChildren().get(1);
    		AST_Node Asiz = fparam.getChildren().get(2);
    		
    		int size = 0;
			String name = ID.getValue().trim();
			String insert = "";
			
			if(fparam.isClass(Type))
			{
				size = fparam.getClassSize(Type);
			}
			else if(Type.getType().equals("INTDC"))
			{
				size = 4;
			}
			else if(Type.getType().equals("FLOATDC"))
			{
				size = 8;
			}
			
			size *= Asiz.getDimSize(Asiz);
			
			insert = String.format("%-10s", name) + " res " + size +"\n";
			
			System.out.println(insert);
			
			m_moonDataCode += insert;
    	}

		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
    }
    
	public void visit(ClassListNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
	};

	public void visit(ClassNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
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

	public void visit(AST_Node p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
	};

	public void visit(StatBlockNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren()){
			child.accept(this);
		}
	};

	public void visit(TypeNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
    };

	public void visit(DimNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);		
	}; 

	public void visit(IdNode p_node){
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
	}
}

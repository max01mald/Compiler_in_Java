package A4;

import java.io.File;
import java.io.PrintWriter;

import A3.SymTabEntry;
import A3.SymTabl;
import A3.VarEntry;
import AST_Tree.AST_Node;
import Element_Node.*;
import Visitor.Visitor;

/** 
 */

public class ComputeMemSizeVisitor extends Visitor {

	public String  m_outputfilename = new String(); 

	public ComputeMemSizeVisitor() {
	}
	
	public ComputeMemSizeVisitor(String p_filename) {
		this.m_outputfilename = p_filename; 
	}
	
	
	
	
	public int sizeOfEntry(AST_Node p_node) {
		int size = 0;
		
		if(p_node.m_symtabentry.m_type == "int")
			size = 4;
		else if(p_node.m_symtabentry.m_type == "float")
			size = 8;
		else if(p_node.ClassCheck(p_node))
			size = p_node.ClassSize(p_node);
		
		//System.out.println(size);
		
		// if it is an array, multiply by all dimension sizes
		VarEntry ve = (VarEntry) p_node.m_symtabentry; 
		if(!ve.m_dims.isEmpty())
			for(Integer dim : ve.m_dims)
				size *= dim;	
		return size;
	}
	
	public int sizeOfTypeNode(AST_Node p_node) {
		int size = 0;
		
		System.out.println("SIIIIIIZZZZ " + p_node.getType());
		
		if(p_node.getType() == "INT")
			size = 4;
		else if(p_node.getType() == "FLOAT")
			size = 8;
		
		return size;
	}
	
	public void visit(ProgNode p_node){
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
		if (!this.m_outputfilename.isEmpty()) {
			File file = new File(this.m_outputfilename);
			try (PrintWriter out = new PrintWriter(file)){ 
			    out.println(p_node.m_symtab);
			}
			catch(Exception e){
				e.printStackTrace();}
		}
	};
	
	public void visit(ProgramBlockNode p_node){
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
		// compute total size and offsets along the way
		// this should be node on all nodes that represent
		// a scope and contain their own table
		for (SymTabEntry entry : p_node.m_symtab.m_symlist){
			entry.m_offset     = p_node.m_symtab.m_size - entry.m_size;
			p_node.m_symtab.m_size -= entry.m_size;
		}
	};
	
	public void visit(ClassNode p_node){
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
		// compute total size and offsets along the way		
		// this should be node on all nodes that represent
		// a scope and contain their own table
		for (SymTabEntry entry : p_node.m_symtab.m_symlist){
			entry.m_offset = p_node.m_symtab.m_size - entry.m_size;
			
			p_node.printBottom();
			System.out.println("Size of entry " + entry.m_size);
			
			p_node.m_symtab.m_size -= entry.m_size;
			
			System.out.println("Table size " + p_node.m_symtab.m_size);
		}
		
		for (AST_Node inher : p_node.getChildren().get(1).getChildren())
		{
			inher.printBottom();
			if(p_node.isClass(inher))
			{
				p_node.m_symtab.m_size -= p_node.getClassSize(inher);
			}
		}
		
	};

	public void visit(FuncDefNode p_node){
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
		// compute total size and offsets along the way
		// this should be node on all nodes that represent
		// a scope and contain their own table
		// stack frame contains the return value at the bottom of the stack
		p_node.m_symtab.m_size = -(this.sizeOfTypeNode(p_node.getChildren().get(0)));
		
		
		//then is the return addess is stored on the stack frame
		p_node.m_symtab.m_size -= 4;
		for (SymTabEntry entry : p_node.m_symtab.m_symlist){
			entry.m_offset = p_node.m_symtab.m_size - entry.m_size; 
			p_node.m_symtab.m_size -= entry.m_size;
		}
	};
	
	public void visit(VarDeclNode p_node){
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
		// determine the size for basic variables
		p_node.m_symtabentry.m_size = this.sizeOfEntry(p_node);
	}

	public void visit(MultOpNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
		p_node.m_symtabentry.m_size = this.sizeOfEntry(p_node);
	};
	
	public void visit(AddOpNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
		p_node.m_symtabentry.m_size = this.sizeOfEntry(p_node);
	};
	
	// Below are the visit methods for node types for which this visitor does
	// not apply. They still have to propagate acceptance of the visitor to
	// their children.

	public void visit(NumNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
		p_node.m_symtabentry.m_size = this.sizeOfEntry(p_node);
	};
	
	public void visit(AssignStatNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
	};

	public void visit(ClassListNode p_node) {
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
	
	public void visit(PutStatNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren())
			child.accept(this);
	};

	public void visit(StatBlockNode p_node){
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
	};

	public void visit(TypeNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
	 };
	
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
	}; 

	public void visit(FuncCallNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST_Node child : p_node.getChildren() )
			child.accept(this);
		p_node.m_symtabentry.m_size = this.sizeOfEntry(p_node);
	}; 
	
	public void visit(ReturnStatNode p_node) {
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
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
	
	public void visit(IndexListNode p_node)
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

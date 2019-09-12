package A3;

import AST_Tree.AST_Node;

public class SemanticCorrectness {
	
	public boolean isCorrect(AST_Node p_node)
	{
		for(AST_Node child : p_node.getChildren())
		{
			System.out.println(child.m_symtab.valid);
			child.printBottom();
			
			isCorrect(child);
			
		}
		
		
		
		return true;
	}
}

package Element_Node;
import AST_Tree.AST_Node;
import Visitor.Visitor;


public class TypeNode extends AST_Node {
	
	public TypeNode(String p_data){
		super(p_data);
	}
	
	public TypeNode(AST_Node p_Node){
		super(p_Node);
	}
	 
	public void accept(Visitor p_visitor) {
		p_visitor.visit(this);
	}
}

package Element_Node;
import AST_Tree.AST_Node;
import Visitor.Visitor;


public class IfNode extends AST_Node {
	
	public IfNode(String p_data){
		super(p_data);
	}
	
	public IfNode(AST_Node p_Node){
		super(p_Node);
	}
	
	public void accept(Visitor p_visitor) {
		p_visitor.visit(this);
	}
}

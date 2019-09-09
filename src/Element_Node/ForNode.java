package Element_Node;
import AST_Tree.AST_Node;
import Visitor.Visitor;


public class ForNode extends AST_Node {
	
	public ForNode(String p_data){
		super(p_data);
	}
	
	public ForNode(AST_Node p_Node){
		super(p_Node);
	}
	
	public void accept(Visitor p_visitor) {
		p_visitor.visit(this);
	}
}

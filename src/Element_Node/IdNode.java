package Element_Node;
import AST_Tree.AST_Node;
import Visitor.Visitor;


public class IdNode extends AST_Node {
	
	public IdNode(String p_data){
		super(p_data);
	}
	
	public IdNode(AST_Node p_Node){
		super(p_Node);
	}
	
	public void accept(Visitor p_visitor) {
		p_visitor.visit(this);
	}
}

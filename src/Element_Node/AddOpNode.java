package Element_Node;
import AST_Tree.AST_Node;
import Visitor.Visitor;

public class AddOpNode extends AST_Node {
	
	public AddOpNode(String p_data){
		super(p_data);
	}
	
	public AddOpNode(AST_Node p_Node){
		super(p_Node); 
		
	}

	public void accept(Visitor p_visitor) {
		p_visitor.visit(this);
	}
}
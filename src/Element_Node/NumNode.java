package Element_Node;
import AST_Tree.AST_Node;
import Visitor.Visitor;


public class NumNode extends AST_Node {
	
	public NumNode(String p_data){
		super(p_data);
	}
	
	public NumNode(AST_Node p_Node){
		super(p_Node);
	}
	
	public void accept(Visitor p_visitor) {
		p_visitor.visit(this);
	}
}

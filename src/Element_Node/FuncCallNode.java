package Element_Node;
import AST_Tree.AST_Node;
import Visitor.Visitor;


public class FuncCallNode extends AST_Node {
		
	public FuncCallNode(){
		super("");
	}
	
	public FuncCallNode(AST_Node p_Node){
		super(p_Node); 	
	}
	
	public void accept(Visitor p_visitor) {
		p_visitor.visit(this);
	}
}
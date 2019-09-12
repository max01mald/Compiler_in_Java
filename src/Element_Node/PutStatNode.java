package Element_Node;
import AST_Tree.AST_Node;
import Visitor.Visitor;

public class PutStatNode extends AST_Node {
	
	public PutStatNode(){
		super("");
	}
		
	public PutStatNode(AST_Node p_Node){
		super(p_Node); 
	}
	
	public void accept(Visitor p_visitor) {
		p_visitor.visit(this);
	}
}

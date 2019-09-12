package Element_Node;
import AST_Tree.AST_Node;
import Visitor.Visitor;


public class GetStatNode extends AST_Node {
		
	public GetStatNode(){
		super("");
	}
	
	public GetStatNode(AST_Node p_Node){
		super(p_Node); 
		
	}
	
	public void accept(Visitor p_visitor) {
		p_visitor.visit(this);
	}
	
}
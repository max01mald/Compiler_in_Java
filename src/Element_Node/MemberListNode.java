package Element_Node;
import AST_Tree.AST_Node;
import Visitor.Visitor;


public class MemberListNode extends AST_Node {
		
	public MemberListNode(){
		super("");
	}
	
	public MemberListNode(AST_Node p_Node){
		super(p_Node); 
		
	}
	
	public void accept(Visitor p_visitor) {
		p_visitor.visit(this);
	}
	
}
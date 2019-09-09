package Element_Node;
import AST_Tree.AST_Node;
import Visitor.Visitor;


public class DataMemberNode extends AST_Node {
	
	public DataMemberNode(){
		super("");
	}
	
	public DataMemberNode(AST_Node p_Node){
		super(p_Node);
		
	}

	public void accept(Visitor p_visitor) {
		p_visitor.visit(this);
	}
}

package Element_Node;
import AST_Tree.AST_Node;
import Visitor.Visitor;

public class DimNode extends AST_Node {
	
	public DimNode(String p_data){
		super(p_data);
	}
	
	public DimNode(AST_Node p_Node){
		super(p_Node);
	}
	
	public void accept(Visitor p_visitor) {
		p_visitor.visit(this);
	}
}

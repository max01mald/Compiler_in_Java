package A3;

import A2.Parser;
import A4.ComputeMemSizeVisitor;
import A4.TagsBasedCodeGenerationVisitor;
import AST_Tree.AST_Node;
import AST_Tree.Transform;
import Element_Node.ProgNode;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Parser p = new Parser();
		Transform t = new Transform();
		
		p.setFile("test.txt");
		
		System.out.println(p.Parse());
		
		AST_Node nw = p.getDTree();
		
		SymTabCreationVisitor cv = new SymTabCreationVisitor();
		TypeCheckingVisitor tc = new TypeCheckingVisitor();
		ComputeMemSizeVisitor ms = new ComputeMemSizeVisitor();
		TagsBasedCodeGenerationVisitor tg = new TagsBasedCodeGenerationVisitor("Code.txt");
		
		
		ASTPrinterVisitor pv = new ASTPrinterVisitor();
		
		nw.accept(pv);
		
		nw.accept(cv);
		
		nw.accept(tc);
		
		//nw.accept(ms);
		
		//nw.accept(tg);
		
		
		
		nw.m_symtab.printTables(nw, "");
		
	}

}

package AST_Tree;

import java.io.IOException;

import A2.Parser;
import Element_Node.*;

public class Transform 
{
	public void Switch(AST_Node Parent, AST_Node Child, int pos)
	{
		System.out.println("POOOOOSSS " + pos);
		if(Parent.getChildren() == null)
		{
			return;
		}
		
		
		
		if(Child.getType().equals("NULL"))
		{
			return;
		}
		
		if(pos >= Parent.getChildren().size())
		{
			pos = Parent.getChildren().size()-1;
			
			System.out.println("pos2 " +pos);
		}
		
		
		
		Parent.getChildren().add(pos, Child);
		Parent.getChildren().remove(pos+1);
		
		return;
	}
	
	public int ListFinder(AST_Node Parent, AST_Node Original)
	{
		int count = 0;
		
		AST_Node Check = null;
		
		
		for(int i =0; i<Parent.getChildren().size(); i++)
		{
			if(Parent.getChildren().get(i).getVisited())
			{
				Parent.getChildren().get(i).setVisited(false);
				
				return i;
				
			}
		}
		
		return Parent.getChildren().size()-1;
	}
	
	public AST_Node transform(AST_Node old, AST_Node Parent, int posi)
	{
		System.out.println(old.getName() + " " + old.getType() + " " + old.getValue());
		
		System.out.println(old.getLocation());
		
		int pos = posi;
		
		for(int i=0; i<old.getChildren().size(); i++)
		{
			if(old.getChildren().get(i).getType().equals("Asop"))
			{
				old.printBottom();
				
				System.out.println(i);
				
			}
			transform(old.getChildren().get(i), old, i);
			
		}
		
		if(old.getName().equals("ID")||old.getType().equals("ID"))
		{
			if(old.getParent().getType().equals("VarEl"))
			{
				old = new NumNode(old);
			}
			else if(old.getType().equals("ID") || (!old.getValue().equals("")))
			{
				old = new IdNode(old);
				
				old.Type = "ID";
				
				System.out.println(old.getClass() + " " + old.getValue());
				
			}
			else if(old.getType().equals("ID") || (!old.getValue().equals("")))
			{
				old = new IdNode(old);
				
				old.Type = "ID";
				
				System.out.println(old.getClass() + " " + old.getValue());
				
			}
			
			
		}
		else if(old.getType().equals("Type"))
		{
			old = new TypeNode(old);
			
			System.out.println(old.getClass() + " " + old.getValue());
		}
		else if(old.getType().equals("DimList"))
		{
			if(old.getChildren().size()>1)
			{
				old = new DimListNode(old);
			}
			else
			{
				old = new DimNode(old);
			}
			System.out.println(old.getClass() + " " + old.getValue());
		}
		else if(old.NumTest(old.getValue()))
		{
			
				if(!old.getParent().getType().equals("DimList"))
				{
					if(!old.getType().equals("Rop"))
					{
						old = new NumNode(old);
					}
				}
			
			
			System.out.println(old.getClass() + " " + old.getValue());
		}
		else if(old.getName().equals("Cdcl"))
		{
			if(old.getType().equals("ClassDcl"))
			{
				old = new ClassNode(old);
			}
			else if(old.getType().equals("ClassList"))
			{
				old = new ClassListNode(old);
			}
			System.out.println(old.getClass() + " " + old.getValue());
		}
		else if(old.getType().equals("Program"))
		{
			old = new ProgNode(old);
			System.out.println(old.getClass() + " " + old.getValue());
		}
		else if(old.getName().equals("Fpara") || old.getName().equals("Fparat"))
		{
			if(old.getType().equals("ParamList"))
			{
				old = new ParamListNode(old);
			}
			else if(old.getType().equals("Fparam"))
			{
				old = new ParamNode(old);
			}
			
			System.out.println(old.getClass() + " " + old.getValue());
			
		}
		else if(old.getType().equals("MembDcl"))
		{
			if(old.getChildType("ParamList") != null)
			{
				old = new FuncDefNode(old);
			}
			else if(old.getChild("Fpara") == null)
			{
				old = new VarDeclNode(old);
			}
				
		}
		else if(old.getName().equals("AD"))
		{
			if(old.getType().equals("MemberList"))
			{
				//old = new MemberListNode(old);
			}
			else if(old.getChild("Fpara") == null)
			{
				old = new VarDeclNode(old);
			}
			
			System.out.println(old.getClass() + " " + old.getValue());
			
		}
		else if(old.getType().equals("VarEl"))
		{
			if(old.getChild("Apara") != null)
			{
				old = new FuncCallNode(old);
			}
			else
			{
				old = new NumNode(old);
			}
			
			System.out.println(old.getClass() + " " + old.getValue());
		}
		else if(old.getType().equals("Term"))
		{
			if(old.AddTest(old.getValue()))
			{
				old = new AddOpNode(old);
			}
			else if(old.MultTest(old.getValue()))
			{
				old = new MultOpNode(old);
			}
			else if(old.AddTest(old.getParent().getValue()))
			{
				old = new NumNode(old);
			}
			else if(old.MultTest(old.getParent().getValue()))
			{
				old = new NumNode(old);
			}
			else if(old.getLC().getType().equals("ID"))
			{
				old = new NumNode(old);
			}
			System.out.println(old.getClass() + " " + old.getValue());
		}
		else if(old.getType().equals("InL"))
		{
			old = new IndexListNode(old);
			
			System.out.println(old.getClass() + " " + old.getValue());
		}
		else if(old.getType().equals("StatORVar"))
		{
			if(old.getChildType("DimList") != null)
			{
				old = new VarDeclNode(old);
			}
			else if(old.EqTest(old.getValue()))
			{
				old = new AssignStatNode(old);
			}
			else if(old.ReturnTest(old.getValue()))
			{
				old = new ReturnStatNode(old);
			}
			else if(old.GetTest(old.getValue()))
			{
				old = new GetStatNode(old);
			}
			else if(old.PutTest(old.getValue()))
			{
				old = new PutStatNode(old);
			}
			else if(old.IfTest(old.getValue()))
			{
				old = new IfNode(old);
				System.out.println(old.getClass() + " " + old.getValue());
			}
			else if(old.ForTest(old.getValue()))
			{
				old = new ForNode(old);
				System.out.println(old.getClass() + " " + old.getValue());
			}
		}
		else if(old.getName().equals("Fdef"))
		{
			if(old.getType().equals("FuncDefList"))
			{
				old = new FuncDefListNode(old);
			}
			else if(old.getType().equals("FuncDef"))
			{
				old = new FuncDefNode(old);
			}
			System.out.println(old.getClass() + " " + old.getValue());
		}
		else if(old.getType().equals("Stat"))
		{
			if((int)old.getValue().charAt(0) == 61)
			{
				old = new AssignStatNode(old);
				
				System.out.println(old.getClass() + " " + old.getValue());
			}
		}
		else if(old.getType().equals("ProgBlock"))
		{
			System.out.println(old.getParent().getName());
			
			old = new ProgramBlockNode(old);
			
			System.out.println(old.getClass() + " " + old.getValue());
		}
		else if(old.getType().equals("StatBlock"))
		{
			System.out.println(old.getParent().getName());
			
			old = new StatBlockNode(old);
			
			old.printBottom();
			old.getParent().printBottom();
			System.out.println(old.getClass() + " " + old.getValue());
			
			//System.exit(0);
			
		}
		else if(old.getType().equals("Asop"))
		{
			old = new AssignStatNode(old);
			System.out.println(old.getClass() + " " + old.getValue());
		}
		else if(old.getType().equals("Expr"))
		{
			if(old.getChildType("Aparam") != null)
			{
				old = new FuncCallNode(old);
			}
			else if(old.AddTest(old.getValue()))
			{
				old = new AddOpNode(old);
			}
			else if(old.MultTest(old.getValue()))
			{
				old = new MultOpNode(old);
			}
			else if(old.getParent().getType().equals("Aparam"))
			{
				old = new NumNode(old);
			}
			
			
			System.out.println(old.getClass() + " " + old.getValue());
		}
		else if(old.getType().equals("Rexpr"))
		{
			old = new RelNode(old);
			System.out.println(old.getClass() + " " + old.getValue());
		}
		else if(old.getType().equals("Fact"))
		{
			if(old.AddTest(old.getValue()))
			{
				old = new AddOpNode(old);
			}
			else if(old.MultTest(old.getValue()))
			{
				old = new MultOpNode(old);
			}
			else if(old.AddTest(old.getParent().getValue()))
			{
				if(old.getChildType("VarEl") == null)
				{
					if(old.getChildType("Term") == null)
					{
						old = new NumNode(old);
					}
				}
				
			}
			else if(old.MultTest(old.getParent().getValue()))
			{
				if(old.getChildType("VarEl") == null)
				{
					old = new NumNode(old);
				}
			}
			System.out.println(old.getClass() + " " + old.getValue());
		}
		else if(old.AddTest(old.getValue()))
		{
			old = new AddOpNode(old);
			System.out.println(old.getClass() + " " + old.getValue());
		}
		
		if(old.getParent() != null)
		{
			Switch(Parent, old, pos);
		}
		return old;
		
	}
	
	public void printTypes(AST_Node head)
	{
		System.out.println(head.getClass() + " " + head.getName() + " " + head.getValue());
		for(int i=0; i<head.getChildren().size(); i++)
		{
			printTypes(head.getChildren().get(i));
		}
	}
	
	
	public static void main(String args[])
	{
		Parser p = new Parser();
		Transform t = new Transform();
		
		p.setFile("test.txt");
		
		System.out.println(p.Parse());
		
		AST_Node nw = t.transform(p.getDTree(), null, 0);
		
		t.printTypes(nw);
		
	}
	
}

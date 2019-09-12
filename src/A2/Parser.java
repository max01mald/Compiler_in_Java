package A2;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

import A1.ScannerC;
import A1.Token;
import AST_Tree.AST_Node;
import AST_Tree.ID;
import AST_Tree.Operation;
import AST_Tree.Transform;

public class Parser 
{
	private String ParseFile;
	private AST_Node DerivationTree;
	
	public Parser()
	{
		ParseFile = "";
		DerivationTree = null;
	}
	
	public String getFile()
	{
		return ParseFile;
	}
	
	public void setFile(String File)
	{
		ParseFile = File;
	}
	
	public AST_Node getDTree()
	{
		return DerivationTree;
	}
	
	public void setDTree(AST_Node Head)
	{
		DerivationTree = Head;
	}
	
	public boolean Parse()
	{
		Transform t = new Transform();
		
		boolean Error = false;
		
		String temp = "";
		String Grammar = "";
		String Transition = "";
		
		Operation OP = new Operation();
		
		int count = 0;
		
		Vector<Token> token = new Vector<Token>();
		
		PTable PT = new PTable();
		TreeT TT = new TreeT();
		
		PT.createTable();
		TT.createTable();
		
		Stack<String> Stack = new Stack<String>();
		Stack<String> TreeStack = new Stack<String>();
		
		Queue<String> TreeQueue = new LinkedList<String>();
		
		ScannerC Scan = new ScannerC();
		
		Scan.Initializer(this.ParseFile);
		
		Stack.push("$");
		TreeStack.push("$");
		
		Stack.push("Prog");
		TreeStack.push("Prog");
		
		AST_Node Head = new AST_Node("Prog");
		
		AST_Node Head2 = Head;
		
		
		Token Token = null;
		
		try
		{
			FileWriter fw2 = new FileWriter("ParsingErrors.txt");
			BufferedWriter bw2 = new BufferedWriter(fw2);
			
			bw2.write("");
			bw2.flush();
			
			
			FileWriter fw = new FileWriter("Derivation.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			
			while(Token == null)
			{
				Token = Scan.nextToken();
			}
			
			while(Stack.peek() != "$")
			{
				temp = Stack.peek();
				
				if(Token == null)
				{
					if(Stack.peek().equals("SCOL"))
					{
						bw2.write("Syntax error at " + Scan.getLocation() + " Token " + Stack.peek() + " is missing"+"\n" );
						bw2.flush();
						
						Stack.pop();
						System.out.println("SCOL POP");
						
						while((!Stack.peek().equals(TreeStack.peek())))
						{
							TreeStack.pop();
						}
						
						break;
					}
				}
				
				
				while(Token.getType().equals("COMM") || Token.getType().equals("SCOMM"))
				{
					Token = null;
					
					while(Token == null)
					{
						Token = Scan.nextToken();
						count += 1;
						
						if(count > 100)
						{
							break;
						}
						
					}
				}
				
				if(!token.isEmpty())
				{
					if((Token != token.get(token.size()-1)))
					{
						if(TokenTest(Token))
						{
							token.add(Token);
						}
					}
				}
				else
				{
					token.add(Token);
				}
				while(scolCount(Stack, TreeStack))
				{
					if((int)TreeStack.peek().charAt(0)>96)
					{
						
						
						if(true)
						{
							System.out.println("ENTER TRANSITION TERMINAL");
							
							Transition = TreeStack.pop();
							System.out.println(Transition);
							TreeQueue = TT.getTransition(Transition);
							
							System.out.println(Head2);
							Head2 = this.Transition_to_Node(Head2, TreeQueue);
							Head2.setLocation(Token.getLocation());
							System.out.println("in SCOL runner");
							printStack(TreeStack);
							//System.out.println("Head after Terminal Translation " + Head2.getName() + " " + Head2.getType());
						}
					}
					else if(TreeStack.peek().equals("SCOL"))
					{
						TreeStack.pop();
					}
					else if(TreeStack.peek().equals("CCBR"))
					{
						TreeStack.pop();
					}
					else if(TreeStack.peek().equals("CBR"))
					{
						TreeStack.pop();
					}
					else
					{
						TreeStack.pop();
					}
				}
				
				
				if(PT.LookTerminal(temp) != -1)
				{
					
					System.out.println("THIS IS THE TEMP " + temp + " " + Token.getType());
					printStack(TreeStack);
					Head.setLocation(Token.getLocation());
					//System.out.println("Stack top: " + temp + " " + "Token: " + Token.toString());
					
					if(temp.equals(Token.getType()))
					{
						Head.setLocation(Token.getLocation());
						Head2.setLocation(Token.getLocation());
						Head2.printBottom();
						System.out.println(Head2.getLocation());
						
						
						if(Head2 != null)
						{	
							Head2.setLocation(Token.getLocation());
							
							if(Token.getType().equals("ID"))
							{
								System.out.println("Found ID " + Token.getValue() + " " + Head.getName());
								
								AST_Node Temp = null;
								
								Head.printBottom();
								Head.printSiblings();
								
								Temp = Head.getChild("ID");
								
								if(Temp == null)
								{
									Temp = Head.getSibling("ID");
								}
								
								Temp.setValue(Token.getValue());
								
								
								System.out.println("Founf ID Head: " + Temp.getName() + " " + Temp.getValue());
								
								
							}
							else if(Token.getType().equals("INT"))
							{
								System.out.println("Found Int " + Token.getValue() + "x " + Head.getName());
								
								AST_Node Temp = null;
								
								AST_Node INT = new AST_Node();
								
								if(Head2.getChild("INT") != null)
								{
									System.out.println("Right head found Head2 Asiz");
									INT = Head2.getChild("INT");
									INT.setValue(Token.getValue());
								}
								
								Head2.printBottom();
							}
							else if(Token.getType().equals("FLOAT"))
							{
								System.out.println("Found float " + Token.getValue() + "x " + Head.getName());
								
								AST_Node Temp = null;
								
								AST_Node INT = new AST_Node();
								
								if(Head2.getChild("FLOAT") != null)
								{
									System.out.println("Right head found Head2 Asiz");
									INT = Head2.getChild("FLOAT");
									INT.setValue(Token.getValue());
									
									System.out.println(INT.getValue());
									
								}
								
								Head2.printBottom();
							}
							else if(OP.isOP(Token.getType()))
							{
								//System.out.println("Found OP " + Token.getValue() + "x " + Head2.getName());
								
								AST_Node Temp = null;
								
								AST_Node Op = new AST_Node();
								
								if(Head2 != null)
								{
									Head2.setValue(Token.getValue());
									Head2.setType(Head2.getName());
								}
								
								Head2.setLocation(Token.getLocation());
							}
						}
						Stack.pop();
						
						System.out.println("ENTER AT TERMINAL: ");
						System.out.println("TREESTACK");
						printStack(TreeStack);
						printStack(Stack);
						
						
						if((int)TreeStack.peek().charAt(0)>96)
						{
							
							if(true)
							{
								System.out.println("ENTER TRANSITION TERMINAL");
								
								Transition = TreeStack.pop();
								System.out.println(Transition);
								TreeQueue = TT.getTransition(Transition);
								Head2 = this.Transition_to_Node(Head2, TreeQueue);
								Head2.setLocation(Token.getLocation());
							}
						}
						
						System.out.println("TERMINAL POP: " + TreeStack.peek());
						printStack(TreeStack);
						printStack(Stack);
						
						if(TreeStack.peek().charAt(0) == 'm')
						{
							System.out.println("Skip pop for make");
						}
						else if((int)TreeStack.peek().charAt(0)>96)
						{
							System.out.println("Skip pop for transition");
						}
						else
						{
							System.out.println("Pop at the Terminal POp");
							TreeStack.pop();
						}
						
						
						Token = null;
						
						while(Token == null)
						{
							Token = Scan.nextToken();
							count += 1;
							
							if(count > 100)
							{
								break;
							}
							
						}
						//token.add(Token);
						count = 0;
						
					}
					else
					{
						System.out.println("Skipping error");
						printStack(Stack);
						
						System.out.println(PT.LookTerminal(temp));
						System.out.println("TEMP " + temp + " " + Token.getType());
						System.out.println(Token.getValue());
						
						
						Token = this.skipErrors(Token, Stack, TreeStack, Scan);
						
						//Head2 = this.Transition_to_Node(Head2, q) 
						
						
						
						Error = true;
						
					
					}
				}
				else
				{
					printStack(TreeStack);
					if(PT.LookParseT(PT.LookGRule(temp), Token.getType()) <= PT.getRefT().length)
					{
						
						if(Head.getParent() != null)
						{
							System.out.println("Parent " + Head.getParent().getName());
						}
						else
						{
							System.out.println("parent is null");
						}
						
						if(!Stack.peek().equals(TreeStack.peek()))
						{
							if(TreeStack.peek().charAt(0) == 'm')
							{
								printStack(TreeStack);
								printStack(Stack);
								Transition = TreeStack.pop();
								System.out.println("TransitionZ " + Transition);
								
								TreeQueue = TT.getTransition(Transition);
								
								System.out.println();
								Head2 = this.Transition_to_Node(Head2, TreeQueue);
								Head2.setLocation(Token.getLocation());
								System.out.println("New Head " +Head2.getName());
							}
						}
						
						if(Head != Head2)
						{
							Head = Head2;
							Head.setLocation(Token.getLocation());
						}
						
						Head = this.moveToNode(Head, Stack.peek());
						//Head.setLocation(Token.getLocation());
						
						if(Head == null)
						{
							Head = Head2;
						}
						
						System.out.println("MOVED HERE " + Head.getName());
						
						Head.printBottom();
						Head.printSiblings();
						
						bw.write(Head.getName()+" ");
						Stack.pop();
						
						printStack(Stack);
						
						System.out.println("BEFOR GRAMMAR BUILDER " + temp + " " + Token.getType());
						
						if(!Token.getType().equals("ERR"))
						{
							
							Grammar = PT.getGrammar(temp, Token.getType());
						}
						
						System.out.println(Grammar);
						
						if((!Grammar.equals("EPSILON")))
						{
							System.out.println("hello");
							Head = this.buildTree(Head, Grammar, Token);
							Head.setLocation(Token.getLocation());
							Head2.setLocation(Token.getLocation());
							
						}
						
						Head.printBottom();
						
						if(TreeStack.peek().equals("makeInt"))
						{
							Head2 = Head.getParent();
							Head2.setLocation(Token.getLocation());
						}
						
						
						System.out.println("TREESTACK");
						printStack(TreeStack);
						printStack(Stack);
						System.out.println("BEFORE CHECK: " + TreeStack.peek());
						
						if(!((int)TreeStack.peek().charAt(0)>96))
						{
							System.out.println("POP " + TreeStack.peek());
							TreeStack.pop();
							System.out.println("NEW TOP: " + TreeStack.peek());
						}
						
						
						if((int)TreeStack.peek().charAt(0)>96)
						{
							printStack(TreeStack);
							printStack(Stack);
							System.out.println("ENTER CHECK: " + TreeStack.peek());
							Transition = TreeStack.pop();
							printStack(TreeStack);
							
							System.out.println("Transition " + Transition);
							TreeQueue = TT.getTransition(Transition);
							
							Head2 = Transition_to_Node(Head2, TreeQueue);
							Head2.setLocation(Token.getLocation());
							System.out.println("New Head " + Head2.getName());
							System.out.println();
							
							
						}
						
						System.out.println("AFTER POP: " + TreeStack.peek());
						
						System.out.println("TREESTACK");
						printStack(TreeStack);
						printStack(Stack);
						System.out.println("GRAMMAR: " + Grammar);
						System.out.println("STACK: ");
						printStack(Stack);
						
						
						
						while(!Stack.peek().equals(TreeStack.peek()))
						{
							
							if((int)TreeStack.peek().charAt(0)>96)
							{
								printStack(TreeStack);
								printStack(Stack);
								System.out.println("ENTER CHECK: " + TreeStack.peek());
								Transition = TreeStack.pop();
								printStack(TreeStack);
								
								System.out.println(Transition);
								
								System.out.println("Transition " + Transition);
								
								TreeQueue = TT.getTransition(Transition);
								
								Head2 = this.Transition_to_Node(Head2, TreeQueue);
								Head2.setLocation(Token.getLocation());
								System.out.println("New Head " + Head2.getName());
								
								
								System.out.println(Scan.getLocation());
							}
							else if(TreeStack.peek().charAt(0) == 'W')
							{
								System.out.println("WAAAAIT");
								
								System.out.println("STACK");
								printStack(Stack);
								System.out.println("TREESTACK");
								printStack(TreeStack);
								TreeStack.pop();
								System.out.println("TreeStack Grammar " + TT.searchTG(temp, Grammar));
								//System.in.read();
								
								System.out.println(topGrammar(Grammar)+"x");
								
								System.out.println("WAAAAIT DONE");
								
								System.out.println(temp);
								System.out.println("STACK");
								printStack(Stack);
								System.out.println("TREESTACK");
								printStack(TreeStack);
								
								
								System.out.println("TreeStack Grammar " + TT.searchTG(temp, Grammar));
								//System.in.read();
								
								
								break;
							}
							else
							{
								System.out.println("While POP");
								printStack(TreeStack);
								TreeStack.pop();
							}
							
						}
						
						Head2.setLocation(Token.getLocation());
						
						System.out.println(temp);
						
						PT.InsertReverse(Grammar, Stack);
						
						System.out.println("TEMP " + temp + " Grammar " + Grammar + " Type " + Token.getType());
						
						TT.InsertReverse(temp, Grammar, TreeStack);
						
						printStack(Stack);
						printStack(TreeStack);
					}
					else
					{
						System.out.println("Error");
						printStack(Stack);
						System.out.println(PT.LookGRule(temp));
						System.out.println(PT.LookParseT(PT.LookGRule(temp), Token.getType()));
						System.out.println(Token.getType()+"x");
						System.out.println(temp);
						
						
						Token.setCode(PT.LookParseT(PT.LookGRule(temp), Token.getType()));
						Token = this.skipErrors(Token, Stack, TreeStack, Scan);
						
						
						
						Error = true;
						
					}
				}
				
			}
			
			if(Stack.peek().equals("$"))
			{
				while(!(TreeStack.peek().equals("$")))
				{
					if((int)TreeStack.peek().charAt(0)>96)
					{
						
						if(true)
						{
							System.out.println("ENTER TRANSITION TERMINAL");
							
							Transition = TreeStack.pop();
							printStack(TreeStack);
							System.out.println(Transition);
							TreeQueue = TT.getTransition(Transition);
							System.out.println("Transition " + Transition);
							
							Head2 = this.Transition_to_Node(Head2, TreeQueue);

							
							
							System.out.println("New Head " + Head2.getName());
							
							//System.out.println("Head after Terminal Translation " + Head2.getName() + " " + Head2.getType());
						}
					}
					if(TreeStack.peek().equals("SCOL"))
					{
						TreeStack.pop();
					}
				}
			}
			
			
			
			System.out.println(token.size());
			
			for(int i=0; i<token.size(); i++)
			{
				if(token.get(i) == null)
				{
					token.remove(i);
				}
				
				System.out.println(token.get(i).getType());
			}
			
			for(int i=0; i<token.size();i++)
			{
				System.out.println(token.get(i));
			}
			
			this.TreeTraversal(Head2.TreeParent(), token);
			
			//System.exit(0);
			
			this.DerivationTree = t.transform(Head2.TreeParent(), null, -1);
			
			
			
			
			
			bw2.close();
			bw.flush();
			bw.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Error finding the file, program terminating");
		}
		catch(IOException e2)
		{
			System.out.println("Error handeling the file, program terminating");
		}
		
		System.out.println(Token);
		
		if((Token != null) || Error == true)
		{
			return false;
		}
		else
		{
			return true;
		}
		
	}
	
	public boolean TokenTest(Token token)
	{
		boolean check = false;;
		
		if(token.getType().equals("ID")||token.getType().equals("INTDC")||token.getType().equals("FLOATDC"))
		{
			check = true;
		}
		else if(token.getType().equals("CLASS")||token.getType().equals("INT")||token.getType().equals("FLOAT"))
		{
			check = true;
		}
		return check;
	}
	
	public void TreeTraversal(AST_Node p_node, Vector<Token> token)
	{
		int count = 0;
		for(AST_Node node : p_node.getChildren())
		{
			node.printBottom();
			
			if(token.size() == 1)
			{
				node.setLocation(token.get(0).getLocation());
				
				TreeTraversal(node, token);
				
				return;
			}
			
			if(node.getName().equals("ID")|| node.getType().equals("ID")||node.getName().equals("Type")||node.getName().equals("CLASS"))
			{
				
				token.remove(0);
				node.setLocation(token.get(0).getLocation());
				
				
				
				
			}
			else if(node.NumTest(node.getValue().trim()))
			{
				//||node.getName().equals("INT")||node.getName().equals("FLOAT"))

				token.remove(0);
				node.setLocation(token.get(0).getLocation());
				
				System.out.println(node.getName());
				System.out.println(node.getType());
				System.out.println(node.getLocation());
				
				TreeTraversal(node, token);
			}
			else if(node.EqTest(node.getValue().trim())||node.AddTest(node.getValue().trim())||node.MultTest(node.getValue().trim()))
			{
				token.remove(0);
				node.setLocation(token.get(0).getLocation());
				
				System.out.println(node.getName());
				System.out.println(node.getType());
				System.out.println(node.getLocation());
				
				TreeTraversal(node, token);
				
			}
			
			if(count == 0)
			{
				node.setLocation(token.get(0).getLocation());
				
				System.out.println(node.getName());
				System.out.println(node.getType());
				System.out.println(node.getLocation());
				TreeTraversal(node, token);
			}
			
		}
		
	}
	
	public boolean scolCount(Stack<String> Stack, Stack<String>  TreeStack)
	{
		int count = 0;
		int count2 = 0;
		
		int count3 = 0;
		int count4 = 0;
		
		int count5 = 0;
		int count6 = 0;
		
		for(int i=0; i<Stack.size(); i++)
		{
			if(Stack.get(i).equals("CCBR"))
			{
				count +=1;
			}
			
			if(Stack.get(i).equals("SCOL"))
			{
				count3 += 1;
			}
			
			if(Stack.get(i).equals("CBR"))
			{
				count5 += 1;
			}
		}
		
		for(int i=0; i<TreeStack.size(); i++)
		{
			if(TreeStack.get(i).equals("CCBR"))
			{
				count2 +=1;
			}
			
			if(TreeStack.get(i).equals("SCOL"))
			{
				count3 += 1;
			}
			
			if(TreeStack.get(i).equals("CBR"))
			{
				count6 += 1;
			}
		}
		
		if(count < count2 || count3 < count4 || count5 < count6)
		{
			return true;
		}
		
		
		return false;
	
		
	}
	
	public String topGrammar(String Grammar)
	{
		
		for(int i=0; i<Grammar.length(); i++)
		{
			if(Grammar.charAt(i) == ' ')
			{
				return Grammar.substring(0, i);
			}
		}
		return "";
	}
	
	public int StackSCOLcount(Stack stack)
	{
		int count = 0;
		
		for(int i=0; i<stack.size(); i++)
		{
			if(stack.get(i).equals("SCOL"))
			{
				count +=1;
			}
		}
		
		return count;
	}
	
	public AST_Node makeNode(String name)
	{
		AST_Node Temp = new AST_Node(name);
		
		return Temp;
	}
	
	public AST_Node buildTree(AST_Node Head, String Grammar, Token token)
	{
		
		
		int count = 0;
		int beg = 0;
		int end = 0;
		
		int count2 = 0;
		
		AST_Node Siblings = null;
		AST_Node Temp = null;
		
		System.out.println("Grammar in tree builder " + Grammar);
		
		if(Grammar.charAt(0) == 'R')
		{
			if(Head.getName().equals("Stmt"))
			{
				Head.printBottom();
				
			}
		}
		
		
		Head.printBottom();
		
		if(Grammar.charAt(0) == 'O')
		{
			if((!Head.getType().equals("")))
			{
				System.out.println("Grammar " + Grammar);
				Head.printSiblings();
				Head.printBottom();
				Head.getParent().printSiblings();
				
				Head = Head.getParent();
				
				AST_Node Right = Head;
				
				while(Right.getRSib() != null)
				{
					if(Right.getRSib() != null)
					{
						Right = Right.getRSib();
						
						if(Right.getName().equals(Head.getName()))
						{
							
							Head = Right;
							
							break;
						}
					}
				}
				
				System.out.println("NEW HEAD IN BUILDER");
				Head.printBottom();
				
				
			}
		}
		
		
		if(Head.getChild(Head.getName()) != null)
		{
			System.out.println("Moving Head");
			
			Head = Head.getChild(Head.getName());
			
			
		}
		
		
		
		for(int i=0; i<Grammar.length(); i++)
		{
			if(Grammar.substring(i,i+1).equals(" "))
			{
				end = i;
				
				
				if(count2 == 0)
				{
					Temp = 
					
					Siblings = makeNode(Grammar.substring(beg, end));
					
					count2 += 1;
				}
				else
				{
					
					Temp = makeNode(Grammar.substring(beg, end));
					
					Siblings = Siblings.makeSibling(Temp);
					
				}
				
				beg = end+1;
				count += 1;
			}
			else if(Grammar.length() == i+1)
			{
				end = i+1;
				if(count2 != 0)
				{
					Siblings = Siblings.makeSibling(makeNode(Grammar.substring(beg, end)));
				}
				else
				{
					Siblings = makeNode(Grammar.substring(beg, end));
				}
			}
			
		}
		
		
		Head.printBottom();
		Siblings.printBottom();
		
		
		
		Head = (Head.adoptChildren(Siblings));
		
		if(Head.getName().equals("CCBR"))
		{
			System.out.println("hello");
			
		}
		
		
		System.out.println("End of tree builder + bottom");
		Head.printBottom();
		
		if(Head.getParent() != null)
		{
			Head.getParent().printBottom();
		}
		
		return Head;
	}
	
	public AST_Node Transition_to_Node(AST_Node Head, Queue<String> q)
	{
		if(Head == null)
		{
			return null;
		}
		AST_Node Head2 = new AST_Node(Head);
		AST_Node test = null;
		
		
		if(q.peek().equals("if Sblok"))
		{
			Head.printSiblings();
			Head.printBottom();
			
			System.out.println(Head.getLC());
			
			try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(Head.getLC() == null)
			{
			
			System.exit(0);
			}
			else 
			{
				q.clear();
				q.add("Sblok");
				q.add("SblokP");
				
			}
			
		}
		
		boolean check = false;
		
		System.out.println();
		
		if(Head.getName() != null)
		{
			System.out.println("INSIDE Transition, Head: " + Head.getName() + " " + Head);
			Head.printSiblings();
			Head.printBottom();
			
			if(Head.getParent() != null)
			{
				System.out.println("Parent " + Head.getParent().getName());
			}
			if(Head.getParent() != null)
			{
				Head.getParent().printSiblings();
				Head.getParent().printBottom();
			}
		}
		else
		{
			System.out.println("INSIDE: NULLLLLL");
		}
		
		
		TreeT T = new TreeT();
		
		System.out.println("The Queue in the Transition");
		T.printQueue(q);
		System.out.println();
		
		System.out.println("INSIDE QUEUE TOP: " + q.peek());
		
		if(Head.getName().equals(q.peek()))
		{
			q.poll();
		}
		else if(!Head.getName().equals(q.peek()))
		{
			/*try {
				System.in.read();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			
			while(!q.isEmpty())
			{
				q.poll();
				
				if(q.peek() == null)
				{
					return Head;
				}
				
				if(q.peek().equals("||"))
				{
					q.poll();
					if(Head.getName().equals(q.peek()))
					{
						q.poll();
						break;
					}
					
				}
			}
			
		}
		else return Head;
		
		System.out.println("INSIDE QUEUE TOP AFTER POP: " + q.peek());
		
		while(!q.isEmpty())
		{
			System.out.println("WHILE: " + q.peek());
			System.out.println("Head: " + Head.getName());
			
			if(q.peek().equals("&&"))
			{
				q.poll();
				System.out.println(q.peek());
				if(Head.getName().equals(q.peek()))
				{
					System.out.println("Success: " + q.peek());
					q.poll();
				}
				else return Head;
			}
			else if(q.peek().equals("||"))
			{
				System.out.println("|| " + check);
				if(!check)
				{
					q.poll();
					
				}
				else
				{
					return Head;
				}
			}
			else if(q.peek().length()<4 || q.peek().charAt(0) != 'm')
			{
				System.out.println("LESS 4 " +q.peek());
				System.out.println("HEAD " + Head.getName());
				
				test = Head;
				
				Head = moveToNode(Head, q.poll());
				
				
				check = true;
				
				if(Head == null)
				{
					Head = Head2;
				}
				
				System.out.println(Head.getName());
				
			}
			else if(!q.peek().substring(0,4).equals("make"))
			{
				System.out.println(q.peek().substring(0,4)+ " inside non make");
				
				test = Head;
				
				Head = moveToNode(Head, q.poll());
				
				if(Head != test)
				{
					check = true;
				}
				
			}
			else
			{
				AST_Node Head3 = new AST_Node(Head);
				
				System.out.println("MAKE " + Head.getName());
				
				/*try {
					System.in.read();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
				
				while(true)
				{
					System.out.println("Before transfrom " + Head + " Type " + Head.getType());
					Head3 = transformNode(Head, q.peek());
					System.out.println("After transform " + Head3 + " Type " + Head.getType());
					
					System.out.println("second");
					
					if(Head3 == null)
					{
						Head3 = new AST_Node(Head);
						
						q.poll();
						
						System.out.println("Null Returned and pop " + q.peek());
						
						if(q.peek() == null)
						{
							System.out.println("POPING OVER");
							return Head;
						}
						if(q.peek().equals("||"))
						{
							q.poll();
							
						}
						else if(q.isEmpty())
						{
							//return null;
						}
						else if((int)q.peek().charAt(0) < 91)
						{
							Head = Head.searchFunction(Head, q.peek());
							q.poll();
							
							return Head;
						}
					}
					else
					{
						System.out.println("Stopped Searching");
						break;
					}
				}
				
				System.out.println("before replace Parent " + Head.getParent() + " Head " + Head.getName() + " Head3 " + Head3.getName());
				
				
				Head.replaceNode(Head.getParent(), Head, Head3);
				
				if(Head.getParent() != null)
				{
					System.out.println("After Transform return " + Head3.getName() + " Type " + Head3.getType() + " parent " + Head.getParent().getName());
				
				
				
					Head3.printSiblings();
					
					System.out.println("Parenttt " + Head3.getParent());
					
					System.out.println("Replacing " + Head + " " + Head3 + " " + Head3.getType() + " " + Head.getParent().getChild(Head3.getName()));
					//System.out.println("Replacing " + Head.getType() + " " + Head3.getType() + " " + Head.getParent().getChild(Head3.getName()).getType());
				}
				if(Head3.getParent() == Head3)
				{
					System.out.println("Parent = Parent");
					System.exit(0);
				}
				
				System.out.println("End of Transform");
				Head3.printBottom();
				System.out.println(Head3.getName() + " " + Head3.getType());
				
				
				return Head3;
			}
			
			
			
		}
		
		
		
		return Head;
	}
	
	public AST_Node transformNode(AST_Node Head2, String Instruction)
	{
		AST_Node Head = new AST_Node(Head2);
		System.out.println("Transform Head: " + Head);
		
		AST_Node Node1 = new AST_Node();
		AST_Node Node2 = new AST_Node();
		AST_Node Node3 = new AST_Node();
		AST_Node Node4 = new AST_Node();
		AST_Node Node5 = new AST_Node();
		AST_Node Node6 = new AST_Node();
		AST_Node Node7 = new AST_Node();
		AST_Node Node8 = new AST_Node();
		
		ArrayList<AST_Node> Vec = new ArrayList<AST_Node>();
		
		String FuncN = "";
		String Target = "";
		
		Operation OP = new Operation();
		
		boolean check = false;
		
		int beg = 0;
		int end = 0;
		int count = 0;
		
		
		
		for(int i=0; i<Instruction.length(); i++)
		{
			if(Instruction.charAt(i) == '(')
			{
				end = i;
				FuncN = Instruction.substring(beg, end);
				beg = i+1;
				
			}
			else if(Instruction.charAt(i) == ',' || Instruction.charAt(i) == ')')
			{
				end = i;
				count += 1;
				Target = Instruction.substring(beg, end);
				beg = i+1;
				
				if(Target.charAt(0) == ' ')
				{
					Target = Target.substring(1, Target.length());
				}
				
				System.out.println("TARGET " + Target+"X");
				System.out.println(count);
			}
			
			if(Target.length()>0)
			{
				if(Target.charAt(0) == 'V' && Target.charAt(1) == 'e')
				{
					System.out.println("Searching for a Vector");
					
					int beg2 = 0;
					int end2 = 0;
					check = true;
					
					for(int j=0; j<Target.length(); j++)
					{
						if(Target.charAt(j) == '[')
						{
							beg2 = j+1;
						}
						else if(Target.charAt(j) == ']')
						{
							end2 = j;
						}
						
					}
					Target = Target.substring(beg2, end2);
					System.out.println("Head: " + Head.getName() + " " + Target);
					
					
					if(Target.equals("Fparam"))
					{
						Head.printBottom();
						Head.printSiblings();
						
						
						
					}
					
					
					Vec = Head.searchVector(Head, Target, Vec);
					
					System.out.println(Vec.size());
					
					Target = "";
				}
			}
			
			if(count>0)
			{
				//System.out.println("Inside count " + count);
				
				if(count == 1)
				{
					if(!Node1.hasName() && Target.length()>0)
					{
						System.out.println("Searching 1 " + Head.getName() + "x " + Target+"x");
						Head.printBottom();
						Head.printSiblings();
						
						
						Node1 = Node1.searchFunction(Head, Target);
						
						
						if(Node1 != null)
						{
							System.out.println(Node1.getName());
						}
						else return null;
						
					}
				}
				else if(count == 2 && Target.length()>0)
				{
					if(!Node2.hasName())
					{
						System.out.println("Searching 2 " + Head.getName() + "x " + Target+"x");
						Head.printBottom();
						Head.printSiblings();
						Node2 = Node2.searchFunction(Head, Target);
						
						if(Node2 != null)
						{
							System.out.println(Node2.getName());
						}
						else return null;
					}
				}
				else if(count == 3 && Target.length()>0)
				{
					if(!Node3.hasName())
					{
						System.out.println("Searching 3 " + Head.getName() + "x " + Target+"x");
						Head.printBottom();
						Head.printSiblings();
						Node3 = Node3.searchFunction(Head, Target);
						
						if(Node3 == Node2)
						{
							Node3 = Node2.getSibling("ID");
						}
						
						if(Node3 != null)
						{
							System.out.println(Node3.getName());
						}
						else return null;
					}
				}
				else if(count == 4 && Target.length()>0)
				{
					if(!Node4.hasName())
					{
						System.out.println("Searching 4 " + Head.getName() + "x " + Target+"x");
						Head.printBottom();
						Head.printSiblings();
						Node4 = Node4.searchFunction(Head, Target);
						
						if(Node4 != null)
						{
							System.out.println(Node4.getName());
						}
						else return null;
						
					}
				}
				else if(count == 5 && Target.length()>0)
				{
					if(!Node5.hasName())
					{
						System.out.println("Searching 5 " + Head.getName() + "x " + Target+"x");
						Head.printBottom();
						Head.printSiblings();
						Node5 = Node5.searchFunction(Head, Target);
						System.out.println(Node5.getName());
					}
				}
				else if(count == 6 && Target.length()>0)
				{
					if(!Node6.hasName())
					{
						System.out.println("Searching 6 " + Head.getName() + "x " + Target+"x");
						Head.printBottom();
						Head.printSiblings();
						Node6 = Node6.searchFunction(Head, Target);
						System.out.println(Node6.getName());
					}
				}
				else if(count == 7 && Target.length()>0)
				{
					if(!Node7.hasName())
					{
						System.out.println("Searching 7 " + Head.getName() + "x " + Target+"x");
						Head.printBottom();
						Head.printSiblings();
						Node7 = Node7.searchFunction(Head, Target);
						System.out.println(Node7.getName());
					}
				}
				else if(count == 8 && Target.length()>0)
				{
					if(!Node8.hasName())
					{
						System.out.println("Searching 8 " + Head.getName() + "x " + Target+"x");
						Head.printBottom();
						Head.printSiblings();
						Node8 = Node8.searchFunction(Head, Target);
						System.out.println(Node8.getName());
					}
				}
				
			}
			
			
		}
		
		if(FuncN.equals("makeNode"))
		{
			System.out.println("TARGET " + Target+"x");
			if(Target.equals("ID"))
			{
				String ID_Val = Node1.getValue();
				
				ID id = new ID(ID_Val);
				System.out.println("MADE ID NODE");
				
				Node1.setNode(Head);
				
				Head = Head.makeNode(id);
				Head.setNode(Node1);
				
				Head.setName("ID");
				Head.setType("ID");
				Head.setValue(ID_Val);
				
				Head = Head.isolate(Head);
				System.out.println("HEAD ID TYPE " + Head.getType() + Head.getName());
				
				return Head;
			}
			else if(Target.equals("INT"))
			{
				int Int = 0;
				String cut = "";
				cut = Node1.getValue();
				
				
				AST_Node Temp = Node1.getParent();
				AST_Node Temp2 = Temp.getParent();
				
				if(cut.equals(""))
				{
					System.out.println(Node1.getName());
					cut = Node1.getValue();
				}
				
				for(int i=0; i<cut.length(); i++)
				{
					if(cut.charAt(i) == ' ')
					{
						cut = cut.substring(0, i);
					}
					
				}
				System.out.println("Head " + Head.getName() + " Node1 " + Node1.getName());
				System.out.println(Head.getValue() + " " + Node1.getValue());
				
				if(cut == "")
				{
					
				}
				else if(cut.charAt(0) == '0')
				{
					Int = 0;
				}
				else
				{
					Int = Integer.parseInt(cut);	
				}
				
				System.out.println(Head.getName() + " " + Node1.getName());
				
				Head.printSiblings();
				Node1.printSiblings();
				
				Head = Head.makeNode(Int);
				
				
				Head.setParent(Node1.getParent());
				
				
				
				System.out.println("HEAD ID TYPE " + Node1.getType());
				Head.getParent().printSiblings();
				Head.getParent().printBottom();
				
				
				
				Head = Head.isolate(Head);
				
				
				return Head;
			}
			else if(Target.equals("FLOAT"))
			{
				double flt = 0;
				String cut = "";
				cut = Node1.getValue();
				
				
				AST_Node Temp = Node1.getParent();
				AST_Node Temp2 = Temp.getParent();
				
				if(cut.equals(""))
				{
					System.out.println(Node1.getName());
					cut = Node1.getValue();
				}
				
				for(int i=0; i<cut.length(); i++)
				{
					if(cut.charAt(i) == ' ')
					{
						cut = cut.substring(0, i);
					}
					
				}
				System.out.println("Head " + Head.getName() + " Node1 " + Node1.getName());
				System.out.println(Head.getValue() + " " + Node1.getValue());
				
				
				if(cut.charAt(0) == '0')
				{
					flt = 0;
				}
				else
				{
					flt = Double.parseDouble(cut);	
				}
				
				System.out.println(Head.getName() + " " + Node1.getName());
				
				Head.printSiblings();
				Node1.printSiblings();
				
				Head = Head.makeNode(flt);
				Head.setParent(Node1.getParent());
				
				Head.setValue(cut);
				
				System.out.println("HEAD ID TYPE " + Node1.getType());
				Head.getParent().printSiblings();
				Head.getParent().printBottom();
				
				Head = Head.isolate(Head);
				
				return Head;
			}
			else if(OP.ismakeOP(Target))
			{
				
				Head.setValue(Node1.getValue());
				Head.setType(Target);
				Head.setValue(Node1.getValue());
				Head = Head.isolate(Head);
				
				Head.printBottom();
				
				
				return Head;
			}
		}
		else if(FuncN.equals("makeTID"))
		{
			return Head.makeTID(Node1);
		}
		else if(FuncN.equals("makeT"))
		{
			return Head.makeT(Node1);
		}
		else if(FuncN.equals("makeProg"))
		{
			return Head.makeProg(Node1);
		}
		else if(FuncN.equals("makeClassList"))
		{
			if((!Vec.isEmpty()))
			{
				return Head.makeClassList(Node1, Vec);
			}
			else
			{
				Head.abandonChild();
				Head.adoptChildren(Head.makeNode());
				Head.setType("ClassList");
			}
		}
		else if(FuncN.equals("makeFuncDefList"))
		{
			if((!Vec.isEmpty()))
			{
				return Head.makeFuncDefList(Node1, Vec);
			}
			else
			{
				Head.abandonChild();
				Head.adoptChildren(Head.makeNode());
				Head.setType("FuncDefList");
				
				Head.printBottom();
				
				Head.getParent().printBottom();
				//System.exit(0);
			}
		}
		else if(FuncN.equals("makeClassDcl"))
		{
			return Head.makeClassDcl(Node1, Node2, Node3, Node4);
		}
		else if(FuncN.equals("makeInherList"))
		{
			System.out.println(count + " " + Vec.size());
			
			if((!Vec.isEmpty()))
			{
				return Head.makeInherList(Node1, Vec);
			}
			else
			{
				return Head.makeInherList(Node1);
			}
			
			
			
		}
		else if(FuncN.equals("makeMemberList"))
		{
			return Head.makeMemberList(Node1, Vec);
		}
		else if(FuncN.equals("makeFuncDef"))
		{
			return Head.makeFuncDef(Node1, Node2, Node3);
		}
		else if(FuncN.equals("makeScopeSpec"))
		{
			if(count == 2)
			{
				System.out.println("Make it to the function");
				return Head.makeScopeSpec(Node1, Vec);
			}
			
		}
		else if(FuncN.equals("makeMemberDecl"))
		{
			if(count ==2)
			{
				return Head.makeMemberDecl(Node1, Node2);
			}
			else
			{
				//return Head.makeMemberDecl(Node1, Node2, Node3, Node4, Node5);
			}
			
		}
		else if(FuncN.equals("makeFparam"))
		{
			if(count == 1)
			{
				return Head.makeFparam(Node1);
			}
			else
			{
				return Head.makeFparam(Node1, Node2, Node3, Node4);
			}
		}
		else if(FuncN.equals("makeVarDecl"))
		{
			if(count == 2 && !Vec.isEmpty())
			{
				return Head.makeVar(Node1, Vec);
			}
			else if(count == 2)
			{
				return Head.makeVarDecl(Node1, Node2);
			}
			else 
			{
				System.out.println("here");
				
				System.out.println(Node1.getName());
				System.out.println(Node2.getName());
				System.out.println(Node3.getName());
				System.out.println(Node4.getName());
				
				return Head.makeVarDecl(Node1, Node2, Node3, Node4);
				
			}
		}
		else if(FuncN.equals("makeVarEl"))
		{
			return Head.makeVarEl(Node1, Node2);
		}
		else if(FuncN.equals("makeFDecl"))
		{
			return Head.makeFDecl(Node1, Node2, Node3, Node4);
		}
		else if(FuncN.equals("makeParamList"))
		{
			System.out.println(count);
			if(count == 1)
			{
				return Head.makeParamList(Node1);
			}
			else
			{
				System.out.println("MakeParam");
				return Head.makeParamList(Node1, Vec);
			}
		}
		else if(FuncN.equals("makeDimList"))
		{
			
			if((!Vec.isEmpty()))
			{
				return Head.makeDimList(Node1, Vec);
			}
			else
			{
				
				Head = Head.makeDimList(Node1);
				
				
				System.out.println("NULL NODE " + Head + " " + Head.getType());
				return Head;
			}
		}
		else if(FuncN.equals("makeStatBlock"))
		{
			if((!Vec.isEmpty()))
			{
				System.out.println(count);
				return Head.makeStatBlock(Node1, Vec);
			}
			else
			{
				System.out.println("hello");
				return Head.makeStatBlock(Node1);
			}
		}
		else if(FuncN.equals("makeFhea"))
		{
			if(count == 4)
			{
				System.out.println("Making FHea");
				return Head.makeFhea(Node1, Node2, Node3, Node4);
			}
			
		}
		else if(FuncN.equals("makeStatORVar"))
		{
			System.out.println(count);
			if(count == 4)
			{
				return Head.makeStatORVar(Node1, Node2, Node3, Node4);
				
			}
			else
			{
				return Head.makeStatORVar(Node1, Node2);
				
			}
		}
		else if(FuncN.equals("makeStat"))
		{
			return Head.makeStat(Node1, Node2);
		}
		else if(FuncN.equals("makeAssStat"))
		{
			return Head.makeAssStat(Node1, Node2, Node3, Node4);
		}
		else if(FuncN.equals("makeStatIF"))
		{
			System.out.println("IF");
			return Head.makeStatIF(Node1, Node2);
		}
		else if(FuncN.equals("makeStatFOR"))
		{
			return Head.makeStatFOR(Node1, Node2, Node3, Node4, Node5);
		}
		else if(FuncN.equals("makeStatKW"))
		{
			return Head.makeStatKW(Node1, Node2);
		}
		else if(FuncN.equals("makeExr"))
		{
			return Head.makeExr(Node1, Node2);
			
		}
		else if(FuncN.equals("makeAexr"))
		{
			if(count == 2)
			{
				System.out.println("Make AEXR");
				
				
				
				return Head.makeAexr(Node1, Node2);
			}
			else
			{
				return Head.makeAexr(Node1, Node2, Node3, Node4);
			}
		}
		else if(FuncN.equals("makeRelExr"))
		{
			return Head.makeRelExr(Node1, Node2);
		}
		else if(FuncN.equals("makeTerm"))
		{
			if(count == 2)
			{
				System.out.println("MAKE TERM");
				return Head.makeTerm(Node1, Node2);
			}
			else
			{
				return Head.makeTerm(Node1, Node2, Node3);
			}
		}
		else if(FuncN.equals("makeFact"))
		{
			System.out.println(count);
			
			if(count == 2)
			{
				return Head.makeFact(Node1, Node2);
			}
			else
			{
				return Head.makeFact(Node1, Node2, Node3);
			}
		}
		else if(FuncN.equals("makeVar") && !Vec.isEmpty())
		{
			return Head.makeVar(Node1, Vec);
		}
		else if(FuncN.equals("makeDmem"))
		{
			return Head.makeDmem(Node1, Node2, Node3);
		}
		else if(FuncN.equals("makeFcall"))
		{
			return Head.makeFcall(Node1, Node2, Node3);
		}
		else if(FuncN.equals("makeInL"))
		{
			if((!Vec.isEmpty()))
			{
				System.out.println("YOLO");
				return Head.makeInL(Node1, Vec);
			}
			else
			{
				System.out.println("YOLO2");
				return Head.makeInL(Node1);
			}
		}
		else if(FuncN.equals("makeApara") && !Vec.isEmpty())
		{
			return Head.makeApara(Node1, Vec);
		}

		return Head;
	}
	
	
	public AST_Node moveToNode(AST_Node Head, String name)
	{
		
		String direction = "";
		
		if(Head.getRSib() != null)
		{
			if(Head.getLSib().getName().equals(Head.getRSib().getName()))
			{
				System.out.println("TEEESTTT " + Head.getName() + " " + name);
				
				name = name+"R";
				
			}
		}
		
		if(Head.getName().equals("Fdef"))
		{
			if(name.equals("Fbod"))
			{
				if(Head.getLC() == null)
				{
					while((!Head.getParent().getName().equals("Prog")))
					{
						if(Head.getParent().getName().equals("Fdef"))
						{
							System.out.println("Travel UP");
							Head = Head.getParent();
							
							Head.printBottom();
						}
					}
					name = name+"S";
				}
			}
		}
		
//		if(Head.getName().equals("Sblok"))
//		{
//			if(Head.getRSib() != null)
//			{
//			if(Head.getRSib().getName().equals("ELSE"))
//			{
//				
//				Head.printBottom();
//				Head.printSiblings();
//				//Head = Head.getChild("Fparat");
//				System.out.println(name + " " + direction);
//				
//				if(!(Head.getLC().getName().equals("OCBR")))
//				{
//					System.exit(0);
//				}
////				if(name.equals("Sblok"))
////				{
////					if(name.charAt(name.length()-1) != 'P' && name.charAt(name.length()-1) != 'G' &&name.charAt(name.length()-1) != 'S' &&name.charAt(name.length()-1) != 'Q' && name.charAt(name.length()-1) != 'R' )
////					{
////						name = name + "R";
////						
////						Head.printBottom();
////						Head.printSiblings();
////						
////						
////					}
////				}
//			}
//			}
//		}
		
		
		if(Head.getName().equals("Ind") && name.equals("E"))
		{
			if(Head.getChild("E") == null && Head.getSibling("E") == null)
			{
				System.out.println("GUten tag");
				Head = Head.getParent();
			}
		}
		
		if(name.charAt(name.length()-1) == 'P' || name.charAt(name.length()-1) == 'G' ||name.charAt(name.length()-1) == 'S' ||name.charAt(name.length()-1) == 'Q' || name.charAt(name.length()-1) == 'R' )
		{
			direction = name.charAt(name.length()-1)+ "";
			name = name.substring(0, name.length()-1);
		}
		
		System.out.println("Direction " + direction);
		
		System.out.println("Moving to: Head " + Head.getName() + " String " + name);
		//System.out.println(Head.getSibling(name).getName());
		
		System.out.println("Siblings");
		Head.listSib();
		
		System.out.println("Bottom");
		Head.printBottom();
		
		System.out.println("SpaceCheck: " + name+"x");
		
		if(direction.equals("P"))
		{
			System.out.println("Move UP: Head " + Head.getName() + " " + name);
			AST_Node store = Head;
			Head = Head.moveUP(Head, name);
			
			
			return Head;
		}
		
		if(direction.equals("Q"))
		{
			System.out.println("Move UP to Term: Head " + Head.getName() + " " + name);
			Head = Head.moveUPTerm(Head, name);
			return Head;
		}
		
		if(direction.equals("R"))
		{
			System.out.println("Move Right: Head " + Head.getName() + " " + name);
			
			while(Head.getRSib() != null)
			{
				if(Head.getRSib() != null)
				{
					Head = Head.getRSib();
					
					if(Head.getName().equals(name))
					{
						return Head;
					}
				}
			}
			return Head;
		}
		
		if(Head.getName().equals("Hed") && name.equals("Hed"))
		{
			direction = "G";
		}
		
		if(direction.equals("G") && Head.getChild(name) == null)
		{
			
			return Head;
		}
		
		if(Head.getName().equals(name)  && direction.equals(""))
		{
			System.out.println("Move: Head " + Head.getName() + " " + name);
			return Head;
		}
		else if(Head.getChild(name) != null && (!direction.equals("P")) && (!direction.equals("S")))
		{
			System.out.println("Move: Child " + Head.getName()+ " " + name);
			
			return Head.getChild(name);
		}
		else if(Head.getSibling(name) != null && (!direction.equals("P")) && (!direction.equals("G")))
		{
			System.out.println("Move: Sib " + Head.getName() + " " + name);
			
			System.out.println("Sib " + Head.getSibling(name).getName()+ " " + name);
			return Head.getSibling(name);
		}
		else
		{
			Head = Head.getParent();
			System.out.println("TREE ");
			Head.printBottom();
			System.out.println(Head.getParent());
			
			while(true)
			{
				if(Head.getName().equals(name))
				{
					return Head;
				}
				else if(Head.getSibling(name) != null)
				{
					return Head.getSibling(name);
				}
				else if(Head.getChild(name) != null)
				{
					System.out.println("Move: Child " + Head.getName());
					return Head.getChild(name);
				}
				else
				{
					if(Head.getParent() != null)
					{
						
						System.out.println("MOVE TO PARENT");
						Head = Head.getParent();
						System.out.println("NEWW HEAD " + Head.getName());
						
						
						
						Head.printBottom();
						
					}
					else
					{
						return null;
					}
				}
			}
		}

	}
	
	public Token skipErrors(Token Token, Stack<String> Stack, Stack<String> TreeStack, ScannerC Scan)
	{
		boolean First = false;
		boolean Second = false;
		boolean Third = false;
		boolean Four = false;
		boolean check = false;
		
		String top = "";
		int count = 0;
		
		PTable PT = new PTable();
		
		PT.createTable();
		
		FirstFollowT FFT = new FirstFollowT();
		FFT.createTable();
		
		try
		{
			FileWriter fw = new FileWriter("ParsingErrors.txt", true);
			BufferedWriter bbw = new BufferedWriter(fw);
			PrintWriter bw = new PrintWriter(bbw);
			
			if(!Token.getType().equals("ERR"))
			{
				Token.setFinal(Token.getType());
			}
			
			if(Token.getCode() == 0 && PT.LookGRule(Stack.peek()) == -1)
			{
				Token.setCode(-1);
			}
			
			if(Stack.peek().equals("Asop"))
			{
				bw.write("Error missing Asop " + Scan.getLocation()+"\n");
				bw.flush();
				Stack.pop();
				
				
				TreeStack.pop();
				
				
				bw.close();
				return Token;
			}
			
			if(Token.getCode() == 109)
			{
				if(PT.LookGRule(Stack.peek())<0)
				{
					bw.write("Syntax error at " + Token.getLocation() + " Token " + Stack.peek() + " is missing"+"\n" );
					bw.flush();
				}
				else
				{
					bw.write("Syntax error at " + Token.getLocation() + " Grammar does not allow" +"\n");
					bw.flush();
				}
				
				Stack.pop();
				System.out.println("Error pop");
				
				printStack(Stack);
				printStack(TreeStack);
				
				while(!TreeStack.peek().equals(Stack.peek()))
				{
					TreeStack.pop();
				}
				
				
				bw.close();
				return Token;
			}
			else if(Token.getCode() == 110)
			{
				
				bw.write("Syntax error at " + Token.getLocation() + " Token " + Token.getValue() + " is missplaced"+"\n" );
				bw.flush();
				
				while((!FFT.inFirst(Stack.peek(), Token.getFinal())) && (FFT.inFirst(Stack.peek(), "EPSILON") && !FFT.inFollow(Stack.peek(), Token.getFinal())))
				{
					First = (!FFT.inFirst(Stack.peek(), Token.getFinal()));
					Second = (FFT.inFirst(Stack.peek(), "EPSILON"));
					Third = (!FFT.inFollow(Stack.peek(), Token.getFinal()));
					Four = (Second && Third);
					
					Token = this.ErrorToken(Token, Scan);
					
					First = (!FFT.inFirst(Stack.peek(), Token.getFinal()));
					Second = (FFT.inFirst(Stack.peek(), "EPSILON"));
					Third = (!FFT.inFollow(Stack.peek(), Token.getFinal()));
					Four = (Second && Third);
				}
				
				bw.close();
				return Token;
					
			}
			else if(Token.getCode() == -1)
			{
				if(PT.LookTerminal(Token.getType())> 0)
				{
					System.out.println(Token.getType());
					if(PT.LookGRule(Stack.peek())<0)
					{
						if(!Stack.peek().equals("PER"))
						{
							bw.write("Error Missing " + Stack.peek() + " " + Scan.getLocation()+"\n");
							bw.flush();
						}
						else
						{
							bw.write("Syntax error at " + Token.getLocation() + " failure in Idnest"+"\n" );
							bw.flush();
						}
					}
					else
					{
						bw.write("Syntax error at " + Token.getLocation() + " Grammar does not allow"+"\n" );
						bw.flush();
					}
					
					Stack.pop();
					System.out.println("ERROR POP");
					
					while(!TreeStack.peek().equals(Stack.peek()))
					{
						TreeStack.pop();
					}
					
					bw.close();
					return Token;
				}
				if(PT.LookGRule(Stack.peek())<0)
				{
					
					bw.write("Syntax error at " + Token.getLocation() + " Token " + Token.getValue() + " is missplaced"+"\n" );
					bw.flush();
				}
				else
				{
					bw.write("Syntax error at " + Token.getLocation() + " Grammar does not allow"+"\n" );
					bw.flush();
				}
				
				Token = this.ErrorToken(Token, Scan);
				
				if(Stack.peek() == Token.getFinal())
				{
					bw.close();
					return Token;
				}
				
			}
			bw.flush();
			bw.close();
			
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Error finding the file, program terminating"+"\n");
		}
		catch(IOException e2)
		{
			System.out.println("Error handeling the file, program terminating"+"\n");
		}
		return Token;
	}
	
	public Token ErrorToken(Token Token, ScannerC Scan)
	{
		int count = 0;
		
		Token = null;
		
		while(Token == null)
		{
			Token = Scan.nextToken();
			count += 1;
			
			if(count > 100)
			{
				break;
			}
		}
	
		count = 0;
		
		if(!Token.getType().equals("ERR"))
		{
			Token.setFinal(Token.getType());
		}
		
		return Token;
	}
	
	public void printStack(Stack Stack)
	{
		Stack<String> s2 = new Stack<String>();
		
		s2.addAll(Stack);
		
		while(!s2.isEmpty())
		{
			
			System.out.print(s2.pop()+" ");
			
		}
		System.out.println();
	}
	
	
	
	
	public static void main(String args[])
	{
		Parser p = new Parser();
		
		p.setFile("test.txt");
		
		System.out.println(p.Parse());
		
	}
}

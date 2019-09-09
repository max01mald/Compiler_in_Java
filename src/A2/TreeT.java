package A2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class TreeT 
{
	private String[][] TG;
	private String[][] TT;
	
	public TreeT()
	{
		TG = null;
		TT = null;
	}
	
	public String[][] getTG()
	{
		return TG;
	}
	
	public String[][] getTT()
	{
		return TT;
	}
	
	public void setTG(int Row)
	{
		TG = new String[Row][2];
	}
	
	public void setTT(int Row)
	{
		TT = new String[Row][2];
	}
	
	
	public void BuildTable()
	{
		FileReader fr = null;
		BufferedReader br = null;
		
		int countR = 0;
				
		try
		{
			fr = new FileReader("TransitionGrammar.txt");
			br = new BufferedReader(fr);
			
			while(br.readLine() != null)
			{
				countR += 1;
			}
			
			
			setTG(countR);
			
			br.close();
			fr.close();
			countR = 0;
			
			fr = new FileReader("TreeTraversal.txt");
			br = new BufferedReader(fr);
			
			while(br.readLine() != null)
			{
				countR += 1;
			}
			
			setTT(countR);
			
			br.close();
			fr.close();
			
		}
		catch(FileNotFoundException e2)
		{
			System.out.println("Error opening the file, Process will Terminate");
		}
		
		catch(IOException e)
		{
			System.out.println("Error opening the file, Process will Terminate");
		}
		
	}
	
	public void FillTable()
	{
		FileReader fr = null;
		BufferedReader br = null;
		
		int c = 0;
		int countR = 0;
		
		int beg = 0;
		int end = 0;
		String Temp = "";
		
		try
		{
			fr = new FileReader("TransitionGrammar.txt");
			
			
			while(c != -1)
			{
				
				c = fr.read();
				Temp += (char)c;
				
				end += 1;
				
				if((char)c == '>')
				{
					Temp = Temp.substring(beg, end-3);
					end = 0;
					
					getTG()[countR][0] = Temp;
					Temp = "";
				}
				
				if(c == 10)
				{
					Temp = Temp.substring(beg+1, end-1);
					end = 0;
					
					getTG()[countR][1] = Temp;
					Temp = "";
					countR += 1;
				}
				
				
				if(c == -1)
				{
					Temp = Temp.substring(beg+1, end-1);
					end = 0;
					
					getTG()[countR][1] = Temp;
					Temp = "";
				}
				
				
				
			}

			
			fr.close();
			countR = 0;
			c = 0;
			
			fr = new FileReader("TreeTraversal.txt");
			
			
			while(c != -1)
			{
				
				c = fr.read();
				Temp += (char)c;
				
				end += 1;
				
				if((char)c == '>')
				{
					Temp = Temp.substring(beg, end-3);
					end = 0;
					
					getTT()[countR][0] = Temp;
					Temp = "";
				}
				
				if(c == 10)
				{
					Temp = Temp.substring(beg+1, end-1);
					end = 0;
					
					getTT()[countR][1] = Temp;
					Temp = "";
					countR += 1;
				}
				
				
				if(c == -1)
				{
					Temp = Temp.substring(beg+1, end-1);
					end = 0;
					
					getTT()[countR][1] = Temp;
					Temp = "";
				}
				
				
				
			}
			
			fr.close();
			
		}
		catch(FileNotFoundException e2)
		{
			System.out.println("Error opening the file, Process will Terminate");
		}
		
		catch(IOException e)
		{
			System.out.println("Error opening the file, Process will Terminate");
		}
	}
	
	public void createTable()
	{
		BuildTable();
		FillTable();
	}
	
	public String searchTG(String Locator, String Transition)
	{
		String temp = "";
		
		for(int i=0; i<Transition.length(); i++)
		{
			if((int)Transition.charAt(i) == 32 || i==Transition.length()-1)
			{
				if(i==Transition.length()-1)
				{
					temp = Transition;
					break;
				}
				else
				{
					temp = Transition.substring(0, i);
					break;
				}
			}
		}
		
		System.out.println("Transition " + temp+ "x");
		
		for(int i=0; i<TG.length; i++)
		{
			if(TG[i][0].equals(Locator))
			{
				if(TG[i][1].length()>=temp.length())
				{
					if(TG[i][1].substring(0, temp.length()).equals(temp))
					{
						return TG[i][1];
					}
				}
			}
		}
		return null;
	}
	
	public Stack InsertReverse(String Locator, String Transition, Stack Stack)
	{
		Stack<String> Rev = new Stack<String>();
		
		System.out.println("Locator " + Locator + " Transition " + Transition);
		
		String Grammar = searchTG(Locator, Transition);
		
		System.out.println("REV: " + Grammar);
		
		String temp = "";
		
		int count = 0;
		int beg = 0;
		int end = 0;
		
		for(int i=0; i<Grammar.length(); i++)
		{
			if(Grammar.substring(i,i+1).equals(" "))
			{
				end = i;
				Rev.push(Grammar.substring(beg, end));
				beg = end+1;
				count += 1;
			}
			else if(Grammar.length() == i+1)
			{
				end = i+1;
				Rev.push(Grammar.substring(beg, end));
			}
			
		}
		
		while(!Rev.isEmpty())
		{
			if(!Rev.peek().equals("EPSILON"))
			{
				Stack.push(Rev.pop());
			}
			else
			{
				Rev.pop();
			}
		}
		
		return Stack;
	}
	
	public String searchTT(String Locator)
	{
		for(int i=0; i<TT.length; i++)
		{
			if(Locator.equals(TT[i][0]))
			{
				return TT[i][1];
			}
		}
		return null;
	}
	
	
	public Queue<String> getTransitionQueue(String Transition)
	{
		int beg = 0;
		int end = 0;
		
		Queue<String> queue = new LinkedList<String>();
		
		String Temp = "";
		
		for(int i=0; i<Transition.length(); i++)
		{
			if(Transition.charAt(i) == '=')
			{
				end = i -1;
				Temp = Transition.substring(beg, end);
				beg = i+2;
				
				queue.add(Temp);
			}
			
			if(Transition.charAt(i) == '&' || Transition.charAt(i) == '|' || Transition.length()-1 == i)
			{
				if(Transition.charAt(i) == '&' || Transition.charAt(i) == '|')
				{
					end = i;
					
					Temp = Transition.substring(beg, end);
					beg = end;
					
					queue.add(Temp);
					
					end = i+2;
					Temp = Transition.substring(beg, end);
					beg = end+1;
					i += 1;
					
					queue.add(Temp);
				}
				else
				{
					end = i+1;
					Temp = Transition.substring(beg, end);
					beg = end;
					
					queue.add(Temp);
				}
				
			}
			
		}
		return queue;
	}
	
	public Queue<String> getTransition(String Locator)
	{
		
		if(Locator.charAt(Locator.length()-1) == '|')
		{
			Locator = Locator.substring(0, Locator.length()-1);
		}
		
		String Temp = searchTT(Locator);
		
		return getTransitionQueue(Temp);
		
	}
	
	public void printQueue(Queue<String> queue)
	{
		Queue<String> q2 = new LinkedList<String>(queue);
		
		
		while(!q2.isEmpty())
		{
			if(!q2.isEmpty())
			{
				System.out.print(q2.poll()+" ");
			}
			else
			{
				System.out.println();
			}
		}
	}
	
	public void printTG()
	{
		for(int i=0; i<TG.length; i++)
		{
			for(int j=0; j<2; j++)
			{
				System.out.print(TG[i][j]+ " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void printTT()
	{
		for(int i=0; i<TT.length; i++)
		{
			for(int j=0; j<2; j++)
			{
				System.out.print(TT[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static void main(String args[])
	{
		TreeT T = new TreeT();
		
		T.createTable();
		
		//T.printTG();
		T.printTT();
		
		Queue<String> q = T.getTransition("makeStatORVar");
		
		T.printQueue(q);
		System.out.println();
		System.out.println(T.searchTG("Prog", "Cdcl "));
	}
	
}


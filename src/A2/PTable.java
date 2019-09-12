package A2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

import A1.ScannerC;

public class PTable 
{
	private String [][] ParseT;
	private String [][] RefT;
	
	PTable()
	{
		ParseT = null;
		RefT = null;
	}
	
	public String [][] getParseT()
	{
		return ParseT;
	}
	
	public String [][] getRefT()
	{
		return RefT;
	}
	
	public void setParseT(int Row, int Col)
	{
		ParseT = new String[Row][Col];
	}
	
	public void setRefT(int Row)
	{
		RefT = new String[Row][3];
	}
	
	public void BuildTable()
	{
		String line = "";
		int countR = 0;
		int countC = 0;
		
		try
		{
			FileReader fr = new FileReader("ParseTable.txt");
			BufferedReader br = new BufferedReader(fr);
			
			while(line != null)
			{
				line = br.readLine();
				
				if(line != null)
				{
					countR += 1;
				}
				
			}
			
			fr.close();
			
			fr = new FileReader("ParseTable.txt");
			
			int c = 0;
			
			while(c != -1)
			{
				c = fr.read();
				
				if (c == 9)
				{
					countC += 1;
				}
				if (c == 10)
				{
					countC += 1;
					break;
				}
			}
			fr.close();
			
			this.setParseT(countR, countC);
			
			fr = new FileReader("RefrenceTable.txt");
			br = new BufferedReader(fr);
			
			line = "";
			
			countR = 0;
			
			while(line != null)
			{
				line = br.readLine();
				
				if(line != null)
				{
					countR += 1;
				}
			}
			
			fr.close();
			this.setRefT(countR);
			
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
		int countR = 0;
		int countC = 0;
		
		int c = 0;
		String temp = "";
		
		try
		{
			FileReader fr = new FileReader("ParseTable.txt");
			
			while(c != -1)
			{
				c = fr.read();
				
				
				if(c != -1)
				{
					temp += (char) c;
				}
				
				if(c == 9)
				{
					this.ParseT[countR][countC] = temp.substring(0,temp.length()-1);
					temp = "";
					countC += 1;
				}
				if(c == 10)
				{
					this.ParseT[countR][countC] = temp;
					temp = "";
					countR += 1;
					countC = 0;
				}
				
				if(c == -1)
				{
					this.ParseT[countR][countC] = temp;
				}
			}
			
			fr.close();
			
			fr = new FileReader("RefrenceTable.txt");
			
			Boolean check = false; 
			
			c = 0;
			countR = 0;
			countC = 0;
			temp = "";
			
			while(c != -1)
			{
				c = fr.read();
				
				if(!check)
				{
					temp += (char) c; 
				}
				
				if((char) c == '-')
				{
					temp = temp.substring(0, temp.length()-2);
					this.RefT[countR][1] = temp;
					temp = "";
				}
				
				if((char) c == '>')
				{
					check = true;
				}
				
				if(check)
				{
					temp += (char) c;
				}
				
				if(c == 10 || c == -1)
				{
					temp = temp.substring(3, temp.length()-1);
					this.RefT[countR][2] = temp;
					temp = "";
					check = false;
					
					temp = countR + 1 + " "; 
					this.RefT[countR][0] =  temp;
					temp = "";
					countR += 1;
				}
				else
				{
					
					
				}
				//System.out.println(countR);
				if(c != -1)
				{
					
				}
				
			}
			
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
		this.BuildTable();
		this.FillTable();
	}
	
	public int LookGRule(String State)
	{
		int Row = 0;
		
		for(int i=0;i<this.ParseT.length; i++)
		{
			if(this.ParseT[i][0].equals(State))
			{
				Row = i;
			}
		}
		
		if(Row == 0)
		{
			Row = -1;
		}
		
		return Row;
	}
	
	public int LookTerminal(String State)
	{
		int Col = 0;
		
		for(int i=0;i<this.ParseT[0].length; i++)
		{
			if(this.ParseT[0][i].equals(State))
			{
				Col = i;
			}
		}
		
		if(Col == 0)
		{
			Col = -1;
		}
		return Col;
	}
	
	public int LookParseT(int Row, String Token)
	{
		int Col = 0;
		int end = 0;
		boolean check = false;
			
		String temp = "";
		
		if(Row == -1)
		{
			return -1;
		}
		
		for(int i=0; i<this.ParseT[0].length; i++)
		{
			if(this.ParseT[0][i].equals(Token))
			{
				Col = i;
				check = true;
				temp = this.ParseT[Row][Col];
			}
		}
		
		if(check)
		{
			
				//System.out.println(temp+"XX");
				if(temp.equals("108 "))
				{
					end = 108;
				}
				else
				{
					System.out.println(this.ParseT[Row][Col] + " " + Row + " " + Col);
					end = Integer.parseInt(this.ParseT[Row][Col]);
				}
			
		}
		else
		{
			end = -1;
		}
		
		return end;
	}
	
	public String LookRef(int State)
	{
		String temp = "";
		
		temp = this.RefT[State-1][2];
		
		
		return temp;
	}
	
	public boolean EPSILONcheck(String State)
	{
		boolean check = false;
		String temp = "";
		
		for(int i=0; i<this.RefT.length; i++)
		{
			if(this.RefT[i][1].equals(State))
			{
				if(this.RefT[i][2].equals("EPSILON"))
				{
					System.out.println(this.RefT[i][2]);
					check = true;
				}
			}
		}
		
		
		return check;
	}
	
	public String getGrammar(String Rule, String Token)
	{
		System.out.println(Rule + " " + Token);
		System.out.println(this.LookGRule(Rule));
		return this.LookRef(this.LookParseT(this.LookGRule(Rule), Token)) + "";
				
	}
	
	public Stack InsertReverse(String Grammar, Stack Stack)
	{
		Stack<String> Rev = new Stack<String>();
		
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

	
	
	
	public void printPTable()
	{
		for(int i=0; i<this.ParseT.length; i++)
		{
			for(int j=0; j<this.ParseT[0].length; j++)
			{
				System.out.print(this.ParseT[i][j]+ " ");
			}
			System.out.println();
		}
	}
	
	public void printRTable()
	{
		for(int i=0; i<this.RefT.length; i++)
		{
			for(int j=0; j<this.RefT[0].length; j++)
			{
				System.out.print(this.RefT[i][j]);
			}
			System.out.println();
		}
	}
	
	public static void main(String args[])
	{
		Stack<String> Stack = new Stack<String>();
		Stack.push("$");
		
		PTable PT = new PTable();
		
		PT.createTable();
		/*PT.printPTable();
		PT.printRTable();
		
		System.out.println(PT.Reverse(PT.getGrammar(("BD"), "SCOL")));
		PT.InsertReverse(PT.getGrammar("BD", "SCOL"), Stack);
		
		
		*/
		System.out.println(PT.LookGRule("CD"));
		System.out.println(PT.LookParseT(8, "CCBR"));
		String Grammar = PT.getGrammar("E", "GT");
		System.out.println(Grammar);
	}

}

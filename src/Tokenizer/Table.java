package A1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Table {

	private int [][] Table;
	private String[][] Final; 
	
	Table()
	{
		Table = null;
		Final = null;
	}
	
	Table(int Row, int Col)
	{
		Table = new int[Row][Col];
		Final = new String[Row][4];
	}
	
	public int[][] getTable()
	{
		return Table;
	}
	
	public void setTable(int Row, int Col)
	{
		Table = new int [Row][Col];
	}
	
	public String[][] getFinal()
	{
		return Final;
	}
	
	public void setFinal(int Row)
	{
		Final = new String[Row][4];
	}
	
	public void buildTable() 
	{
		int countR = 0;
		int countC = 0;
		
		String line = "";
		
		FileReader inputStream = null;
		BufferedReader bufferedReader = null;
		
		try
		{
			
			inputStream = new FileReader("Transition_Table.txt");
			bufferedReader = new BufferedReader(inputStream);
			
			
			while(line != null)
			{
				line = bufferedReader.readLine();
				
				if (line != null)
				{
					countR += 1;
				}
			}
			
			inputStream.close();
			inputStream = new FileReader("Transition_Table.txt");
			
			//System.out.println("COUNTR " + countR);
			
			int c = 0;
			
			while (c != -1)
			{
				c = inputStream.read();
				//System.out.println(c + " " + (char)c);
				if (c == 9)
				{
					countC += 1;
				}
				if (c == 10)
				{
					break;
				}
			}
			inputStream.close();
			
			//System.out.println("COUNTC " + countC);
		}
		catch(FileNotFoundException e2)
		{
			System.out.println("Error opening the file, Process will Terminate");
		}
		
		catch(IOException e)
		{
			System.out.println("Error opening the file, Process will Terminate");
		}
		
		this.setTable(countR, countC);
		this.setFinal(countR);
		
		for (int i=0; i<this.getFinal().length; i++)
		{
			this.getFinal()[i][0] = i+"";
		}
	}
	
	public void fillTable()
	{
		FileReader inputStream = null;
		
		try
		{
			inputStream = new FileReader("Transition_Table.txt");
			
			int Row = 0;
			int Col = 0;
			
			int c = 0;
			int flush = 0;
			String temp = "";
			String temp2 = "";
			
			while (c != -1)
			{
				c = inputStream.read();
				//System.out.println(c + " " + (char)c + "j");
				
				if((char)c == 'R')
				{
					while((char)c != 's')
					{
						c = inputStream.read();
					}
					temp = ""+9000000;
					
					c = inputStream.read();
				}
				
				if((char)c == 'N')
				{
					while((char)c != 'o')
					{
						c = inputStream.read();
					}
					temp = ""+8000000;
					c = inputStream.read();
				}
				
				if((char)c == 'F')
				{
					while((char)c != ']')
					{
						c = inputStream.read();
					}
					temp = ""+4000000;
					c = inputStream.read();
				}
				
				int count = 0;
				
				if((char)c == 'B')
				{
					while(count != 2)
					{
						c = inputStream.read();
						if((char)c == 'k')
						{
							count +=1;
						}
					}
					temp = ""+3000000;
					c = inputStream.read();
				}
				
				if((char)c == 'y')
				{
					temp = "";
							
					while((char)c != '[')
					{
						c = inputStream.read();
					}
					this.getFinal()[Row][1] = "yes";
					
					c = inputStream.read();
					
					while((char)c != ']')
					{
						temp += (char)c;
						
						c = inputStream.read();
					}
					
					this.getFinal()[Row][2] = temp;
					
					while((char)c != 'N' && (char)c != 'Y')
					{
						c = inputStream.read();
					}
					
					if((char)c == 'N')
					{
						this.getFinal()[Row][3] = "No";
					}
					
					if((char)c == 'Y')
					{
						this.getFinal()[Row][2] = "Yes";
					}
					
					temp = "";
					
				}
				
				if((char)c == 'E')
				{
					while ((char)c != 'L')
					{
						c = inputStream.read();
					}
					
					temp = ""+7000000;
					c = inputStream.read();
				}
				
				if((char)c == 'S')
				{
					while((char)c != 'e')
					{
						c = inputStream.read();
					}
					
					temp = ""+6000000;
					c = inputStream.read();
				}
				
				if((char)c == 'T')
				{
					while((char)c != 'b')
					{
						c = inputStream.read();
					}
					
					temp = ""+5000000;
					c = inputStream.read();
				}
				
				if (c == 9)
				{
					if(temp != "")
					{
						flush = Integer.parseInt(temp);
					}
					
					if(Row != 0 && Col <= (this.getTable()[Row].length-2))
					{
						if(temp.length() == 2)
						{
							flush = Integer.parseInt(temp);
							temp = ""+(char)flush;
							flush = Integer.parseInt(temp);
						}
						
						if(temp.length() == 4)
						{
							temp2 = temp.substring(2,4);
							temp = temp.substring(0,2);
							
							flush = Integer.parseInt(temp);
							temp = ""+(char)flush;
							
							flush = Integer.parseInt(temp2);
							temp2 = ""+(char)flush;
							
							temp = temp + temp2;
							
							flush = Integer.parseInt(temp);
						}
					}
					
					this.getTable()[Row][Col] = flush;
					
					temp = "";
					flush = 0;
					Col += 1;
					
				}
				if (c == 10)
				{
					temp = "";
					Row += 1;
					Col = 0;

				}
				
				if(c != 10 && c != 9)
				{
					temp += c; 
				}
			}
			inputStream.close();
			
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
		this.buildTable();
		this.fillTable();
	}
	
	public int findState(int state,int lookup)
	{
		int Col = 0;
		
		for(int i=0;i<this.getTable()[0].length; i++)
		{
			if(lookup == this.getTable()[0][i])
			{
				
				Col = i;
				break;
			}
		}
		
		return this.getTable()[state][Col];
	}
	
	public boolean isFinalState(int state)
	{
		if(this.getFinal()[state][1] == "yes")
		{
			return true;
		}
		
		return false;
	}
	
	public Token createToken(int state, String value, String location)
	{
		Token token = new Token(this.getFinal()[state][2],value, location);
		return token;
	}
	
	public void printTable()
	{
		for(int i=0; i< this.getTable().length; i++)
		{
			for(int j=0; j<this.getTable()[i].length; j++)
			{
				System.out.print(this.getTable()[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public void printFinal()
	{
		for(int i=0; i< this.getFinal().length; i++)
		{
			for(int j=0; j<this.getFinal()[i].length; j++)
			{
				System.out.print(this.getFinal()[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		int newS = 0;
		
		Table table = new Table();
		
		table.createTable();
		
		table.printTable();
		
		table.printFinal();
		
		
	}

}

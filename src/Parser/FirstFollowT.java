package A2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class FirstFollowT {

	private String [][] FiT;
	private String [][] FoT;
	
	FirstFollowT()
	{
		FiT = null;
		FoT = null;
	}
	
	public String [][] getFiT()
	{
		return FiT;
	}
	
	public String [][] getFoT()
	{
		return FoT;
	}
	
	public void setFiT(int Row)
	{
		FiT = new String[Row][2];
	}
	
	public void setFoT(int Row)
	{
		FoT = new String[Row][2];
	}

	public void BuildTable()
	{
		String line = "";
		int countR = 0;
		
		try
		{
			FileReader fr = new FileReader("FirstSet.txt");
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
			
			this.setFiT(countR);
			
			countR = 0;
			line = "";
			
			fr = new FileReader("FollowSet.txt");
			br = new BufferedReader(fr);
			
			while(line != null)
			{
				line = br.readLine();
				
				if(line != null)
				{
					countR += 1;
				}
				
			}
			
			fr.close();
			
			this.setFoT(countR);
			
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
		String line = "";
		int c = 0;
		int count = 0;
		
		try
		{
			FileReader fr = new FileReader("FirstSet.txt");
			
			while(c != -1)
			{
				c = fr.read();
				
				line += (char) c;
				
				if(c == 949)
				{
					line = line.substring(0,line.length()-1) + "EPSILON";
				}
				
				if(c == 9)
				{
					line = line.substring(0,line.length()-1);
					this.FiT[count][0] = line;
					line = "";
				}
				
				if(c == 10)
				{
					line = line.substring(0,line.length()-1);
					
					this.FiT[count][1] = line;
					line = "";
					count += 1;
				}
				
				if(c == -1)
				{
					line = line.substring(0,line.length()-1);
					
					this.FiT[count][1] = line;
					line = "";
					count += 1;
				}
				
			}
			
			fr.close();
			
			fr = new FileReader("FollowSet.txt");
			
			count = 0;
			line = "";
			c = 0;
			
			while(c != -1)
			{
				c = fr.read();
				
				line += (char) c;
				
				if(c == 949)
				{
					line = line.substring(0,line.length()-1) + "EPSILON";
				}
				
				if(c == 9)
				{
					line = line.substring(0,line.length()-1);
					this.FoT[count][0] = line;
					line = "";
				}
				
				if(c == 10)
				{
					line = line.substring(0,line.length()-1);
					this.FoT[count][1] = line;
					line = "";
					count += 1;
				}
				
				if(c == -1)
				{
					line = line.substring(0,line.length()-1);
					
					this.FoT[count][1] = line;
					line = "";
					count += 1;
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
		this.BuildTable();
		this.FillTable();
	}
	
	public int findFollow(String State)
	{
		int Row = -1;
		
		for(int i=0; i<this.FoT.length; i++)
		{
			if(this.FoT[i][0].equals(State))
			{
				Row = i;
				break;
			}
		}
		
		return Row;
	}
	
	public boolean inFollow(String State, String Lookup)
	{
		boolean check = false;
		int Row = this.findFollow(State);
		String line = this.FoT[Row][1];
		String temp = "";
		
		StringTokenizer st = new StringTokenizer(line);
		
		while(st.hasMoreTokens())
		{
			temp = st.nextToken(", ");
			if(temp.equals(Lookup))
			{
				check = true;
				break;
			}
		}
		
		return check;
	}
	
	public int findFirst(String State)
	{
		int Row = -1;
		
		for(int i=0; i<this.FiT.length; i++)
		{
			if(this.FiT[i][0].equals(State))
			{
				Row = i;
				break;
			}
		}
		
		return Row;
	}
	
	public boolean inFirst(String State, String Lookup)
	{
		boolean check = false;
		int Row = this.findFirst(State);
		String line = this.FiT[Row][1];
		String temp = "";
		
		StringTokenizer st = new StringTokenizer(line);
		
		while(st.hasMoreTokens())
		{
			temp = st.nextToken(", ");
			
			if(temp.equals(Lookup))
			{
				check = true;
				break;
			}
		}
		
		return check;
	}
	
	
	public void printFiT()
	{
		for(int i=0; i<this.FiT.length; i++)
		{
			for(int j=0; j<this.FiT[0].length; j++)
			{
				System.out.print(this.FiT[i][j] + "x ");
			}
			System.out.println();
		}
	}
	
	public void printFoT()
	{
		for(int i=0; i<this.FoT.length; i++)
		{
			for(int j=0; j<this.FoT[0].length; j++)
			{
				System.out.print(this.FoT[i][j] + "x ");
			}
			System.out.println();
		}
	}
	
	public static void main(String args[])
	{
		FirstFollowT FFT = new FirstFollowT();
		
		FFT.createTable();
		
		//FFT.printFiT();
		
		System.out.println(FFT.findFirst("Hed")+ " "+9);
		System.out.println(FFT.inFirst("Hed", "ID"));
		
	}
}

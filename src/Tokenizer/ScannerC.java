package A1;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ScannerC {
	
	private Table table;
	private char[] Buffer;
	private char[] Buffer2;
	private int currentBuffer;
	private int BufferPosition;
	private int N_buffer;
	private boolean endBuffer;
	private int Position;
	private int Line;
	private int Character;
	private String Location;
	private String ReadFileName;
	private String TokenOutFileName;
	private String ErrorOutFileName;
	private String AtoCCFileName;
	
	public ScannerC()
	{
		table = null;
		Buffer = new char[10];
		Buffer2 = new char[10];
		currentBuffer = 1;
		BufferPosition = -1;
		N_buffer = 0;
		endBuffer = false;
		Position = 0;
		Line = 1;
		Character = 0;
		Location = null;
		ReadFileName = "test.txt";
		TokenOutFileName = "Success.txt";
		ErrorOutFileName = "Error.txt";
		AtoCCFileName = "AtoCC.txt";
	}
	
	public ScannerC(String TextFile)
	{
		table = null;
		Buffer = new char[10];
		Buffer2 = new char[10];
		currentBuffer = 1;
		BufferPosition = -1;
		N_buffer = 0;
		endBuffer = false;
		Position = 0;
		Line = 1;
		Character = 0;
		Location = null;
		ReadFileName = TextFile;
		TokenOutFileName = "Success.txt";
		ErrorOutFileName = "Error.txt";
		AtoCCFileName = "AtoCC.txt";
	}
	
	public void PrintScanner()
	{
		System.out.println(Position + " " + Line + " " + Location);
	}
	
	public Table getTable()
	{
		return table;
	}
	
	public char[] getBuffer()
	{
		return Buffer;
	}
	
	public char[] getBuffer2()
	{
		return Buffer2;
	}
	
	public String getReadFileName()
	{
		return ReadFileName;
	}
	
	public int getPosition()
	{
		return Position;
	}
	
	public String getLocation()
	{
		return Location;
	}
	
	public int getCurrentBuffer()
	{
		return this.currentBuffer;
	}
	public void setTable(Table t)
	{
		table = t;
	}
	
	public void setBuffer(char[] buffer)
	{
		Buffer = buffer;
	}
	
	public void setBuffer2(char[] buffer)
	{
		Buffer2 = buffer;
	}
	
	public void setPosition(int position)
	{
		Position = position;
	}
	
	public void setLocation(String location)
	{
		Location = location;
	}
	
	
	public void LexicalAnalyzer()
	{
		
		
		Table table = new Table();
		table.createTable();
		
		this.setTable(table);
		
		FileReader inputStream = null;
		FileWriter outputStream = null;
		BufferedWriter writer = null;
		
		File success = new File(this.TokenOutFileName);
		File error = new File(this.ErrorOutFileName);
		File atocc = new File(this.AtoCCFileName);
				
		Token token = null;
		
		ArrayList<Token> TokenList = new ArrayList();
		
		int c = 0;
		int line = 0;
		int character = 0;
		
		
		try
		{
			outputStream = new FileWriter(success);
			
			outputStream.write("");
			outputStream.flush();
			outputStream.close();
			
			outputStream = new FileWriter(error);
			
			outputStream.write("");
			outputStream.flush();
			outputStream.close();
			
			outputStream = new FileWriter(atocc);
			
			outputStream.write("");
			outputStream.flush();
			outputStream.close();
			
			inputStream = new FileReader(this.getReadFileName());
			
			c = inputStream.read(this.getBuffer());
			this.N_buffer += 1;
			
			c = inputStream.read(this.getBuffer2());
			this.N_buffer += 1;
			
			this.setPosition(this.N_buffer*10);
			
			while(this.currentBuffer != 0)
			{
				token = nextToken();
				
				TokenList.add(token);
				
			}
			
			inputStream.close();
			
			for(int i=0; i<TokenList.size(); i++)
			{
				if(TokenList.get(i) != null)
				{
					if(!TokenList.get(i).getType().equals("ERR"))
					{
						outputStream = new FileWriter(success, true);
						
						writer = new BufferedWriter(outputStream);
						
						writer.write(TokenList.get(i).toString() + "\n");
						
						writer.flush();
						
						outputStream.close();
					}
					else
					{
						outputStream = new FileWriter(error, true);
						
						writer = new BufferedWriter(outputStream);
						
						writer.write(TokenList.get(i).toStringErr() + "\n");
						
						writer.flush();
						
						outputStream.close();
						
					}
					
					outputStream = new FileWriter(atocc, true);
					
					writer = new BufferedWriter(outputStream);
					
					writer.write(TokenList.get(i).getType() + "+");
					
					writer.flush();
					
					writer.close();
				}
			}
			
			
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Error finding the file, program terminating");
		}
		catch(IOException e2)
		{
			System.out.println("Error handeling the file, program terminating");
		}
	}
	
	public void Initializer(String File)
	{
		this.ReadFileName = File;
		
		Table table = new Table();
		table.createTable();
		
		this.setTable(table);
		
		Token token = null;
		
		int c = 0;
		int line = 0;
		int character = 0;
		
		FileReader inputStream = null;
		
		try
		{
			inputStream = new FileReader(this.getReadFileName());
			
			c = inputStream.read(this.getBuffer());
			this.N_buffer += 1;
			
			c = inputStream.read(this.getBuffer2());
			this.N_buffer += 1;
			
			this.setPosition(this.N_buffer*10);
			
			inputStream.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Error finding the file, program terminating");
		}
		catch(IOException e2)
		{
			System.out.println("Error handeling the file, program terminating");
		}
	}
	
	
	public void nextBuffer()
	{
		
		FileReader inputStream = null;
		int c = 0;
		
		try
		{
			inputStream = new FileReader(this.getReadFileName());
			
			inputStream.skip(this.Position);
			
			if(this.endBuffer)
			{
				this.currentBuffer = 0;
				return;
			}
			
			if(this.currentBuffer == 1)
			{
				this.Buffer = new char[10];
				c = inputStream.read(this.Buffer);
				
				if(c == -1)
				{
					this.endBuffer = true;
					
					this.currentBuffer = 2;
					inputStream.close();
					return;
				}
				this.Position += c;
				
				//System.out.println("Next buffer 1 takes in: " + c);
				//this.printBuffer();
				
				inputStream.close();
				return;
			}
			else if(this.currentBuffer == 2)
			{
				this.Buffer2 = new char[10];
				c = inputStream.read(this.Buffer2);
				
				if(c == -1)
				{
					this.endBuffer = true;
					this.currentBuffer = 1;
					inputStream.close();
					return;
				}
				
				//System.out.println("Next buffer 2 takes in: " + c);
				//this.printBuffer();
				
				this.Position += c;
				inputStream.close();
				return;
			}
			else
			{
				inputStream.close();
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Error finding the file, program terminating");
		}
		catch(IOException e2)
		{
			System.out.println("Error handeling the file, program terminating");
		}

		return;
	}
	
	public void previousBuffer()
	{
		//System.out.println("PREEEVVVVVVVVVVVVVV");
		FileReader inputStream = null;
		int c = 0;
		
		if(this.endBuffer)
		{
			this.endBuffer = false;
		}
		
		try
		{
			inputStream = new FileReader(this.getReadFileName());
			
			if(this.currentBuffer == 1)
			{
				this.Position -= (this.BufferLength(this.Buffer) + this.BufferLength(this.Buffer2) + 10);
				
				this.Buffer2 = this.Buffer;
				
				
				if(this.Position < 0)
				{
					this.Position = 0;
					this.Buffer = new char[10];
					this.Buffer2 = new char[10];
					c = inputStream.read(this.Buffer);
					c = inputStream.read(this.Buffer2);
					this.Position = 20;
					this.currentBuffer = 1;
					inputStream.close();
					return;
				}
				
				inputStream.skip(this.Position); 
				
				this.Buffer = new char[10];
				
				c = inputStream.read(this.Buffer);
				
				this.Position += (this.BufferLength(this.Buffer) + this.BufferLength(this.Buffer2));
				
				inputStream.close();
				
				this.currentBuffer = 1;
				return;
			}
			else if(this.currentBuffer == 2)
			{
				
				this.Position -= (this.BufferLength(this.Buffer2) + this.BufferLength(this.Buffer));
				
				if(this.Position < 0)
				{
					this.Position = 0;
					
					this.Buffer = new char[10];
					this.Buffer2 = new char[10];
	
					c = inputStream.read(this.Buffer);
					c = inputStream.read(this.Buffer2);
					
					this.currentBuffer = 1;
					this.Position = 20;
					inputStream.close();
					return;
				}
				
				inputStream.skip(this.Position);
				
				this.Buffer = new char[10];
				c = inputStream.read(this.Buffer);
				
				this.Position += (this.BufferLength(this.Buffer2));
				
				inputStream.close();
				
				this.currentBuffer = 1;

				return;
			}
			
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Error finding the file, program terminating");
		}
		catch(IOException e2)
		{
			System.out.println("Error handeling the file, program terminating");
		}

	}
	
	public char nextChar()
	{
		if(this.currentBuffer == 1)
		{
			this.BufferPosition += 1;
			
			if(this.BufferPosition > 9)
			{
				this.BufferPosition = 0;
				
				this.nextBuffer();
				
				if(this.currentBuffer == 0)
				{
					return '#';
				}
				this.currentBuffer = 2;
				return this.Buffer2[this.BufferPosition];
			}
			else
			{
				return this.Buffer[this.BufferPosition];
			}
		}
		else if(this.currentBuffer == 2)
		{
			this.BufferPosition += 1;
			
			if(this.BufferPosition > 9)
			{
				this.BufferPosition = 0;
				this.nextBuffer();
				
				if(this.currentBuffer == 0)
				{
					return '#';
				}
				
				this.currentBuffer = 1;
				return this.Buffer[this.BufferPosition];
			}
			else
			{
				return this.Buffer2[this.BufferPosition];
			}
		}
		return ' ';
	}
	
	public char backupChar()
	{
		if(this.currentBuffer == 1)
		{
			this.BufferPosition -= 1;
			
			if(this.BufferPosition == -1)
			{
				this.previousBuffer();
				this.BufferPosition = 9;
				return this.Buffer[this.BufferPosition];
			}
			else
			{
				return this.Buffer[this.BufferPosition];
			}
		}
		else if(this.currentBuffer == 2)
		{
			this.BufferPosition -= 1;
			
			if(this.BufferPosition == -1)
			{
				this.previousBuffer();
				this.BufferPosition = 9;
				return this.Buffer[this.BufferPosition];
			}
			else
			{
				return this.Buffer2[this.BufferPosition];
			}
		}
		
		
		return ' ';
	}
	
	public char currentChar()
	{
		if(currentBuffer == 1)
		{
			return this.Buffer[this.BufferPosition];
		}
		else if(currentBuffer == 2)
		{
			return this.Buffer2[this.BufferPosition];
		}
		
		return ' ';
	}
	
	public Token nextToken()
	{
		String lex = "";
		String final1 = "";
		
		int start = 0;
		int end = 0;
		
		int state = 1;
		int checkState = 1;
		int count = 0;
		
		Token token = null;
			
		char[] Buffer = new char[100];
		
		char lookup = ' ';
		int lookup_T = 0;
		int lookup_C = 0;
		
		while(token == null)
		{
			lookup = nextChar();
			
			if(lookup == '#')
			{
				lookup = ' ';
			}
			
			lookup_T = Transform(lookup);
			
			if(lookup_T == 0)
			{
				lookup_T = (int)lookup;
			}
			
			if(lookup_T == 7000000)
			{
				this.Line += 1;
				this.Character = 0;
			}
			else
			{
				this.Character += 1;
			}
			
			if(count == 0)
			{
				start = this.Character;
			}
			
			Buffer[count] = lookup;
			count += 1;
			
			state = this.table.findState(state, lookup_T);
			
			if(this.table.isFinalState(state))
			{
				final1 = this.table.getFinal()[state][2];
				
				if(lookup_T == 6000000)
				{
					end = this.Character-1;
				}
				else if(lookup_T == 7000000)
				{
					end = start+count;
					this.Line -= 1;
				}
				else
				{
					end = this.Character;
				}
				
				if(count > 1)
				{
					if(lookup_T != 48 && (lookup_T < 90 || lookup_T > 122) && lookup_T != 9000000 && lookup_T != 8000000 && lookup_T != 6000000)
					{
						Buffer[count-1] = ' ';
						end = end-1;
						lookup = backupChar();
						this.Character -= 1;
					}
					if(this.delLast(lookup) == 1000000)
					{
						Buffer[count-1] = ' ';
						end = end-1;
						lookup = backupChar();
						this.Character -= 1;
					}
					else if(this.delLast(lookup) == 2000000 && final1.equals("INT"))
					{
						Buffer[count-1] = ' ';
						end = end-1;
						lookup = backupChar();
						this.Character -= 1;
					}
				}
				
				int state2 = 0;
				
				lookup = nextChar();
				lookup_T = Transform(lookup);
				
				if(lookup_T == 0)
				{
					lookup_T = (int)lookup;
				}
				
				state2 = this.table.findState(state, lookup_T);
				
				if(state2 == 62)
				{
					Buffer[count] = lookup;
					count +=1;
					
					while(!this.table.isFinalState(state2))
					{
						lookup = nextChar();
						lookup_T = Transform(lookup);
						
						if(lookup_T == 0)
						{
							lookup_T = (int)lookup;
						}
						
						end += 1;
						
						state2 = this.table.findState(state2, lookup_T);
					}
					
					state = state2;
				}
				
				if(state2 == 64)
				{
					Buffer[count] = lookup;
					count +=1;
					
					while(!this.table.isFinalState(state2))
					{
						lookup = nextChar();
						lookup_T = Transform(lookup);
						
						if(lookup_T == 0)
						{
							lookup_T = (int)lookup;
						}
						
						
						end += 1;
						
						state2 = this.table.findState(state2, lookup_T);
					}
					
					state = state2;
					
					nextChar();
				}
				
				if(state != state2 && state2 != 1)
				{
					state = state2;
					Buffer[count] = lookup;
					end += 1;
					count +=1;
				}
				else
				{
					backupChar();
				}
				
				if(this.table.getFinal()[state][2].equals("INT"))
				{
					for(int i=0; i<100; i++)
					{
						lookup = Buffer[i];
						
						lookup_T = Transform(lookup);
						
						if(lookup_T == 0)
						{
							lookup_T = (int)lookup;
						}
				
						if(lookup_T != 8000000 && lookup != '0' && lookup_T != 7000000 && lookup_T != 6000000 && lookup_T != 0)
						{
							state = 85;
							final1 = "ERR";
						}
					}
				}
				
				
				if(this.table.getFinal()[state][2].equals("ERR"))
				{
					
					while(lookup_T != 6000000)
					{
						lookup = nextChar();
						lookup_T = Transform(lookup);
						
						if(lookup == '#')
						{
							lookup_T = 6000000;
							lookup = ' ';
						}
						
						if(lookup_T == 7000000)
						{
							lookup = ' ';
							this.Line += 1;
							lookup_T = 6000000;
						}
						
						Buffer[count] = lookup;
						count +=1;
						end += 1;
					}
					
					for(int i=0; i<Buffer.length; i++)
					{
						if(Buffer[i] == '.')
						{
							final1 = "FLOAT";
						}
					}
					
					if(final1.equals("ERR"))
					{
						int state3 = 1;
						state2 = 1;
						int count2 = 0;
						int count3 = 0;
						
						while(!this.table.isFinalState(state2))
						{
							lookup = Buffer[count2];
							lookup_T = Transform(lookup);
							
							if(lookup_T == 0)
							{
								lookup_T = (int)lookup;
							}
							
							state2 = this.table.findState(state2, lookup_T);
							
							if(this.table.isFinalState(state2))
							{
								state3 = state2;
								
								while(lookup != ' ')
								{
									lookup = Buffer[count2];
									lookup_T = Transform(lookup);
									
									if(lookup_T == 0)
									{
										lookup_T = (int)lookup;
									}
									
									count2 += 1;
									
									state3 = this.table.findState(state3, lookup_T);
									
									if(this.table.isFinalState(state3))
									{
										state2 = state3;
									}
								}
							}
							count2 += 1;
						}
						final1 = this.table.getFinal()[state2][2];
						
						for(int i=0; i<Buffer.length; i++)
						{
							if(Buffer[i] == '.')
							{
								final1 = "FLOAT";
							}
						}
					}
				}
				
				lex = String.valueOf(Buffer);
				this.Location = this.Line + "::" + start + "-" + end;
				
				//System.out.println(state);
				
				token = this.table.createToken(state, lex, this.Location);
				state = 1;
				
				if(token.getType().equals("ERR"))
				{
					token.setFinal(final1);
				}
				
				return token;
				
			}
			
			if(state == 1)
			{
				return null;
			}
		}
		
		return token;
	}
	
	public int Transform(char lookup)
	{
		int lookup_T = 0;
		
		switch(lookup)
		{
			case 'b':
				lookup_T = 9000000;
				break;
			case 'j':
				lookup_T = 9000000;
				break;
			case 'k':
				lookup_T = 9000000;
				break;
			case 'q':
				lookup_T = 9000000;
				break;
			case 'v':
				lookup_T = 9000000;
				break;
			case 'w':
				lookup_T = 9000000;
				break;
			case 'x':
				lookup_T = 9000000;
				break;
			case 'y':
				lookup_T = 9000000;
				break;
			case 'z':
				lookup_T = 9000000;
				break;
			case '\n':
				lookup_T = 7000000;
				break;
			case ' ':
				lookup_T = 6000000;
				break;
			case '\t':
				lookup_T = 5000000;
				break;
			case '1':
				lookup_T = 8000000;
				break;
			case '2':
				lookup_T = 8000000;
				break;
			case '3':
				lookup_T = 8000000;
				break;
			case '4':
				lookup_T = 8000000;
				break;
			case '5':
				lookup_T = 8000000;
				break;
			case '6':
				lookup_T = 8000000;
				break;
			case '7':
				lookup_T = 8000000;
				break;
			case '8':
				lookup_T = 8000000;
				break;
			case '9':
				lookup_T = 8000000;
				break;
				
		}
		
		if (lookup_T == 0)
		{
			if((int) lookup > 64 && (int)lookup <91)
			{
				lookup_T = 9000000;
			}
			
		}
		
		return lookup_T;
	}
	
	public int delLast(char lookup)
	{
		int del = 0;
		
		if((int) lookup == 40 || (int) lookup == 41 || ((int) lookup > 57 && (int) lookup < 63) || ((int) lookup > 90 && (int) lookup < 96) || ((int) lookup > 122 && (int) lookup < 125))
		{
			del = 1000000;
		}
		
		
		
		if(((int) lookup > 64 && (int)lookup <91) || ((int) lookup > 96 && (int) lookup < 123))
		{
			del = 2000000;
		}
		
		
		return del;
	}
	
	public int BufferLength(char[] Buffer)
	{
		int count = 0;
		
		
		for(int i=0; i<10; i++)
		{
			if(Buffer[i] != 0)
			{
				count += 1;
			}
		}
		
		return count;
	}
	
	public void printBuffer()
	{
		if(this.currentBuffer == 1)
		{
			System.out.println("1");
			for(int i=0; i<this.Buffer.length; i++)
			{
				System.out.print(this.Buffer[i]);
			}
			System.out.println();
		}
		else if(this.currentBuffer == 2)
		{
			System.out.println("2");
			for(int i=0; i<this.Buffer2.length; i++)
			{
				System.out.print(this.Buffer2[i]);
			}
			System.out.println();
		}
	}

	public static void main(String[] args) 
	{
		ScannerC scan = new ScannerC();
		
		scan.LexicalAnalyzer();
	}
}

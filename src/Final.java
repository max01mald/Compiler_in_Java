import A1.ScannerC;
import A2.Parser;
import A3.ASTPrinterVisitor;
import A3.SemanticCorrectness;
import A3.SymTabCreationVisitor;
import A3.TypeCheckingVisitor;
import A4.ComputeMemSizeVisitor;
import A4.TagsBasedCodeGenerationVisitor;
import AST_Tree.AST_Node;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;
public class Final 
{
	public static void main(String args[])
	{
		Scanner kb = new Scanner(System.in);
		
		boolean check = true;
		boolean check2 = true;
		boolean check3 = true;
		boolean check4 = true;
		boolean check5 = true;
		
		String answer = "";
		try
		{
		while(check)
		{
			System.out.println("Please Select a Phase");
			System.out.println("1) Scanner");
			System.out.println("2) Parser");
			System.out.println("3) Symboltable");
			System.out.println("4) Codegeneration");
			System.out.println("E) Exit");
			answer = kb.nextLine();
			
			if(answer.equals("1"))
			{
				while(check2)
				{
					System.out.println("Please Select a File by name");
					System.out.println("E to Exit");
					answer = kb.nextLine();
					
					if(answer.equals("E"))
					{
						check2 = false;
					}
					else if(answer.charAt(answer.length()-1) == 't')
					{
						ScannerC sc = new ScannerC(answer);
						
						sc.LexicalAnalyzer();
						
						Final f = new Final();
						
						f.OrganizeError();
						
						System.out.println("Compleated");
					}
					else
					{
						System.out.println("Please try again");
					}
					
				}
			}
			else if(answer.equals("2"))
			{
				while(check3)
				{
					System.out.println("Please Select a File by name");
					System.out.println("E to Exit");
					answer = kb.nextLine();
					
					if(answer.equals("E"))
					{
						check3 = false;
					}
					else if(answer.charAt(answer.length()-1) == 't')
					{
						Parser par = new Parser();
						
						par.setFile(answer);
						
						Final f = new Final();
						
						if(!par.Parse())
						{
							f.OrganizeError();
						}
						else
						{
							System.out.println(true);
						}
						
						
						BufferedWriter bw = new BufferedWriter(new FileWriter("SymTable.txt"));
						
						bw.write("");
						bw.flush();
						bw.close();
						
						AST_Node nw = par.getDTree();
						
						nw = nw.TreeParent();
						
						System.out.println("Compleated");
					}
					else
					{
						System.out.println("Please try again");
					}
				}
			}
			else if(answer.equals("3"))
			{
				while(check4)
				{
					System.out.println("Please Select a File by name");
					System.out.println("E to Exit");
					answer = kb.nextLine();
					
					if(answer.equals("E"))
					{
						check4 = false;
					}
					else if(answer.charAt(answer.length()-1) == 't')
					{
						Parser par = new Parser();
						
						par.setFile(answer);
						
						System.out.println(par.Parse());
						
						AST_Node nw = par.getDTree();
						
						SymTabCreationVisitor cv = new SymTabCreationVisitor();
						TypeCheckingVisitor tc = new TypeCheckingVisitor("TypeCheck.txt");
						
						ASTPrinterVisitor pv = new ASTPrinterVisitor("printedAST.txt");
						
						nw.accept(pv);
						
						cv.setErrorFile("Error.txt");
						
						nw.accept(cv);
						
						boolean SymCheck = nw.m_symtab.valid;
						
						nw.accept(tc);
						
						SemanticCorrectness sc = new SemanticCorrectness(); 
						
						boolean typeCheck = nw.m_symtab.valid;
						Final f = new Final();
						
						if(typeCheck && SymCheck)
						{
							System.out.println(true);
						}
						else
						{
							f.OrganizeError();
							System.out.println(false);
						}
						
						System.out.println("Compleated");
						
						
						
						BufferedWriter bw = new BufferedWriter(new FileWriter("SymTable.txt"));
						
						bw.write("");
						bw.flush();
						bw.close();
						
						nw.m_symtab.printTables(nw, "", "");
						
						
						
					}
					else
					{
						System.out.println("Please try again");
					}
				}
			}
			else if(answer.equals("4"))
			{
				while(check5)
				{
					System.out.println("Please Select a File by name");
					System.out.println("E to Exit");
					answer = kb.nextLine();
					
					if(answer.equals("E"))
					{
						check5 = false;
					}
					else if(answer.charAt(answer.length()-1) == 't')
					{
						Parser par = new Parser();
						
						par.setFile(answer);
						
						System.out.println(par.Parse());
						
						AST_Node nw = par.getDTree();
						
						SymTabCreationVisitor cv = new SymTabCreationVisitor();
						TypeCheckingVisitor tc = new TypeCheckingVisitor("TypeCheck.txt");
						
						ASTPrinterVisitor pv = new ASTPrinterVisitor("printedAST.txt");
						
						nw.accept(pv);
						
						cv.setErrorFile("Error.txt");
						
						nw.accept(cv);
						
						boolean SymCheck = nw.m_symtab.valid;
						
						nw.accept(tc);
						
						nw.m_symtab.printTables(nw, "");
						
		
						
						
						
						
						ComputeMemSizeVisitor cm = new ComputeMemSizeVisitor();
						
						nw.accept(cm);
						
						TagsBasedCodeGenerationVisitor tg = new TagsBasedCodeGenerationVisitor("Code.txt");
						
						nw.accept(tg);
						
						Final f = new Final();
						
						//f.OrganizeError();
						
						System.out.println("Compleated");
						
						
					}
					else
					{
						System.out.println("Please try again");
					}
				}
			}
			else if(answer.equals("E"))
			{
				check = false;
			}
		
		}
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Error");
		}
		catch(IOException e)
		{
			System.out.println("Error");
		}
		
	}
	
	public File file = new File("");
	
	public void OrganizeError() throws IOException, FileNotFoundException
	{
		File file1 = new File("Error.txt");
		File file2 = new File("TypeCheck.txt");
		File file3 = new File("SymTabError.txt");
		File file4 = new File("ParsingErrors.txt");
		
		Final f = new Final();
		
		Vector<String> vec = new Vector<String>();
		
	
		FileReader fr = null;
		BufferedReader br = null;
		Scanner sc = null;
		for(int i=0; i<4; i++)
		{
		
			if(i == 0)
			{
				f.file = file1;
			}
			else if(i == 1)
			{
				f.file = file2;
			}
			else if(i == 2)
			{
				f.file = file3;
			}
			else if(i == 3)
			{
				f.file = file4;
			}
			
			
			sc = new Scanner(f.file);
			
			
			
			while(sc.hasNextLine())
			{
				vec.add(sc.nextLine());
			}
			
			System.out.println("At least this works ");
			
			System.out.println(vec.size());
			
			sc.close();
			sc = null;
			br = null;
			fr = null;
		}
		
		for(int i=0; i<vec.size()-1; i++)
		{
			System.out.println(vec.get(i));
			if(vec.get(i).equals("") || vec.get(i) == null)
			{
				vec.remove(i);
			}
			
			
		}
		
		for(int i=0; i<vec.size()-1; i++)
		{
			for(int j=i+1; j<vec.size()-1; j++)
			{
				if(vec.get(i) != null && vec.get(j) != null)
				{
				if(LocationCompare(vec.get(i), vec.get(j)))
				{
					System.out.println(LocationCompare(vec.get(i), vec.get(j)));
					String Store = "";
					//System.out.println(vec.get(i));
					//System.out.println(vec.get(j));
					Store = vec.get(i);
					
					vec.add(i, vec.get(j));
					vec.remove(i+1);
					vec.add(j, Store);
					vec.remove(j+ 1);
				}
				}
			}
		}
		
		PrintWriter pw = new PrintWriter(file1);
		
		
		for(int i=0; i<vec.size(); i++)
		{
			System.out.println(vec.get(i));
			pw.println(vec.get(i));
		}
		
		pw.close();
	}
	
	
	public boolean LocationCompare(String i, String j)
	{
		int one1 = 0;
		int two1 = 0;
		int three1 = 0;
		
		int one2 = 0;
		int two2 = 0;
		int three2 = 0;
		
		String Temp1 = "";
		String Temp2 = "";
		String Temp3 = "";
		
		int count = 0;
		
		boolean check = true;
		
		//System.out.println(i);
		//System.out.println(j);
		
		
		
		for(int k=0; k<i.length(); k++)
		{
			if((int)i.charAt(k) < 58 &&  (int)i.charAt(k) > 47)
			{
				if(count == 0)
				{
					Temp1 += i.charAt(k);
				}
				else if(count == 1)
				{
					Temp2 += i.charAt(k); 
				}
				if(count == 2)
				{
					Temp3 += i.charAt(k);
				}
			}
			else if((int)i.charAt(k) == 58)
			{
				if(check)
				{
					count += 1;
					check = false;
				}
			}
			else if((int)i.charAt(k) == 45)
			{
				count += 1;
			}
		}
		
		/*System.out.println("Temp 1 " + Temp1);
		System.out.println("Temp 2 " + Temp2);
		System.out.println("Temp 3 " + Temp3);*/
		
		if(Temp1.equals(""))
		{
			return false;
		}
		
		one1 = Integer.parseInt(Temp1);
		two1 = Integer.parseInt(Temp2);
		three1 = Integer.parseInt(Temp3);
		
		check = true;
		count = 0;
		
		Temp1 = "";
		Temp2 = "";
		Temp3 = "";
		
		for(int k=0; k<j.length(); k++)
		{
			
			if((int)j.charAt(k) < 58 &&  (int)j.charAt(k) > 47)
			{
				if(count == 0)
				{
					Temp1 += j.charAt(k);
				}
				else if(count == 1)
				{
					Temp2 += j.charAt(k); 
				}
				if(count == 2)
				{
					Temp3 += j.charAt(k);
				}
			}
			else if((int)j.charAt(k) == 58)
			{
				if(check)
				{
					count += 1;
					check = false;
				}
			}
			else if((int)j.charAt(k) == 45)
			{
				count += 1;
			}
			else if(count == 2)
			{
				count += 1;
			}
		}
		
		if(Temp1.equals(""))
		{
			return true;
		}
		one2 = Integer.parseInt(Temp1);
		two2 = Integer.parseInt(Temp2);
		three2 = Integer.parseInt(Temp3);
		
		/*System.out.println(one1);
		System.out.println(two1);
		System.out.println(three1);
		
		

		System.out.println(one2);
		System.out.println(two2);
		System.out.println(three2);*/
		
		if(one1 > one2)
		{
			return true;
		}
		else if(one2 > one1)
		{
			return false;
		}
		else if(one2 == one1)
		{
			if(two1 > two2)
			{
				return true;
			}
			else if(two2 > two1)
			{
				return false;
			}
			else if(two1 == two2)
			{
				if(three1 > three2)
				{
					return true;
				}
				else if(three2 > three1)
				{
					return false;
				}
				else if(three1 == three2)
				{
					return false;
				}
			}
		}
		
		
		
		return true;
	}
	
	
	
	
	
}

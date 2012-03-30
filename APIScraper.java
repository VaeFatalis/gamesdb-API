
import java.net.*;
import java.io.*;
import java.util.*;

public class APIScraper {
	
	static public String[] nameList;
	static public File data = new File("data.txt");
	static public Game game = null;
	
	public static void main(String argv[]) throws Exception {
		
		Scanner in = new Scanner(new FileReader("C:\\Classwork\\eclipse Java\\gamesdb\\src\\namelist.txt"));
		int j = in.nextInt();
		PrintWriter out = new PrintWriter(new FileWriter("C:\\Classwork\\eclipse Java\\gamesdb\\src\\sqltext2.txt"));		
		
		/*
		String title = "Super Mario Bros 2";
		String platform = "NES"; 
		int year = 1987; 
		String esrb = "E for Everyone";
		String[] genre = {"Action","Platformer"};
		String description = "Mario jumps on things to save the Princess"; 
		String developer = "Nitendo LTD"; 
		String producer = "Nitendo LTD";
		*/
		
		//Game game = new Game(title, platform, year, esrb, genre, description, developer, producer);
		//game.printToSQL(out);

		nameList = new String[j];
		in.nextLine();
		
		for (int i = 0; in.hasNextLine();i++)
		{
			nameList[i] = in.nextLine().trim();
		}
		/*
		URLGrabber blank = new URLGrabber(new URL("http://thegamesdb.net/api/GetGame.php?name=" + nameList[0]));
		BufferedReader buff = blank.getBuff();
		String next = buff.readLine();
		while(next != null)
		{
			System.out.println(next);
			next = buff.readLine();
		}
		*/
		
		/*
		int i = 5;
		System.out.println("http://thegamesdb.net/api/GetGame.php?name=" + nameList[i]);
		URLGrabber blank = new URLGrabber(new URL("http://thegamesdb.net/api/GetGame.php?name=" + nameList[i].replaceAll(" ", "%20")));
		game = scrape(blank.getBuff(), nameList[i]);
		System.out.println(game.getPlatform() + " " + i);
		*/
		
		for (int i = 0; i < 10/*nameList.length*/; i++)
		{
			URLGrabber blank = new URLGrabber(new URL("http://thegamesdb.net/api/GetGame.php?name=" + nameList[i].replaceAll(" ", "%20")));
			game = scrape(blank.getBuff(), nameList[i]);
			// System.out.println(game.toString() + " " + i);
			game.printToSQL(out);
			System.out.println(i);
		}
		
		out.close();
		/*
		for(int k = 0; k < nameList.length ; k++)
		{
			System.out.println(nameList[k]);
		}
		*/
	}
	
	public static Game scrape(BufferedReader buff, String title)
	{
		Game game = new Game();
		String data = "";
		game.setTitle(title);
		//System.out.println(title);
		try
		{
			String next = buff.readLine();
			while(next != null)
			{
				data = data + next;
				next = buff.readLine();
			}
			//System.out.println(data);
			StringTokenizer st = new StringTokenizer(data,"<>");
			next = st.nextToken();
			while(st.hasMoreTokens())
			{
				//System.out.println(next);
				// checks the page against the title
				if(uContains(next , title ,""))
				{
					next = st.nextToken();
					//System.out.println(next);
					while(true)
					// finds platform
					{
						// looks for platform keyword
						if(uContains(next, "platform",""))
						{
							game.setPlatform(st.nextToken());
							break;
						}
						// check to see if platform is not included
						if(uContains(next, "ReleaseDate","") || uContains(next, "Overview","") 
								|| uContains(next, "ESRB","") || uContains(next, "Genres", "") 
								|| uContains(next, "Publisher","") || uContains(next, "Developer",""))
							break;
						// check to see if end of page has been reached
						if(!st.hasMoreTokens())
							return game;
						else
							next = st.nextToken();
					}
					
					next = st.nextToken();
					while(true)
					// finds the year
					{
						// Look for ReleaseDate keyword
						if(uContains(next, "ReleaseDate",""))
						{
							next = st.nextToken();
							StringTokenizer datest = new StringTokenizer(next, "/ ");
							int test = 0;
							try
							{
								test = Integer.parseInt(datest.nextToken());
							}
							catch(NumberFormatException e)
							{
								test = 0;
								datest.nextToken();
							}
							
							while(true)
							{
								//System.out.println(test);								
								if (test > 31)
								{
									game.setYear(test);
									break;
								}
								else if(!datest.hasMoreTokens())
								{
									System.out.println("No Find");
									return game;
								}
								else
								{
									try
									{
										test = Integer.parseInt(datest.nextToken());
									}
									catch(NumberFormatException e)
									{
										test = 0;
										datest.nextToken();
									}
								}
							}
							
							break;
						}
						
						// check to see if date is not included
						if(uContains(next, "Overview","") || uContains(next, "ESRB","") 
								|| uContains(next, "Genres", "") || uContains(next, "Publisher","") 
								|| uContains(next, "Developer",""))
							break;
						// check to see if end of page has been reached
						if(!st.hasMoreTokens())
							return game;
						else
							next = st.nextToken();
					}
					
					while(true)
					// finds Overview (aka description)
					{
						if (uContains(next, "Overview",""))
						{
							next = st.nextToken();
							game.setDescription(next);
							next = st.nextToken();
							break;
						}
						if (uContains(next, "ESRB","") || uContains(next, "Genres", "") 
								|| uContains(next, "Publisher","") || uContains(next, "Developer",""))
							break;
						if(!st.hasMoreTokens())
							return game;
						else
							next = st.nextToken();
					}
					
					while(true)
					// finds ESRB
					{
						if (uContains(next, "ESRB",""))
						{
							next = st.nextToken();
							game.setESRB(next);
							next = st.nextToken();
							break;
						}
						if (uContains(next, "Genres", "") || uContains(next, "Publisher","") 
								|| uContains(next, "Developer",""))
							break;
						if(!st.hasMoreTokens())
							return game;
						else
							next = st.nextToken();
					}
					
					while(true)
					// finds genres
					{
						if (uContains(next, "Genres", ""))
						{
							String temp = "";
							int count = 0;
							while(!uContains(next, "/genres",""))
							{
								next = st.nextToken();
								temp = temp + "^" + next;
								if(uContains(next, "genre",""))
									count++;
							}
							//System.out.println(temp + " " + count);
							String[] genre = new String[count];
							
							StringTokenizer genreST = new StringTokenizer(temp, "^");
							int i = 0;
							while(genreST.hasMoreTokens())
							{
								next = genreST.nextToken();
								if(uContains(next, "genre",""))
								{
									next = genreST.nextToken();
									// System.out.println(next + " " + i);
									genre[i] = next;
									i++;
								}
							}
							
							//for(int j = 0; j < genre.length; j++)
								//System.out.println(genre[j]);
							
							game.setGenre(genre);
							next = st.nextToken();
							break;
						}
						if (uContains(next, "Publisher","")	|| uContains(next, "Developer",""))
							break;
						if(!st.hasMoreTokens())
							return game;
						else
							next = st.nextToken();
					}
					
					while(true)
					// finds publisher
					{
						if (uContains(next, "Publisher",""))
						{
							next = st.nextToken();
							game.setPublisher(next);
							next = st.nextToken();
							break;
						}
						if (uContains(next, "Developer",""))
							break;
						if(!st.hasMoreTokens())
							return game;
						else
							next = st.nextToken();
					}
					
					while(true)
					// finds developer
					{
						if (uContains(next, "Developer",""))
						{
							next = st.nextToken();
							game.setDeveloper(next);
							break;
						}
						if(!st.hasMoreTokens())
							return game;
						else
							next = st.nextToken();
					}
					
					break;
				}
				// check to see if end of page has been reached
				if(!st.hasMoreTokens())
					return game;
				else
					next = st.nextToken();
			}
		}
		catch(IOException e){e.printStackTrace();}
		return game;
	}
	
	private static boolean uContains(String string, String test, String delim)throws IOException
	{
		boolean isFound = false;
		StringTokenizer st = new StringTokenizer(string, delim);
		while(st.hasMoreTokens() && !isFound)
		{
			String next = st.nextToken();
			if(next.equalsIgnoreCase(test))
				isFound = true;
		}
      
		return isFound;
	}
	

}


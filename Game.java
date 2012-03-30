import java.util.*;
import java.io.*;
import java.net.*;


public class Game {
	private String title;
	private String platform; 
	private int year; 
	private String esrb;
	private String[] genre;
	private String description; 
	private String developer; 
	private String publisher;
	
	public Game()
	// Gratuitous constructor
	{
		title = null;
		platform = null; 
		year = 0; 
		esrb = null;
		genre = null;
		description = null; 
		developer = null; 
		publisher = null;
	}

	public Game(String nTitle, String nPlatform, int nYear, String nesrb, String[] nGenre, String nDescription, String nDeveloper, String nPublisher)
	// constructor
	{
		title = nTitle;
		platform = nPlatform; 
		year = nYear; 
		esrb = nesrb;
		description = nDescription; 
		developer = nDeveloper; 
		publisher = nPublisher;
		
		genre = new String[nGenre.length];
		
		for(int i = 0; i < nGenre.length ; i++)
			genre[i] = nGenre[i];
	}
	

	// title
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String nTitle)
	{
		title = nTitle;
	}
	
	// platform
	public String getPlatform()
	{
		return platform;
	}
	
	public void setPlatform(String nPlatform)
	{
		platform = nPlatform;
	}
	
	// year
	public void setYear(int date)
	{
		year = date;
	}
	
	public int getYear()
	{
		return year;
	}
	
	// esrb
	public String getESRB()
	{
		return esrb;
	}
	
	public void setESRB(String nesrb)
	{
		esrb = nesrb;
	}
	
	// description
	public void setDescription(String nDescription)
	{
		description = nDescription;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	// developer
	public void setDeveloper(String nDeveloper)
	{
		developer = nDeveloper;
	}
	
	public String getDeveloper()
	{
		return developer;
	}
	
	// publisher
	public void setPublisher(String nPublisher)
	{
		publisher = nPublisher;
	}
	
	public String getPublisher()
	{
		return publisher;
	}
	
	// genre
	public void setGenre(String[] nGenre)
	{
		genre = new String[nGenre.length];
		for(int i = 0; i < nGenre.length ; i++)
			genre[i] = nGenre[i];
	}
	
	public String genreToString()
	{
		String stringGenre = "";
		
		if (genre == null)
			return null;
		
		for(int i = 0; i < genre.length; i++)
		{
			stringGenre = stringGenre + genre[i];
			if (i < genre.length - 1)
				stringGenre = stringGenre + ",";
		}
		return stringGenre;
	}
	
	public void printToSQL(PrintWriter out)
	{
		String sGenre = genreToString();
		String nDescription = null;
		if (description != null)
			nDescription = description.replaceAll("\'", "\'\'");
		out.println("INSERT INTO Game (title, platform, year, esrb, genre, publisher, developer, description)");
		out.printf("VALUES ('%s', '%s', %d, '%s', '%s', '%s', '%s', '%s');\n",title,platform,year,esrb,sGenre, publisher, developer, nDescription);		
	}
	
	public String toString()
	{
		return title + ", " + platform + ", " + year + ", \n<" + description + ">\n" + esrb + ", " + genreToString() + ", " + publisher + ", " + developer;
	}
}

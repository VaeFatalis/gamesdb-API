
import java.net.*;
import java.io.*;

public class URLGrabber
{
   // variable declarations
   static URL url = null;
   static URLConnection urlConn = null;
   static InputStreamReader  inStream = null;
   static BufferedReader buff = null;
   
   public URLGrabber()
   // gratuitous constructor, unused since no way to add a URL
   {
      url = null;
      urlConn = null;
      inStream = null;
      buff = null;
   }
   
   public URLGrabber(URL newURL)
   // constructor that opens a connection to the URL object in the parameter
   {
     try
     {
      url = newURL;
      urlConn = url.openConnection();
      inStream = new InputStreamReader(urlConn.getInputStream());
      buff= new BufferedReader(inStream);
     }
     catch(MalformedURLException e){e.printStackTrace();}
     catch(IOException e){e.printStackTrace();}
   }
   
   public BufferedReader getBuff()
   {
	   return buff;
   }
   
   public URL getURL()
   {
	   return url;
   }
   
   public void writeToFile(File file)
   // writes URL source code to parameter file
   {
      String nextLine;
      PrintWriter outFile = null;
      try
      {
         outFile = new PrintWriter(new FileWriter(file));
   
         while (true)
         {
            nextLine =buff.readLine();  
            if (nextLine !=null)
            {
                outFile.println(nextLine); 
            }
            else
            {
               break;
            }
         }
      }
      catch(IOException e){e.printStackTrace();}
      finally
      {
         outFile.close();         
      }
   }
   
}

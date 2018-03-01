import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.Element;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.Arrays;

/**
 *
 * @author Sahar
 */
public class CrawlerThread implements Runnable { 
     final String userAgent="Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0";
     final static int stoppingCriteria = 5000;
     static int FetchedCount=0;
     //static Queue<Document> DocumentsQueue=new LinkedList<>(); //list of fetched documents with their url
     static Queue<String> urlsQueue=new LinkedList<>(); //queue of unique urls to be crawled
     static Set<String> seedsSet = new HashSet<>();//set of unique links returns  false for duplicate elements
    
    
    public static void saveURL(URL url, OutputStream os)throws IOException {
		InputStream in = url.openStream();
		byte[] buf = new byte[1048576];
		int n = in.read(buf);
                //byte[] testbytes=test.getBytes();
                // os.write(testbytes,0,3);
                byte[] urlBytes=((url.toString())+System.lineSeparator()).getBytes(); //change url string to bytes
                int n1=urlBytes.length;
                os.write(urlBytes,0,n1); 
                
		while (n != -1) {
			os.write(buf, 0, n);
			n = in.read(buf);
		}
	}
        /**
	 * Writes the contents of the url to a new file by calling saveURL with
	 * a file writer as argument
     * @param url
     * @param filename
     * @throws java.io.IOException
	 */
        public static void writeURLtoFile(URL url, String filename)
		throws IOException {
		
		FileOutputStream os = new FileOutputStream(filename);
		saveURL(url, os);
		os.close();
	}
  
        
        Document getHmlDocument(String url)
    {
       Document doc = null;
        try
    {
        // connect to url, set 1 s timeout and send a get requesst so html document is returned
     Connection conn=Jsoup.connect(url).userAgent(userAgent);
     Connection.Response response=conn.url(url).timeout(1000).execute();
    //check if type  html document only------->>>>later
    URL URL=new URL(url);
    if(!response.contentType().contains("text/html"))
        return null;
   
//doc=conn.get();
//DocumentsQueue.add(doc);
   writeURLtoFile(URL, Integer.toString(FetchedCount)+".txt");
    return doc; 
    }
    catch(IOException e)
    {
        return null;
    }
     // return doc;  
    }
    
    /**
     * retrevs all document links
     */
    private  Vector<String> getLinks(Document doc) {
     Vector<String> result=new Vector<String>();
      Elements links=doc.select("a[href]"); // get all elements with href
    for(   org.jsoup.nodes.Element link: links)
        {
          result.add(link.attr("abs:href")); // removing <a tag and href attribute
        }
      return result;
   }
  
    /** 
     * pops a link from queue 
     */
    void crawl() throws MalformedURLException{
      //get / pop a url from qeue
        String url,link;
        Document doc;
   
        while(FetchedCount<stoppingCriteria)
        {            
          synchronized(urlsQueue)
          {
            url=urlsQueue.poll();//get a url from ueue to process if queue is emepty url =null 
          }
            if(url!=null)//queue is not empty
            {
                
                
               if(Robot.isRobotAllowed(url))
               {
                //save and get document from url here since url from queue so document is unique
                 doc = getHmlDocument(url);
                //extarct links from this document
                if(doc!=null) //null if not html
                {
                 Vector<String> links=getLinks(doc);

                //add seedSet  if not duplicate add each to the queue
                for(int i=0;i<links.size();i++)
                {  
                    link=links.get(i);
                   if(seedsSet.add(link))
                   {
                       synchronized(urlsQueue)
                       {
                        urlsQueue.add(link);//adding only links that are unique to be crawled
                        FetchedCount++;
                       }
                   }
                 }
                   
                }
               }
             

            }   
        }
    }
     
        @Override
    public void run()
    {
         try {
             crawl();
         } catch (MalformedURLException ex) {
             Logger.getLogger(CrawlerThread.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
    
    
    
}

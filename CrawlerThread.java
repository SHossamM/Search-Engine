import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
/**
 *
 * @author Sahar
 */
public class CrawlerThread implements Runnable { 
     final String userAgent="Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0";
     final static int stoppingCriteria = 5000;
     static int FetchedCount=0;
     static Queue<Document> DocumentsQueue=new LinkedList<>(); //list of fetched documents with their url
    public  Queue<String> urlsQueue;
     static Set<String> visited = new HashSet<>();//set of unique links that are visited returns  false for duplicate elements
       CrawlerThread(  Queue<String> urlsQueue)
       {
           this.urlsQueue=urlsQueue;
       }
    /**
     * if it is html it download it as a document and adds it to a queue 
     * @param url
     * @return  null if not html document
     */ 
        Document getHmlDocument(String url)
    {
       Document doc = null;
        try
    {
        // connect to url, set 1 s timeout and send a get requesst so html document is returned
     Connection conn=Jsoup.connect(url).userAgent(userAgent);
     Connection.Response response=conn.url(url).timeout(1000).execute();
     URL URL=new URL(url);
    if(!response.contentType().contains("text/html"))
        return null;
   
    doc=conn.get();
    synchronized(DocumentsQueue)
    {
        DocumentsQueue.add(doc);
        System.out.println(Thread.currentThread().getName()+"Added a document to the queue");
        Url.writeURLtoFile(URL, Integer.toString(DocumentsQueue.size()-1)+".html");
         FetchedCount++;
         System.out.println("Count= "+FetchedCount);
    }
     
     
    return doc; 
    }
    catch(IOException e)
    {
        return null;
    }
     // return doc;  
    }
    
    /**
     * retrevs all document  in a vector of strings
     */
    private  Vector<String> getLinks(Document doc) {
     Vector<String> result=new Vector<String>();
      Elements links=doc.select("a[href]"); // get all elements with href
    for(   org.jsoup.nodes.Element link: links)
        {
          result.add(link.attr("abs:href")); // removing <a tag and href attribute
        }
        System.out.println(Thread.currentThread().getName()+" Extracted Links");       
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
      //  System.out.println("Thread id: "+Thread.currentThread().getName()+ " tried to pop off the queue");
      url=urlsQueue.poll();//get a url from ueue to process if queue is emepty url =null 
    }
      if(url!=null)//queue is not empty
        {
           System.out.println(Thread.currentThread().getName()+ " popped a url from the queue");
           if(Url.verifyUrl(url)!=null) //check1 that the url is verified
            {
                if(url.contains("#"))  //check2 removing hashes as they mean same page
                {
                    url=url.substring(0,url.indexOf('#'));
                }
                if(Robot.isRobotAllowed(url,userAgent)) //check3 RobotAllowed
                {
                if(visited.add(url)) //check4 not duplicate and visited before
                {
                 doc = getHmlDocument(url);
                //extarct links from this document
                if(doc!=null) //null if not html //check5 html document?
                {
                 Vector<String> links=getLinks(doc);

                //add visited  if not duplicate add each to the queue
                for(int i=0;i<links.size();i++)
                {
                    link=links.get(i);
//                   if(visited.add(link))
//                   {
                       synchronized(urlsQueue)
                       {
                        urlsQueue.add(link);                        
                       }
//                   }
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

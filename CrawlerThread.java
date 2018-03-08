import java.io.*;
import static java.lang.System.exit;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
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
     final String userAgent="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
     final static int stoppingCriteria = 5000;
     final static int domainMax=20;
     DataBase dB;
     boolean v;
    //  Integer fetchedCount; //refrence to controllers count
     //static Queue<Document> DocumentsQueue=new LinkedList<>(); //list of fetched documents with their url-->removed as consumes space
     public  Queue<String> urlsQueue; //refernce to that of controller's
     public Queue<String> visitedBackup;
     static Set<String> visited= new HashSet<>() ;//set of unique links that are visited returns  false for duplicate elements
     //static Set<String> tovisit=new HashSet<>(); //to avoid saving duplicates to visit
       CrawlerThread(  Queue<String> urlsQueue, Queue<String> visitedBackup,DataBase db)
       {
           this.urlsQueue=urlsQueue;
           this.visitedBackup=visitedBackup;
           this.dB=db;
      
         //  fetchedCount=fc;
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
             Connection.Response response=conn.url(url).timeout(1500).execute();
             URL URL=new URL(url);
             if(response.contentType()!=null)
             {
            if(!response.contentType().contains("text/html"))
                return null;
             }
      
            doc=conn.get();
            synchronized(dB) //because of retrived count
            {
              Url.writeURLtoFile(URL, Integer.toString(dB.GetVisitedCount()+1)+".html");
              //dB.InsertLink(url);//insert this link in db as visited----->checkkk
              dB.MarkVisited(url);
                System.out.println(Thread.currentThread().getName()+"marked "+ url +"as visited and downloaded its web content");
              
             // fetchedCount++;
              System.out.println("Count= "+dB.GetVisitedCount());     
              }
             return doc; 
         }
        catch(IOException | NullPointerException e) //-->ask whetehr null pointer returns null or not
        {
            return null;
        }
          
           
                
    }
    
    /**
     * retrevs all document  in a vector of strings
     */
    private  Vector<String> getLinks(Document doc, String domain) {
     Vector<String> result=new Vector<String>();
      Elements links=doc.select("a[href]"); // get all elements with href
      String extractedUrl;
        int domainCount=0;
    for(  org.jsoup.nodes.Element link: links)
        {
         extractedUrl=link.attr("abs:href").toLowerCase();
         if(extractedUrl.contains("#") || extractedUrl.isEmpty()) //empty | # refers to current document too
             continue;

         if(extractedUrl.contains(domain))
         {
           if(domainCount>domainMax)
             continue;
           domainCount++;
         }
         if(dB.GetVisitedCount()<stoppingCriteria)
         {
           result.add(extractedUrl); // removing <a tag and href attribute
         }
        
        }
        System.out.println(Thread.currentThread().getName()+" Extracted Links");       
      return result;
   }
  
    /** 
     * pops a link from queue 
     */
     
void crawl() throws MalformedURLException, InterruptedException, SQLException{
      //get / pop a url from qeue
        String url,link;
        Document doc;

 while(!urlsQueue.isEmpty())
   {            
            synchronized(urlsQueue)
            {
              //  System.out.println("Thread id: "+Thread.currentThread().getName()+ " tried to pop off the queue");
              url=urlsQueue.poll();//get a url from ueue to process if queue is emepty url =null 
              if(url==null)
              {
        //          urlsQueue.wait();
        //          url=urlsQueue.poll();
               System.out.println(Thread.currentThread().getName()+ "existing");
               return; //thread stops??? yes it terminates :)

              }
              System.out.println(Thread.currentThread().getName()+ " popped "+url+" from the URLqueue");
            }
              if(url!=null)//queue is not empty
                {

                   if(Url.verifyUrl(url)!=null) //check1 that the url is verified
                    {
                        if(url.contains("#"))  //check2 removing hashes as they mean same page
                        {
                            url=url.substring(0,url.indexOf('#'));
                        }
                        URL URL=new URL(url);
                        String  host= URL.getHost().toLowerCase();//get the host of this url which is the domain name i.e http://codeproject.com return codeproject.com 
                        if(Robot.isRobotAllowed(url,userAgent,host)) //check3 RobotAllowed
                        {
                            synchronized(visited)
                            {
                             v=  visited.add(url); //e7tmalha d3ef enoh yo23 hna bs bardo mmkn acheck hna w a7oto w yb2a visited bs mafesh 7aga nzlt bs bardo m7lola mn n7yet el db l2noh lsa ma b2sh visited:)
                            }
                        if(v) //check4 not duplicate and visited before
                        {

                                doc = getHmlDocument(url); //MARK the link as visited IN DB
                            
                            if(doc!=null) //null if not html //check5 html document?
                            {
                                //extarct links from this document
                             Vector<String> links=getLinks(doc,host);

                            //add to be visited  if not duplicate add each to the db
                            for(int i=0;i<links.size();i++)
                            {
                                link=links.get(i);
                                synchronized(visitedBackup)
                                {
                                   // if(!dB.CheckUrlExists(link)) //if link not added  before add to  db as not
                                     // {
                                        visitedBackup.add(link);//7l fe el nos 3shan link link f db bybt2aha awyy :(
                                        //  dB.InsertLink(link); //add to db as not visited--->check
                                      //}
                                }
                            }
                             }

                            }
                        System.out.println(url+"is duplicate detected by"+Thread.currentThread().getName());
                        continue;
                         }
                        System.out.println(url+"is robot disallowed "+Thread.currentThread().getName());
                        continue;
                        }
                    System.out.println(url+"is unverified by "+Thread.currentThread().getName());

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
         } catch (InterruptedException ex) {
             Logger.getLogger(CrawlerThread.class.getName()).log(Level.SEVERE, null, ex);
         } catch (SQLException ex) {
             Logger.getLogger(CrawlerThread.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
    
    
    
}

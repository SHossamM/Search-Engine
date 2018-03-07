import java.net.*;
import java.io.*;
import java.util.Queue;

public class SearchEngine {
    public static void main(String[] args) throws Exception {

      CrawlController c=new CrawlController(10);
     // c.urlsQueue.add("https://www.yahoo.com");
      //c.urlsQueue.add("https://who.is/");
      //c.urlsQueue.add("http://www.web-directories.ws/");
     c.Start();
    //CrawlerThread ct=new CrawlerThread();
    
    // Thread t=new Thread(ct);
    // t.start();
      
      //__.shofy mwdo3 enk tkony el 3 browsers m3 b3d
//      DataBase db=new DataBase();
//      Queue<String> seedSet=db.RetriveNonVisited();
//      for(int i=0;i<seedSet.size();i++)
//      {
//          System.out.println(seedSet.poll());
//                  
//      }
   }
   
}
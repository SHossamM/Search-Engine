import java.net.*;
import java.io.*;

public class SearchEngine {
    public static void main(String[] args) throws Exception {

      CrawlController c=new CrawlController(3);
      c.urlsQueue.add("https://www.codeproject.com");
      c.urlsQueue.add("https://who.is/");
      c.Start();
    // CrawlerThread ct=new CrawlerThread();
    
    // Thread t=new Thread(ct);
    // t.start();
      
      
    }
   
}
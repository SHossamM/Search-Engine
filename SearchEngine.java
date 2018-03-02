import java.net.*;
import java.io.*;

public class SearchEngine {
    public static void main(String[] args) throws Exception {

      //CrawlController c=new CrawlController(1);
      //c.Start();
     CrawlerThread ct=new CrawlerThread();
     ct.urlsQueue.add("https://www.codeproject.com");
     Thread t=new Thread(ct);
     t.start();
      
      
    }
   
}
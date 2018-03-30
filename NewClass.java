
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sun.util.calendar.CalendarDate;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sahar
 */
public class NewClass {
    
  
     public static void main(String[] args) throws Exception {
        DataBase dB;
        ConcurrentLinkedQueue<Url> urlsQueue;
            dB = new DataBase();
           urlsQueue = dB.RetriveNonVisited();
         
           String s="oooo";
           
           System.out.println(urlsQueue.size());
         Url url=urlsQueue.poll();
         Document d;
      d = x.getHTMLDocument(url);

     }
  
}

 class x{
  public static  Document getHTMLDocument(Url url) {
        try {
            Connection conn = Jsoup.connect(url.getUrl()).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1");
//            Connection.Response response;
//            response = conn.url(url.toString()).timeout(10000).execute();
//             if(response.contentType()!=null)
//             {
//               if(!response.contentType().contains("text/html"))
//                 return null;
//             }
            Document doc = conn.get();
            File f = new File("./pages/" + Integer.toString(url.getId()) + ".html");
            FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");
               url.setHash(doc.outerHtml().toString().hashCode()) ;
               System.out.println(url.getHash());
               //HashQueue.add(url);
            //System.out.println(Thread.currentThread().getName() + " marked " + url.getId() + " as visited and downloaded its web content");
            return doc;
        } catch (IOException | NullPointerException e) { //-->ask whether null pointer returns null or not
            return null;
        }
    }
}  





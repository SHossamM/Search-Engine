import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
//import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class CrawlerThread implements Runnable  {
    final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    final static int domainMax = 20;
    public ConcurrentLinkedQueue<Url> urlsQueue; //refernce to that of controller's
    public ConcurrentLinkedQueue<Url> HashQueue;
    public ConcurrentLinkedQueue<OutgoingLinks> outgoingLinks;
    public ConcurrentLinkedQueue<Integer> visitedLinks;
    ConcurrentLinkedQueue<Integer> notVerified;
    ConcurrentLinkedQueue<Integer> notRobotallowed;
    ConcurrentLinkedQueue<Integer> notParseable;

    CrawlerThread(ConcurrentLinkedQueue<Url> urlsQueue,
                  ConcurrentLinkedQueue<Url> HashQueue,
                  ConcurrentLinkedQueue<OutgoingLinks> outgoingLinks,
                  ConcurrentLinkedQueue<Integer> visitedLinks,
                  ConcurrentLinkedQueue<Integer> notVerified,
                  ConcurrentLinkedQueue<Integer> notRobotallowed,
                  ConcurrentLinkedQueue<Integer> notParseable)  {
        this.urlsQueue = urlsQueue;
        this.HashQueue=HashQueue;
        this.outgoingLinks = outgoingLinks;
        this.visitedLinks = visitedLinks;
        this.notVerified = notVerified;
        this.notRobotallowed = notRobotallowed;
        this.notParseable = notParseable;
       // date=sdf.parse("1980-01-01");
    }

    /**
     * if it is html it download it as a document and adds it to a queue
     *
     * @param url
     * @return null if not html document
     */
    Document getHTMLDocument(Url url) {
        try {
            Connection conn = Jsoup.connect(url.getUrl()).userAgent(userAgent);
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
            //System.out.println(Thread.currentThread().getName() + " marked " + url.getId() + " as visited and downloaded its web content");
            return doc;
        } catch (IOException | NullPointerException e) { //-->ask whether null pointer returns null or not
            return null;
        }
    }

    /**
     * retrieves all document  in a vector of strings
     */
    private List<String> getLinks(Document doc, String domain) {
        List<String> result = new LinkedList<>();
        Elements links = doc.select("a[href]"); // get all elements with href
        String extractedUrl;
        int domainCount = 0;
        for (org.jsoup.nodes.Element link : links) {
            extractedUrl = link.attr("abs:href").toLowerCase();
            if (extractedUrl.contains("#") || extractedUrl.isEmpty()) { //empty | # refers to current document too
                continue;
            }
            if (extractedUrl.contains(domain)) {
                if (domainCount > domainMax) {
                    continue;
                }
                domainCount++;
            }
            result.add(extractedUrl); // removing <a tag and href attribute
        }
        //System.out.println(Thread.currentThread().getName() + " Extracted Links");
        return result;
    }

    /**
     * pops a link from queue
     */

    void crawl() throws MalformedURLException {
        //get / pop a url from queue
        Url url;
        Document doc;

        while ((url = urlsQueue.poll()) != null) {

            //Get a url to crawl

            System.out.println(Thread.currentThread().getName() + " popped " + url.getId() + " : " + url.getUrl() + " from the urlsQueue");
        
         if( url.getDate().toString().contains("1905") || url.getRank()>0){     
            
            if (url.verifyUrl() != null) {//check1 that the url is verified

                if (url.getUrl().contains("#")) {//check2 removing hashes as they mean same page
                    url.setUrl(url.getUrl().substring(0, url.getUrl().indexOf('#')));
                }
                String host = (new URL(url.getUrl())).getHost().toLowerCase();//get the host of this url which is the domain name i.e http://codeproject.com return codeproject.com

                if (Robot.isRobotAllowed(url.getUrl(), userAgent, host)) {//check3 RobotAllowed
                    //System.out.println(Thread.currentThread().getName() + " trying to download " + url.getUrl());
                    doc = getHTMLDocument(url); //MARK the link as visited IN DB
                    if (doc != null) {//null if not html //check5 html document? 
                        //extract links from this document
                        //System.out.println(Thread.currentThread().getName() + " Adding links of: " + url.getUrl());
                        outgoingLinks.add(new OutgoingLinks(url.getId(), getLinks(doc, host)));
                        HashQueue.add(url);
                        //System.out.println(Thread.currentThread().getName() + " Added links of: " + url.getUrl());
                    } else {
                        //System.out.println(url.getUrl() + " is not parseable(not html) or not reachable" + Thread.currentThread().getName());
                        notParseable.add(url.getId());
                    }
                } else {
                    //System.out.println(url.getUrl() + " is robot disallowed " + Thread.currentThread().getName());
                    notRobotallowed.add(url.getId());
                }
            } else {
                //System.out.println(url.getUrl() + " is unverified by " + Thread.currentThread().getName());
                notVerified.add(url.getId());
            }
           
         }
          visitedLinks.add(url.getId()); //visited means checked today wether it is downloaded or not :)
        }
    }


    @Override
    public void run() {
        try {
            crawl();
            System.out.println(Thread.currentThread().getName() + " exiting");
        } catch (MalformedURLException ex) {
            Logger.getLogger(CrawlerThread.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(Thread.currentThread().getName() + " broken");
        }
    }
}

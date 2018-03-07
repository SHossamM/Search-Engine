
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Sahar
 */
public class CrawlController  {//crawler would be one of the threads in the whole search engine so it implements runable
  int threadsNumber;
  public Queue<String> urlsQueue=new LinkedList<>(); //queue of unique urls to be crawled
 
  private DataBase dB;
   // static Integer fetchedCount; -->not needed now since i check count from database ":)
 final int stoppingCriteria=5000;
  public CrawlController(int threadsNumber)
  {
      this.threadsNumber=threadsNumber;
       dB=new DataBase();
//       fetchedCount=0;
  }
  public CrawlController()
  {
      this.threadsNumber=10;
      dB=new DataBase();
  }

 public void Start() throws SQLException, InterruptedException //should create n threads and each crawl 
  {
//     while(dB.GetVisitedCount()<stoppingCriteria)
//     {
     urlsQueue=dB.RetriveNonVisited(); //get all non visited links from database and fetch in queue
      //System.out.println(urlsQueue);
      for(int i=0; i<threadsNumber ;i++)
      {
      Thread th=new Thread(new CrawlerThread(urlsQueue,dB));
      th.setName("Thread"+i);
      System.out.println("Thread"+i+" Created");
      th.start();  
      }
     //}
     while(true)
     {
         Thread.sleep(300000);
         synchronized(dB)
         {
         urlsQueue=dB.RetriveNonVisited();
         dB.notifyAll();
         
         }
         
     }
      
  }
}

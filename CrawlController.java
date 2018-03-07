
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

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
  public CrawlController(int threadsNumber)
  {
      this.threadsNumber=threadsNumber;
       dB=new DataBase();
  }
  public CrawlController()
  {
      this.threadsNumber=10;
      dB=new DataBase();
  }

 public void Start() throws SQLException //should create n threads and each crawl 
  {
     urlsQueue=dB.RetriveNonVisited();
      for(int i=0; i<threadsNumber ;i++)
      {
      Thread th=new Thread(new CrawlerThread(urlsQueue));
      th.setName("Thread"+i);
      System.out.println("Thread"+i+" Created");
      th.start();
          
      }
  }
}

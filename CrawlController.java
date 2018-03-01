/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sahar
 */
public class CrawlController implements Runnable {//crawler would be one of the threads in the whole search engine so it implements runable
  int threadsNumber;
  
  @Override
  public void run() //should create n threads and each crawl 
  {
      for(int i=0; i<threadsNumber ;i++)
      {
      Thread th=new Thread(new CrawlerThread());
      th.start();
          
      }
  }
}

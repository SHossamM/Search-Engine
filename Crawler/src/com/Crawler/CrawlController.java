package com.Crawler;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Sahar
 */
public class CrawlController {//crawler would be one of the threads in the whole search engine so it implements runable
    int threadsNumber;

    ConcurrentLinkedQueue<Url> urlsQueue; //queue of unique urls to be crawled
    ConcurrentLinkedQueue<OutgoingLinks> outgoingLinks = new ConcurrentLinkedQueue<>();
    ConcurrentLinkedQueue<Integer> visitedLinks = new ConcurrentLinkedQueue<>();
    ConcurrentLinkedQueue<Integer> notVerified = new ConcurrentLinkedQueue<>();
    ConcurrentLinkedQueue<Integer> notRobotallowed = new ConcurrentLinkedQueue<>();
    ConcurrentLinkedQueue<Integer> notParseable = new ConcurrentLinkedQueue<>();

    private DataBase dB;

    // static Integer fetchedCount; -->not needed now since i check count from database ":)
    final int stoppingCriteria = 5000;

    public CrawlController(int threadsNumber) {
        this.threadsNumber = threadsNumber;
        dB = new DataBase();
        //fetchedCount=0;
    }

    public CrawlController() {
        this.threadsNumber = 4;
        dB = new DataBase();
    }

    public void Start() throws SQLException, InterruptedException //should create n threads and each crawl
    {
        Instant startTime;
        Instant endTime;
        while (dB.GetVisitedCount() < stoppingCriteria) {
            System.out.println("--------------------------------------------");
            System.out.println("Fetching new links");
            startTime = Instant.now();
            urlsQueue = dB.RetriveNonVisited(); //get all non visited links from database and fetch in queue
            if(urlsQueue.isEmpty()){
                System.out.println("No more links to  retrieve");
                break;
            }
            endTime = Instant.now();
            System.out.println("Fetched new links in: " + Duration.between(startTime,endTime).getSeconds());
            System.out.println("-----------------");



            //System.out.println(urlsQueue);
            startTime = Instant.now();
            Thread[] threads = new Thread[threadsNumber];
            for (int i = 0; i < threadsNumber; i++) {
                threads[i] = new Thread(new CrawlerThread(urlsQueue, outgoingLinks, visitedLinks, notVerified, notRobotallowed, notParseable));
                threads[i].setName("Thread" + i);
                System.out.println("Thread" + i + " Created");
                threads[i].start();
            }

            for (Thread thread: threads) {
                thread.join();
            }

            endTime = Instant.now();
            System.out.println("Crawled 100 pages in: " + Duration.between(startTime,endTime).getSeconds());
            System.out.println("-----------------");


            System.out.println("Saving links");
            startTime = Instant.now();
            dB.InsertLinks(outgoingLinks);
            dB.MarkVisited(visitedLinks);
            dB.UnmarkVerified(notVerified);
            dB.UnmarkRobotallowed(notRobotallowed);
            dB.UnmarkParseable(notParseable);


            endTime = Instant.now();
            System.out.println("Saved links in: " + Duration.between(startTime,endTime).getSeconds());
            System.out.println("-----------------");
        }
    }
}

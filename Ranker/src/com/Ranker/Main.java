package com.Ranker;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {

    public static void main(String[] args) throws SQLException, InterruptedException {
        Instant instant;
        while (true) {
            instant = Instant.now();
            CrawlerDB crawlerDB = new CrawlerDB();
            System.out.println("Created DB connection in: " + Duration.between(instant, Instant.now()).toMillis());

            instant = Instant.now();
            ConcurrentLinkedQueue<PageConnection> testPR = crawlerDB.RetrieveToBeRanked();
            System.out.println("Retrieved connections in: " + Duration.between(instant, Instant.now()).toMillis());

            /*
            ConcurrentLinkedQueue<PageConnection> testPR = new ConcurrentLinkedQueue<>();
            testPR.add(new PageConnection(0, 1));
            testPR.add(new PageConnection(0, 2));
            testPR.add(new PageConnection(1, 2));
            testPR.add(new PageConnection(2, 0));
            */

            instant = Instant.now();
            PageGraph pageGraph = new PageGraph(testPR, false);
            pageGraph.computePageRanks(10, 0.85);
            System.out.println("Computed PageRank in: " + Duration.between(instant, Instant.now()).toMillis());

            //pageGraph.printPageRanks();

            instant = Instant.now();
            crawlerDB.UpdatePageRank(pageGraph.getPageRanks());
            crawlerDB.close();
            System.out.println("Updated Crawler DB PageRank in: " + Duration.between(instant, Instant.now()).toMillis());

            instant = Instant.now();
            IndexerDB indexerDB = new IndexerDB();
            indexerDB.updatePageRank(pageGraph.getPageRanks());
            System.out.println("Updated Indexer DB PageRank in: " + Duration.between(instant, Instant.now()).toMillis());
            indexerDB.close();

            Thread.sleep(60000);
        }
    }
}

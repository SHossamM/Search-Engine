package com.Ranker;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {

    public static void main(String[] args) throws SQLException, IOException{
	    Instant instant;
        instant = Instant.now();
        DataBase db = new DataBase();
        System.out.println("Created DB connection in: " + Duration.between(instant, Instant.now()).toMillis());

        instant = Instant.now();
        ConcurrentLinkedQueue<PageConnection> testPR = db.RetrieveToBeRanked();
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
        db.UpdatePageRank(pageGraph.getPageRanks());
        System.out.println("Updated Crawler DB PageRank in: " + Duration.between(instant, Instant.now()).toMillis());
    }
}

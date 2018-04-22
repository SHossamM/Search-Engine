package com.Ranker;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;


public class PageGraph {

    private HashMap<Integer, List<Integer>> invertedAdjacencyList;
    private HashMap<Integer, Integer> numberOfOutlinks;
    private HashMap<Integer, Double> pageRanks;
    Integer numberOfPages;

    public PageGraph(ConcurrentLinkedQueue<PageConnection> pageConnections, Boolean probabilityMode) {
        invertedAdjacencyList = new HashMap<>();
        numberOfOutlinks = new HashMap<>();
        pageRanks = new HashMap<>();
        if(probabilityMode) {
            numberOfPages = pageConnections.size();
        } else {
            numberOfPages = 1;
        }
        while (!pageConnections.isEmpty()) {
            PageConnection pageConnection = pageConnections.poll();
            Integer sourceId = pageConnection.getSourceId();
            Integer destinationId = pageConnection.getDestinationId();
            if (!pageRanks.containsKey(sourceId)) {
                pageRanks.put(sourceId, 1.0 / numberOfPages);
                invertedAdjacencyList.put(sourceId, new ArrayList<>());
                numberOfOutlinks.put(sourceId, 0);
            }
            if (!pageRanks.containsKey(destinationId)) {
                pageRanks.put(destinationId, 1.0 / numberOfPages);
                invertedAdjacencyList.put(destinationId, new ArrayList<>());
                numberOfOutlinks.put(destinationId, 0);
            }
            if(sourceId.equals(destinationId)){
                continue;
            }
            numberOfOutlinks.put(sourceId, numberOfOutlinks.get(sourceId) + 1);
            invertedAdjacencyList.get(destinationId).add(sourceId);
        }
    }

    public HashMap<Integer, Double> getPageRanks() {
        return pageRanks;
    }

    public void computePageRanks(Integer numOfIterations, Double dampingFactor) {
        System.out.println("Started computing page rank.");
        for (Integer i = 0; i < numOfIterations; i++) {
            System.out.println("Computing page rank. Iteration: " + i);
            //HashMap<Integer, Boolean> visited = new HashMap<>();
            HashSet<Integer> visited = new HashSet<>();
            for (Integer id : pageRanks.keySet()) {
                if (!visited.contains(id)) {
                    // Start BFS from this page
                    Queue<Integer> bfs = new LinkedList<>();
                    bfs.add(id);

                    while (!bfs.isEmpty()) {
                        Integer destinationId = bfs.poll();
                        visited.add(destinationId);

                        Double currPageRank = (1.0f - dampingFactor) / numberOfPages;

                        for (Integer sourcePageId : invertedAdjacencyList.get(destinationId)) {
                            currPageRank += dampingFactor * pageRanks.get(sourcePageId) / numberOfOutlinks.get(sourcePageId);
                            if (!visited.contains(sourcePageId)) {
                                bfs.add(sourcePageId);
                            }
                        }

                        pageRanks.put(destinationId, currPageRank);
                    }
                }
            }
        }
        System.out.println("Finished computing page rank.");
    }

    public void printPageRanks() throws IOException{
        String out = "";
        for (Integer id : pageRanks.keySet()) {
            //System.out.println(String.format("%d: %f", id, pageRanks.get(id)));
            out += id + "," + pageRanks.get(id)+"\n";
        }
        File f = new File("./out.csv");
        FileUtils.writeStringToFile(f, out, "UTF-8");
    }
}

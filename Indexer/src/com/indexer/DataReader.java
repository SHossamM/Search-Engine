package com.indexer;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.io.FileUtils;


public class DataReader {

    private String filesPath;
    private Queue<Url> urls;
    private CrawlerDB crawlerDB;

    public DataReader(String filesPath) {
        this.filesPath = filesPath;
        crawlerDB = new CrawlerDB();

        try {
            urls = crawlerDB.RetrieveUnindexed();
        } catch (Exception e) {
            System.out.println("Couldn't fetch urls: " + e.getMessage());
        }

    }

    public String readFile(Integer urlId) throws IOException {
        return FileUtils.readFileToString(new File(filesPath + "/" + Integer.toString(urlId) + ".html"), "UTF-8");
    }


    public DataDocument getNextDocument() {
        Url url;
        if ((url = urls.poll()) != null) {
            try {
                return new DataDocument(
                        url.getId(),
                        url.getUrl(),
                        readFile(url.getId())
                );
            } catch (Exception e) {
                System.out.println("Problem ------- " + url.getId());
                System.out.println("Problem ------- " + e.getLocalizedMessage());

                return new DataDocument(
                        url.getId(),
                        "",
                        ""
                );
            }
        }
        return null;
    }

    public void finalize(List<Integer> indexedUrls){
        crawlerDB.MarkIndexed(indexedUrls);
        try {
            crawlerDB.close();
        } catch (SQLException e){
            System.out.println("Error in closing database connection!");
        }
    }
}

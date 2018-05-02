package com.Crawler;


public class Main {

    public static void main(String[] args) throws Exception {
        CrawlController c = new CrawlController(10);
        while(true) {
            System.out.println("Start Crawling");
            c.Start();
            Thread.sleep(60000*5);
        }
    }
}

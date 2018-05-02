package com.Crawler;

import java.net.URL;


public class Url {

    private Integer id;
    private String url;
    private java.sql.Timestamp date;
    private Integer Rank;
    private Integer hash;
    private Integer news;

    public Url(Integer id, String url, java.sql.Timestamp date, Integer Rank, Integer hash, Integer news) {
        this.id = id;
        this.url = url;
        this.date = date;
        this.Rank = Rank;
        this.hash = hash;
        this.news = news;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public int getNews() {
        return news;
    }

    public java.sql.Timestamp getDate() {
        return date;
    }

    public int getRank() {
        return Rank;
    }

    public int getHash() {
        return hash;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHash(int Hash) {
        this.hash = Hash;
    }

    public void setNews(int news) {
        this.news = news;
    }

    /**
     * This methods verifies that the given url is an http url if the
     * url is malformed exception is thrown and a null is returned if the url is not valid or verified give MalformedURLException: no protocol:
     *
     * @param url
     * @return
     */

    public static URL verifyUrl(String url) {
        // Only allow HTTP URLs.
        if (url.isEmpty() || !(url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://"))) {
            return null;
        }
        // Verify format of URL.
        URL verifiedUrl = null;
        try {
            verifiedUrl = new URL(url);
        } catch (Exception e) {
            return null;
        }
        return verifiedUrl;
    }

    public URL verifyUrl() {
        return verifyUrl(url);
    }
}

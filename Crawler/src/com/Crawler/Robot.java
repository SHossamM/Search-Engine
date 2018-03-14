package com.Crawler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author Sahar
 */
public class Robot {
    static public ConcurrentHashMap disallowListCache = new ConcurrentHashMap();///////
    // Check if robot is allowed to access the given URL. 

    /**
     * Robot
     * checks whether robot is allowed or not
     *
     * @param url
     * @return true if robot is allowed
     * @throws java.net.MalformedURLException
     */
    public static boolean isRobotAllowed(String url, String userAgent1, String host) throws MalformedURLException {
        URL URL = new URL(url);

        // Retrieve host's disallow list from cache.
        ArrayList disallowList = (ArrayList) disallowListCache.get(host);
        // If list is not in the cache, download and cache it.

        if (disallowList == null) {
            disallowList = new ArrayList();
            try {
                URL robotsFileUrl = new URL("http://" + host + "/robots.txt"); // if robots.txt not found this returns an exception then robot is allowed
                // System.out.println("robotsFileUrl: "+robotsFileUrl);
                //open connection to robots file url for reading
                BufferedReader reader = null;
                URLConnection con = robotsFileUrl.openConnection();
                con.setConnectTimeout(3000);
                con.setReadTimeout(3000);
                con.setRequestProperty("Connection", "close");
                con.connect();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                //Read it and create list of dissallowed paths
                String line;
                line = reader.readLine();

                while (line != null) {

                    //  System.out.println(line);
                    if (line.indexOf("User-Agent:") == 0) //checks if this line conatins user-agent: A and a later
                    {
                        // System.out.println("found the user agent");
                        String userAgent = line.substring("User-Agent:".length()); //get the words beside user-agent:
                        //System.out.println(userAgent);
                        if (!(" *".equals(userAgent)) && !(userAgent1.equalsIgnoreCase(userAgent))) //-->recheck later
                        {
                            //System.out.println(Thread.currentThread().getName()+"sees its host  allowed");
                            return true;
                        }
                    }
                    if (line.indexOf("User-agent:") == 0) //checks if this line conatins user-agent: A and a later
                    {
                        // System.out.println("found the user agent");
                        String userAgent = line.substring("User-Agent:".length()); //get the words beside user-agent:
                        // System.out.println(userAgent);
                        if (!" *".equals(userAgent) && !(userAgent1.equalsIgnoreCase(userAgent))) {
                            //System.out.println("host is allowed");
                            return true;
                        }
                    }

                    // System.out.println("line.indexOf(\"Disallow:\"): "+line.indexOf("Disallow:"));
                    if (line.indexOf("Disallow:") == 0) {
                        String disallowPath = line.substring("Disallow:".length());
                        // Check disallow path for comments and remove if present.
                        int commentIndex = disallowPath.indexOf("#");
                        //if there exists a comment remove it
                        if (commentIndex != -1) {
                            disallowPath = disallowPath.substring(0, commentIndex);
                        }
                        // Remove leading or trailing spaces from disallow path.
                        disallowPath = disallowPath.trim();
                        // Add disallow path to list.
                        disallowList.add(disallowPath);
                    }
                    line = reader.readLine();
                }

                // Add new disallow list to cache.
                disallowListCache.put(host, disallowList); //_____>check where you wil use the chache!!
            } catch (Exception e) {
                /* Assume robot is allowed since an exception is thrown if the robot file doesn't exist. */
                return true;
            }
        }
        /* Loop through disallow list to see if crawling is allowed for the given URL. */
        //getting file name or path of this url
        String file = URL.getFile();
        String disallow;
        for (int i = 0; i < disallowList.size(); i++) {
            disallow = (String) disallowList.get(i); //get string path of the first dissallowed oath
            if (file.startsWith(disallow)) {
                return false;

            }
        }
        return true;
    }
}

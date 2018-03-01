
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author Sahar
 */
public class Robot {
   static  public HashMap disallowListCache = new HashMap();///////
    // Check if robot is allowed to access the given URL.
    
    /**
     * checks whether robot is allowed or not
     * @param url
     * @return  true if robot is allowed
     * @throws java.net.MalformedURLException
     */
 public static boolean isRobotAllowed(String url) throws MalformedURLException
    {
        URL URL=new URL(url);
       String  host= URL.getHost().toLowerCase();//get the hos of this url

                // Retrieve host's disallow list from cache.
         ArrayList disallowList =(ArrayList) disallowListCache.get(host);
              // If list is not in the cache, download and cache it.
        if (disallowList == null) 
        {
            disallowList = new ArrayList();  
                    try
            {
              URL robotsFileUrl= new URL("http://"+ host+"/robots.txt"); // if robots.txt not found this returns an exception then robot is allowed
              //open connection to robots file url for reading
              BufferedReader reader =new BufferedReader(new InputStreamReader(robotsFileUrl.openStream()));
              //Read it and creat list of dissallowed paths
              String line;
              while ((line = reader.readLine()) != null) {
                    if (line.indexOf("Disallow:") == 0) {
                    String disallowPath =line.substring("Disallow:".length());
                    // Check disallow path for comments and remove if present.
                    int commentIndex = disallowPath.indexOf("#");
                    //if there exists a comment remove it
                    if (commentIndex != - 1) {
                    disallowPath = disallowPath.substring(0, commentIndex);
                                             }
                    // Remove leading or trailing spaces from disallow path.
                    disallowPath = disallowPath.trim();
                    // Add disallow path to list.
                    disallowList.add(disallowPath);
                    }
                }

                // Add new disallow list to cache.
                disallowListCache.put(host, disallowList); //_____>check where you wil use the chache!!
                }

        catch(Exception e)
        {
    /* Assume robot is allowed since an exception
    is thrown if the robot file doesn't exist. */
        return true;
        }
        }
        /* Loop through disallow list to see if
    crawling is allowed for the given URL. */
        //getting file name or path of this url
          String file=URL.getFile();
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

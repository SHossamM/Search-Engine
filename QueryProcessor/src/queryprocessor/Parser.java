/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryprocessor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
/**
 *
 * @author Sahar
 */
public class Parser {
    static public String htmlTitle(String filePath) throws IOException
    {
       File file=new File(filePath);
       Document doc = Jsoup.parse(file, "UTF-8", "");
       return doc.title();
    }
    
    static public String getSnippet(String filePath,List<String> queryInput) throws IOException{
    
        String snip="";
         File file=new File(filePath);
         Document doc = Jsoup.parse(file, "UTF-8", "");
        
        String b= doc.body().text(); //plain body text
      //search for any of the inputs in the body
      int sIndex=0;
        for(int i=0;i<queryInput.size();i++)
        {
            if(b.contains(queryInput.get(i)))
            {
                sIndex=i;
                break;
            }
                
        }
        
          if(sIndex>10)
          {
              if(b.length()>=160)
              {
               snip=   b.substring(sIndex-10, sIndex+150);
              }
              else
              {
                  snip=b.substring(sIndex-10, b.length()-1);
                  for(int i=snip.length()-1;i<160;i++)
                      snip+='.';
              }
          }
          else
          {
              if(b.length()>=160)
              {
               snip=   b.substring(sIndex, sIndex+160);
              }
              else
              {
                  snip=b.substring(sIndex, b.length()-1);
                  for(int i=snip.length()-1;i<160;i++)
                      snip+='.';
              }
          }
        

         System.out.println(b);
       //  System.out.println(in);
       //  System.out.println(b.substring(in-150, in-1));
         //System.out.println(b.substring(in, in+150));
         
         System.out.println("snip=  "+snip);
         
         return  snip;
        
    }
            
            
            
            
}

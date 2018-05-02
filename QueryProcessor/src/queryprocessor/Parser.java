/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryprocessor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.apache.commons.io.FileUtils;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
/**
 *
 * @author Sahar
 */
public class Parser {
    static final String pagesPath = "E:/CCE Files/Semster6/APT/spring 2018/Crawler/pages";
     WhitespaceTokenizer wsTokenizer = WhitespaceTokenizer.INSTANCE;
     
//     Set<String> titleKeywords = dataDocument.getTitleKeywords();
//     Set<String> metaKeywords = dataDocument.getMetaKeywords();
//     String[] tokens = wsTokenizer.tokenize(dataDocument.getText());
     
     /**
      * returns a document contating title meta and body as tokens
      * @param id
      * @param url
      * @return
      * @throws IOException 
      */
     static public DataDocument  Parse(Integer id, String url) throws IOException
     {
          DataDocument dataDocument=new  DataDocument(id,url,readFile(id));
         return  dataDocument;
     }
     
     /**
      * read html file and return its content as a string
      * @param urlId
      * @return
      * @throws IOException 
      */
    static  public String readFile(Integer urlId) throws IOException {
        return FileUtils.readFileToString(new File(pagesPath + "/" + Integer.toString(urlId) + ".html"), "UTF-8");
    }
  
    static public String htmlTitle(String filePath) throws IOException
    {
       File file=new File(filePath);
       Document doc = Jsoup.parse(file, "UTF-8", "");
       return doc.title();
    }
    
    static public String getSnippet(Integer id, String url,List<String> queryInput) throws IOException{
    
        String snip="";
        
         
        DataDocument doc=Parse(id, url);
        String b= doc.getText();
      //search for any of the inputs in the body and return first index
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
              if(b.length()>=150)
              {
               snip=   b.substring(sIndex-10, sIndex+140);
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
              if(b.length()>=150)
              {
               snip=   b.substring(sIndex, sIndex+150);
              }
              else
              {
                  snip=b.substring(sIndex, b.length()-1);
                  if(snip.length()<150)
                      snip+="...";
              }
          }
        

         System.out.println(b);
       //  System.out.println(in);
       //  System.out.println(b.substring(in-150, in-1));
         //System.out.println(b.substring(in, in+150));
         
         System.out.println("snip=  "+snip);
         
         return  snip;
        
    }
    
 static public  String getExactSnippet(Integer id, String url,String input) throws IOException
  {
      String snip="";
      DataDocument doc=Parse(id,url);
      String b= doc.getText();
      //search for any of the inputs in the body and return first index
      int sIndex=b.indexOf(input);
      if(sIndex>10)
      {
          
      if(b.length()>=150)
              {
               snip=   b.substring(sIndex-10, sIndex+140);
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
              if(b.length()>=150)
              {
               snip=   b.substring(sIndex, sIndex+150);
              }
              else
              {
                  snip=b.substring(sIndex, b.length()-1);
                  if(snip.length()<150)
                      snip+="...";
              }
          }
      
      
      return snip;
  }
            
            
            
            
}

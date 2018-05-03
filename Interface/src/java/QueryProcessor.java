/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClientURI;
import opennlp.tools.stemmer.PorterStemmer;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.bson.Document;
/**
 * @author Sahar
 */
public class QueryProcessor {
 static PorterStemmer porterStemmer = new PorterStemmer();
 static private MongoDatabase indexDB;
 private MongoClient mongoClient;
 static final String pagesPath = "E:/CCE Files/Semster6/APT/spring 2018/Crawler/pages/";
 
 public QueryProcessor() {
       
        mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        indexDB = mongoClient.getDatabase("indexDB");
        
    }

 public Result[] nonFullTextQuery(String string) throws IOException{
            //remove stop words 
          List<String> newString=StopWords.remove(string);
            //call fulltext query function
               if(newString.isEmpty()){
                  return PhraseQuery(string);
               }
           List<String> stemmedTerms =new ArrayList<>(); //get tem of each string
           String stemedWords="";
           for(String s:newString){
             stemedWords+="'"+porterStemmer.stem(s)+"'," ;     //add each stem inside single quotes and concatinate all
            }
           stemedWords = stemedWords.substring(0,stemedWords.length() - 1); //remove last ' form stemed words
           String stringArray="["+stemedWords+"]";  
           String fun="searchQuery("+stringArray+")";
           Document doc=new Document("eval",fun);   
           Document document= indexDB.runCommand(doc);  
           Document object= (Document)document.get("retval");
  
           ArrayList<Document> documents=  (ArrayList<Document>) object.get("_batch");
           Result[] results= formResult(documents,newString);
    
            
         return results;
 }
 
 
   public Result[] PhraseQuery(String string) throws IOException{
           
           String fun="PhraseSearch('"+string+"')";
           Document doc=new Document("eval",fun);   
           Document document= indexDB.runCommand(doc);  
          System.out.println("queryprocessor.QueryProcessor.PhraseQuery()");
          
           Document object= (Document)document.get("retval");
  
           ArrayList<Document> documents=  (ArrayList<Document>) object.get("_batch");
           Result[] results= formResult(documents,string);
           return results;
 }
 
 public Result[] formResult( ArrayList<Document> documents, List<String> queryInput) throws IOException
{
    Result[] results=new Result[documents.size()];
           int i=0;
           for(Document d:documents)
            {
                  int id=(int)d.get("_id");
                  String url=d.getString("url");
                  String snip=Parser.getSnippet(id,url,queryInput );
                  results[i]=new Result(id,((double)d.get("score")),url);  
                  results[i].setTitle(Parser.htmlTitle(pagesPath+id+".html"));
                 results[i].setSnippet(snip);
                 System.out.println(snip);
                          i++;  
                        
            }
           return results;
}
 
 
 
  public Result[] formResult( ArrayList<Document> documents, String queryInput) throws IOException
{
    Result[] results=new Result[documents.size()];
           int i=0;
           for(Document d:documents)
            {
                  int id=(int)d.get("_id");
                  String url=d.getString("url");
                //  String snip=Parser.getExactSnippet(id,d.getString("url"),queryInput);
                  results[i]=new Result(id,((double)d.get("score")),url);  
                  results[i].setTitle(Parser.htmlTitle(pagesPath+id+".html"));
                // results[i].setSnippet( snip);
                          i++;  
                        
            }
           return results;
}
  
  
  
 public Result[] ProcessQuery(String string) throws IOException{
    if(string.startsWith("\"")){ //full text search  
        String phrase= string.replaceAll("\"", "");
        return PhraseQuery(phrase);
    }
    else{
       return  nonFullTextQuery(string);
        }
}
    

}

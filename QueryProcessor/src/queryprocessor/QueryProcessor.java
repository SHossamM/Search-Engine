/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryprocessor;

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

 //mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
   //     indexDB = mongoClient.getDatabase("indexDB");
 
 public QueryProcessor() {
       
        mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        indexDB = mongoClient.getDatabase("indexDB");
        
    }

static public void ProcessQuery(String string) throws IOException{
    if(string.startsWith("\"")){ //full text search  
      }
    else{
        // indexDB.runCommand(new Document("eval","updateInformationAndIdf("+s+")"));
      List<String> newString=StopWords.remove(string);
        System.out.println(newString); 
         if(newString.isEmpty()){ //then full text search again
            
           //full text index
          }
         else{
           List<String> stemmedTerms =new ArrayList<>();
           String s2="";
           for(String s:newString){

             s2+="'"+porterStemmer.stem(s)+"'," ;
                 
            }
           s2 = s2.substring(0,s2.length() - 1);
           String S3="["+s2+"]";
           System.out.println(S3);  
           String fun="myFunction2("+S3+")";
           System.out.println(fun);
           Document doc=new Document("eval",fun);   
           Document document= indexDB.runCommand(doc);
           
          System.out.println(document);
        
       Document object= (Document)document.get("retval");
  
      ArrayList<Document> documents=  (ArrayList<Document>) object.get("_batch");
      Result[] results=new Result[documents.size()];
           System.out.println(documents.size());
           int i=0;
      for(Document d:documents)
              {
 //                 System.out.println("id= "+d.get("_id")+" Score= "+d.get("score"));
                 int id=(int)d.get("_id");
                  results[i]=new Result(id,((double)d.get("score")));  
                  results[i].setTitle(Parser.htmlTitle("E:/CCE Files/Semster6/APT/spring 2018/Crawler/pages/"+id+".html"));
                          i++;
                          
                          //fadl el snippet
              }
         
//          for(int j=0;j<results.length;j++)
//               System.out.println("id="+results[j].getId()+" rank ="+results[j].getRank());
    }
    
    
}
    
}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryprocessor;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author Sahar
 */

public class Main {
      public static void main(String[] args) throws IOException {
        // TODO code application logic here
//       String s="a cat hates the dog";
//       String w= StopWords.remove(s);
//       w=Stemmer.stem("stemming");
//        System.out.println(w);
//        w=Stemmer.stem("eating");
//        System.out.println(w);
//        w=Stemmer.stem("computer");
//        System.out.println(w);
//        w=Stemmer.stem("eater");
//        System.out.println(w);
//        w=Stemmer.stem("facing");
//        System.out.println(w);
//        w=Stemmer.stem("hiking");
//        System.out.println(w);
//        w=Stemmer.stem("runs");
//        System.out.println(w);
//        w=Stemmer.stem("fans");
//        System.out.println(w);
//        w=Stemmer.stem("deadly");
//        System.out.println(w);

 String s="hello";
 if(s.startsWith("\""))
              System.out.println("queryprocessor.Main.main()");
       String s1="newly added free listings";
     //List<String> w= StopWords.remove(s1);
      //for(int j=0;j<w.size();j++)
        //    System.out.println(w.get(j));
        QueryProcessor qP=new QueryProcessor();
        qP.ProcessQuery(s1);
      
//      for(String w0:w)  
//      System.out.println(w0);
//         
   String tit;
          tit = Parser.htmlTitle("E:/CCE Files/Semster6/APT/spring 2018/Crawler/pages/3.html");
          List<String> newString=StopWords.remove(s1);
          System.out.println(tit);
          Parser.getSnippet("E:/CCE Files/Semster6/APT/spring 2018/Crawler/pages/3.html",newString);

    }
}

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
      
      QueryProcessor q=new QueryProcessor();
     // q.nonFullTextQuery("a man and an elephant are happhy");
      q.PhraseQuery("The Internet Press");
       
   
  

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sahar
 */

@WebServlet(urlPatterns = {"/DisplayResult"})
public class DisplayResult extends HttpServlet {
    
    
    
    
public void doGet(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException {
  // Set response content type
  response.setContentType("text/html");
  PrintWriter out = response.getWriter();
  
  String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " +
          "transitional//en\">\n";
  QueryProcessor qp=new QueryProcessor();
   String inputText=request.getParameter("textbox");
 Result [] results=qp.ProcessQuery(inputText);
 
  out.println(docType +
  "<html>\n" +
  "<head></head>\n" +
  "<body bgcolor=\"#f0f0f0\">\n" );
 
   for(int i=0;i<10;i++)
   {
   out.println(
  "<b><a href="+results[i].getUrl()+">" +"<font size=\"5\">"+results[i].getTitle()+"</font>"+"</a></b><br>"+"<font color=\"green\">\n"+results[i].getUrl()+"</font><br>\n");
   out.println(results[i].getSnippet()+"<br>");
   
   }
   out.println( "</body></html>");
  }
    


}

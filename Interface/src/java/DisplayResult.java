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
  "<b><a href="+results[i].getUrl()+">" +"<font size=\"5\">"+results[i].getTitle()+"</font>"+"</a></b><br>"+"<font color=\"green\">\n"+results[i].getUrl()+"</font><br><br><br>\n");
   }
   out.println( "</body></html>");
  }
    
    
    

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DisplayResult</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DisplayResult at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    
    
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

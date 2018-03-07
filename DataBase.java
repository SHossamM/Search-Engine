
import java.sql.*;
import java.sql.Connection;
import java.util.Queue;
import javax.xml.transform.Result;

import com.microsoft.sqlserver.jdbc.SQLServerDriver; //Java DB Conection
import java.util.LinkedList;

public class DataBase {
    /**JDBC
     * 1-Import JDBC Packages (import com.Microsoft.sqlserver.jdbc.SQLServerDriver;)
     * 2-Register JDBC Driver
     * 3-Database URL Formulation
     */
    // JDBC driver name and database URL
    private static final String driver = ("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    private static final String dBURL = "jdbc:sqlserver://localhost:1433;databaseName=SearchEngine"; //to create a properly formatted address that points to the database to which you wish to connect
   private static final  String userName = "sa";
    private static final String password = "1234" ; 
    
    private static java.sql.Connection connection;
public DataBase() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException{
    
        Class.forName(driver).newInstance();// force it to be included in the final war,
        connection=DriverManager.getConnection(dBURL,userName,password);
        System.out.println("Connecting to database...");
    
    
       // System.out.println("Error: unable to load driver class!");
        //System.exit(1);  
    
}



/**
 * 
Retreves nonvisited links to a queue
     * @return  a queue of urls
     * @throws java.sql.SQLException
*/
public  Queue<String> RetriveNonVisited() throws SQLException
{
  Queue<String> seedSet=new  LinkedList<String>(); 
  CallableStatement cStmt=connection.prepareCall("{exec RetriveNonVisited}"); //throws sqlexception -->check later
  cStmt.execute();
  ResultSet result=cStmt.getResultSet();
  while(result.next())//while there are rows in returned result set
  {
   seedSet.add(result.getNString(0)); //adding all non visited to urlqueues
  }
  result.close();
  cStmt.close();
  return seedSet;
}

}

package com.indexer;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CrawlerDB {
    /**
     * JDBC
     * 1-Import JDBC Packages (import com.Microsoft.sqlserver.jdbc.SQLServerDriver;)
     * 2-Register JDBC Driver
     * 3-Database URL Formulation
     */
    // JDBC driver name and database URL
    private static final String driver = ("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    private static final String dBURL = "jdbc:sqlserver://localhost:1433;databaseName=SearchEngine"; //to create a properly formatted address that points to the database to which you wish to connect
    private static final String username = "SearchEngine";
    private static final String password = "1234";

    private static java.sql.Connection connection;

    public CrawlerDB() {
        try {
            Class.forName(driver).newInstance();// force it to be included in the final war,
            connection = DriverManager.getConnection(dBURL,username,password);
            System.out.println("Connecting to database...");
        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println("Error: unable to load driver class!");
            System.exit(1);
        }
    }

    /**
     * Retrieves all visited links to a queue
     *
     * @return a list of ids and urls
     * @throws java.sql.SQLException
     */
    public Queue<Url> RetrieveUnindexed() throws SQLException {
        Queue<Url> seedSet = new LinkedList<>();
        CallableStatement cStmt = connection.prepareCall("{call RetrieveToBeIndexed}"); //throws sqlexception -->check later
        cStmt.execute();
        ResultSet result = cStmt.executeQuery();
        while (result.next()) {//while there are rows in returned result set
            seedSet.add(new Url(result.getInt("id"), result.getString("url"))); //adding all non visited to urlqueues
        }
        result.close();
        cStmt.close();
        return seedSet;
    }

    public void MarkIndexed(List<Integer> indexedUrls) {
        try {
            connection.setAutoCommit(false);
            CallableStatement cStmt = connection.prepareCall("{call MarkIndexed(?)}"); //throws sqlexception -->check later


            for(Integer urlId: indexedUrls){
                cStmt.setInt("urlId", urlId);
                cStmt.addBatch();
            }

            cStmt.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }


}

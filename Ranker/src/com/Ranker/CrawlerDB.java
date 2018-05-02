package com.Ranker;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
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

    private static Connection connection;

    public CrawlerDB() {
        try {
            Class.forName(driver).newInstance();// force it to be included in the final war,
            connection = DriverManager.getConnection(dBURL, username, password);
            System.out.println("Connecting to database...");
        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println("Error: unable to load driver class!");
            System.exit(1);
        }
    }

    /**
     * Retreves connections for ranking
     *
     * @return a queue of urls
     * @throws SQLException
     */
    public synchronized ConcurrentLinkedQueue<PageConnection> RetrieveToBeRanked() throws SQLException {
        ConcurrentLinkedQueue<PageConnection> pageConnections = new ConcurrentLinkedQueue<>();
        CallableStatement cStmt = connection.prepareCall("{call RetrieveToBeRanked}"); //throws sqlexception -->check later
        cStmt.execute();
        ResultSet result = cStmt.executeQuery();
        while (result.next()) {//while there are rows in returned result set
            pageConnections.add(new PageConnection(
                    result.getInt("sourceId"),
                    result.getInt("destinationId")
            ));
        }
        result.close();
        cStmt.close();
        return pageConnections;
    }

    public synchronized void UpdatePageRank(HashMap<Integer, Double> pageRanks) {
        try {
            connection.setAutoCommit(false);
            CallableStatement cStmt = connection.prepareCall("{call UpdatePageRank(?, ?)}"); //throws sqlexception -->check later

            for (Integer id : pageRanks.keySet()) {
                cStmt.setInt("urlId", id);
                cStmt.setDouble("pagerank", pageRanks.get(id));
                cStmt.addBatch();
            }
            cStmt.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    void close() throws SQLException {
        connection.close();
    }
}

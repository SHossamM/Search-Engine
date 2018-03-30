import java.sql.*;
import java.util.List;

import java.util.concurrent.ConcurrentLinkedQueue;

public class DataBase {
    /**
     * JDBC
     * 1-Import JDBC Packages (import com.Microsoft.sqlserver.jdbc.SQLServerDriver;)
     * 2-Register JDBC Driver
     * 3-Database URL Formulation
     */
    // JDBC driver name and database URL
    private static final String driver = ("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    private static final String dBURL = "jdbc:sqlserver://localhost:1433;databaseName=SearchEngine"; //to create a properly formatted address that points to the database to which you wish to connect
    private static final String username = "sa";
    private static final String password = "1234";

    private static java.sql.Connection connection;

    public DataBase() {
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
     * Retreves nonvisited links to a queue
     *
     * @return a queue of urls
     * @throws java.sql.SQLException
     */
    public synchronized ConcurrentLinkedQueue<Url> RetriveNonVisited() throws SQLException {
        ConcurrentLinkedQueue<Url> seedSet = new ConcurrentLinkedQueue<>();
        CallableStatement cStmt = connection.prepareCall("{call RetriveNonVisited}"); //throws sqlexception -->check later
        cStmt.execute();
        ResultSet result = cStmt.executeQuery();
        while (result.next()) {//while there are rows in returned result set
            seedSet.add(new Url(result.getInt("id"), result.getString("url"),result.getTimestamp("visited"),result.getInt("Rank"))); //adding all non visited to urlqueues
        }
        result.close();
        cStmt.close();
        return seedSet;
    }

    public synchronized void InsertLink(int srcId, String destUrl) {
        try {
            CallableStatement cStmt = connection.prepareCall("{call InsertLink(?,?)}"); //throws sqlexception -->check later
            cStmt.setInt("srcId", srcId);
            cStmt.setString("destUrl", destUrl);
            cStmt.execute();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public synchronized void InsertLinks(Integer srcId, List<String> destUrls) {
        try {
            connection.setAutoCommit(false);
            CallableStatement cStmt = connection.prepareCall("{call InsertLink(?,?)}"); //throws sqlexception -->check later

            for(String destUrl: destUrls){
                cStmt.setInt("srcId", srcId);
                cStmt.setString("destUrl", destUrl);
                cStmt.addBatch();
            }
            cStmt.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public synchronized void InsertLinks(ConcurrentLinkedQueue<OutgoingLinks> outgoingLinks) {
        try {
            connection.setAutoCommit(false);
            CallableStatement cStmt = connection.prepareCall("{call InsertLink(?,?)}"); //throws sqlexception -->check later

            while (!outgoingLinks.isEmpty()) {
                OutgoingLinks outgoingLink = outgoingLinks.poll();
                Integer srcUrl = outgoingLink.getSrcId();
                for (String destUrl : outgoingLink.getDestUrls()) {
                    cStmt.setInt("srcId", srcUrl);
                    cStmt.setString("destUrl", destUrl);
                    cStmt.addBatch();
                }
            }
            cStmt.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }
   
    public synchronized void MarkVisited(Integer urlId) {
        try {
            CallableStatement cStmt = connection.prepareCall("{call MarkVisited(?)}"); //throws sqlexception -->check later
            cStmt.setInt("urlId", urlId);
            cStmt.execute();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public synchronized void MarkVisited(ConcurrentLinkedQueue<Integer> visitedUrls) {
        try {
            connection.setAutoCommit(false);
            CallableStatement cStmt = connection.prepareCall("{call MarkVisited(?)}"); //throws sqlexception -->check later

            while (!visitedUrls.isEmpty()) {
                    cStmt.setInt("urlId", visitedUrls.poll());
                    cStmt.addBatch();
            }
            cStmt.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }
    public synchronized void InsertHash(ConcurrentLinkedQueue<Url> HashQueue)
    {
        try {
            connection.setAutoCommit(false);
            CallableStatement cStmt = connection.prepareCall("{call InsertHash(?,?)}"); //throws sqlexception -->check later
            Url url;
            while (!HashQueue.isEmpty()) {
                    url=HashQueue.poll();
                    cStmt.setInt("urlId", url.getId());
                    cStmt.setInt("hash", url.getHash());
                    cStmt.addBatch();
            }
            cStmt.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }
    public synchronized void UnmarkParseable(Integer urlId) {
        try {
            CallableStatement cStmt = connection.prepareCall("{call UnmarkParseable(?)}"); //throws sqlexception -->check later
            cStmt.setInt("urlId", urlId);
            cStmt.execute();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public synchronized void UnmarkParseable(ConcurrentLinkedQueue<Integer> notParseable) {
        try {
            connection.setAutoCommit(false);
            CallableStatement cStmt = connection.prepareCall("{call UnmarkParseable(?)}"); //throws sqlexception -->check later

            while (!notParseable.isEmpty()) {
                cStmt.setInt("urlId", notParseable.poll());
                cStmt.addBatch();
            }
            cStmt.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public synchronized void UnmarkVerified(Integer urlId) {
        try {
            CallableStatement cStmt = connection.prepareCall("{call UnmarkVerified(?)}"); //throws sqlexception -->check later
            cStmt.setInt("urlId", urlId);
            cStmt.execute();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public synchronized void UnmarkVerified(ConcurrentLinkedQueue<Integer> notVerified) {
        try {
            connection.setAutoCommit(false);
            CallableStatement cStmt = connection.prepareCall("{call UnmarkVerified(?)}"); //throws sqlexception -->check later

            while (!notVerified.isEmpty()) {
                cStmt.setInt("urlId", notVerified.poll());
                cStmt.addBatch();
            }
            cStmt.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public synchronized void UnmarkRobotallowed(Integer urlId) {
        try {
            CallableStatement cStmt = connection.prepareCall("{call UnmarkRobotallowed(?)}"); //throws sqlexception -->check later
            cStmt.setInt("urlId", urlId);
            cStmt.execute();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public synchronized void UnmarkRobotallowed(ConcurrentLinkedQueue<Integer> notRobotallowed) {
        try {
            connection.setAutoCommit(false);
            CallableStatement cStmt = connection.prepareCall("{call UnmarkRobotallowed(?)}"); //throws sqlexception -->check later

            while (!notRobotallowed.isEmpty()) {
                cStmt.setInt("urlId", notRobotallowed.poll());
                cStmt.addBatch();
            }
            cStmt.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public synchronized int GetVisitedCount() {
        try {
            CallableStatement cStmt = connection.prepareCall("{call CountVisited(?)}"); //throws sqlexception -->check later
            cStmt.registerOutParameter(1, java.sql.Types.INTEGER);
            cStmt.execute();
            return cStmt.getInt(1);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return 0;
    }
   
}

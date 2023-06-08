package helper;

import java.sql.Connection;
import java.sql.DriverManager;

/** This class handles the connection to the database. */
public abstract class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    //had major issues with the server changing zones on me
    //"?connectionTimeZone = SERVER"
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = GMT"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    public static Connection connection;  // Conection Interface

    public static void openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        }
        catch(Exception e)
        {
            //e.getStackTrace()?
            System.out.println("Error:" + e.getMessage());
        }
    }
//https://wgu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=1be32ba5-76c6-47f3-8816-accf0002109b
    public static void getConnection() {
        //return conn;
        //doesnt work?? confused on this video
    }

    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }
}

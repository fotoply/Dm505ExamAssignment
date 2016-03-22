package control;

import java.io.*;
import java.nio.channels.NotYetConnectedException;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created 3/4/16
 *
 * @author Niels Norberg
 */
public class ConnectionDriver {
    private String password;

    private static ConnectionDriver instance;
    private static Connection connection = null;

    public static ConnectionDriver getInstance() {
        if (instance == null) {
            instance = new ConnectionDriver();
        }
        return instance;
    }

    private ConnectionDriver() {
    }

    public void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "postgres", "123");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public static Connection getConnection() throws NotActiveException {
        if (connection == null) {
            throw new NotActiveException("No database connection is established");
        } else {
            return connection;
        }
    }
}

package control;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created 3/4/16
 *
 * @author Niels Norberg
 */
public class ConnectionDriver {
    private String username = "postgres";
    private String password = "123";
    private String url = "jdbc:postgresql://localhost:5432/testdb";

    private static ConnectionDriver instance;
    private Connection connection = null;

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
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public Connection getConnection() {
        return connection;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

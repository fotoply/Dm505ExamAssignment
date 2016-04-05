package control;

import java.sql.*;

/**
 * Created 3/4/16
 *
 * @author Niels Norberg
 */
public class ConnectionDriver {
    private static ConnectionDriver instance;
    private String username = "postgres";
    private String password = "123";
    private String url = "jdbc:postgresql://localhost:5432/testdb";
    private Connection connection;

    private ConnectionDriver() {
    }

    public static ConnectionDriver getInstance() {
        if (instance == null) {
            instance = new ConnectionDriver();
        }
        return instance;
    }

    public void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public void executeUpdate(String sql, Object... args) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(String.format(sql, args));
        statement.close();
    }

    public boolean execute(String sql, Object... args) throws SQLException {
        Statement statement = connection.createStatement();
        statement.closeOnCompletion();
        return statement.execute(String.format(sql, args));
    }

    public ResultSet executeQuery(String sql, Object... args) throws SQLException {
        Statement statement = connection.createStatement();
        statement.closeOnCompletion();
        return statement.executeQuery(String.format(sql, args));
    }

    public Connection getConnection() {
        if (connection == null) {
            connect();
        }
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

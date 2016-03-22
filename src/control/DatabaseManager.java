package control;

import java.io.NotActiveException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Created 3/22/16
 *
 * @author Niels Norberg
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection query;

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private DatabaseManager() {
        try {
            query = ConnectionDriver.getInstance().getConnection();
        } catch (NotActiveException e) {
            e.printStackTrace();
        }
    }

    public ResultSet fetchAllFromTable(String tableName) throws SQLException {
        Statement statement = query.createStatement();
        return statement.executeQuery("SELECT * from " + tableName +";");
    }

    public ResultSet getComponent(int componentId) throws SQLException {
        Statement statement = query.createStatement();
        ResultSet component = statement.executeQuery("SELECT * FROM components WHERE componentid=" + componentId + " LIMIT 1;");
        if (component.next()) {
            String otherTable = component.getString("kind");
            return statement.executeQuery("SELECT * FROM components natural join cpu WHERE componentid=" + componentId + ";");
        }
        return null;
    }

}

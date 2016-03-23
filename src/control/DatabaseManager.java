package control;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
            ConnectionDriver.getInstance().connect();
            query = ConnectionDriver.getInstance().getConnection();
    }

    public ResultSet fetchAllFromTable(String tableName) throws SQLException {
        Statement statement = query.createStatement();
        return statement.executeQuery("SELECT * from " + tableName +";");
    }

    /**
     * Returns a component based on the given componentId. Automatically infers the table from which to get the extra information for the component.
     * Will return null if no component is found.
     * @param componentId
     * @return
     * @throws SQLException
     */
    public ResultSet getComponent(int componentId) throws SQLException {
        Statement statement = query.createStatement();
        ResultSet component = statement.executeQuery("SELECT * FROM components WHERE componentid=" + componentId + " LIMIT 1;");
        if (component.next()) {
            String otherTable = component.getString("kind");
            return statement.executeQuery("SELECT * FROM components natural join " + otherTable + " WHERE componentid=" + componentId + ";");
        }
        return null;
    }

    /**
     * Returns all components of the given kind, plus extended data for these.
     * @param kind
     * @return
     * @throws SQLException
     */
    public ResultSet getComponents(String kind) throws SQLException {
        Statement statement = query.createStatement();
        return statement.executeQuery("SELECT * FROM components natural join " + kind + " WHERE kind=" + kind + ";");
    }

    public ResultSet getAllComponentsOrdered() throws SQLException {
        Statement statement = query.createStatement();
        return statement.executeQuery("SELECT name, kind, price FROM components ORDER BY kind, price");
    }

    public boolean isInStock(String name) throws SQLException {
        Statement statement = query.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM components WHERE name='" + name + "' AND amount > 0");
        if(rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    public int maxBuildable(String name) throws SQLException {
        Statement statement = query.createStatement();
        ResultSet rs = statement.executeQuery("SELECT min(amount) FROM (SELECT amount FROM components c WHERE exists (SELECT ramid,caseid,mainboardid,graphicsid,cpuid FROM computersystems WHERE name=" + name + " AND (c.componentid=ramid OR c.componentid=caseid OR c.componentid=mainboardid OR c.componentid=graphicsid OR c.componentid=cpuid))) AS sub1;");
        rs.next();
        return rs.getInt(1);
    }

    public void sellComponent()  {
        // TODO
    }

    public void sellComputerSystem() {
        // TODO
    }

}

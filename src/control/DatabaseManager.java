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
        return statement.executeQuery("SELECT * FROM components natural join " + kind + " WHERE kind=''" + kind + "';");
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
        ResultSet rs = statement.executeQuery("SELECT min(amount) FROM (SELECT amount FROM components c WHERE exists (SELECT ramid,caseid,mainboardid,graphicsid,cpuid FROM computersystems WHERE name='" + name + "' AND (c.componentid=ramid OR c.componentid=caseid OR c.componentid=mainboardid OR c.componentid=graphicsid OR c.componentid=cpuid))) AS sub1;");
        rs.next();
        return rs.getInt(1);
    }

    public void sellComponent(String name) throws SQLException {
        Statement statement = query.createStatement();
        statement.executeUpdate("UPDATE components SET amount=(SELECT amount FROM components where name='"+name+"')-1 WHERE name='"+name+"';");
    }


    public void sellComponent(int componentId) throws SQLException {
        Statement statement = query.createStatement();
        statement.executeUpdate("UPDATE components SET amount=(SELECT amount FROM components where componentid="+componentId+")-1 WHERE componentid="+componentId+";");
    }


    public void sellComputerSystem(String systemName) throws SQLException {
        Statement statement = query.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM computersystems WHERE name='"+systemName+"';");
        rs.next();
        for (int i = 2; i < 7; i++) {
            if(rs.getObject(i) != null) {
                sellComponent(rs.getInt(i));
            }
        }
    }

    public int getPriceForSystem(String systemName) throws SQLException {
        int price = 0;
        Statement statement = query.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM computersystems WHERE name='"+systemName+"';");
        rs.next();
        for (int i = 2; i < 7; i++) {
            if(rs.getObject(i) != null) {
                ResultSet rs2 = statement.executeQuery("SELECT price FROM components WHERE componentid=" + rs.getInt(i) +";");
                price += rs2.getInt("price")*TextDriver.PRICEMULTIPLIER;
            }
        }
        return price;
    }

}

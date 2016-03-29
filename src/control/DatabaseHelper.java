package control;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created 3/22/16
 *
 * @author Niels Norberg
 */
@SuppressWarnings("SqlResolve")
public class DatabaseHelper {
    private static DatabaseHelper instance;
    private ConnectionDriver connectionDriver;

    private DatabaseHelper() {
        ConnectionDriver.getInstance().connect();
        connectionDriver = ConnectionDriver.getInstance();
    }

    public static DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    public ResultSet getAllFromTable(String tableName) throws SQLException {
        return connectionDriver.executeQuery("SELECT * FROM %s;", tableName);
    }

    /**
     * Returns a component based on the given componentId. Automatically infers the table from which to get the extra information for the component.
     * Will return null if no component is found.
     *
     * @param componentId the ID of the component
     * @return Returns either an empty ResultSet if no component is found otherwise returns the component and any additional information about it
     * @throws SQLException
     */
    public ResultSet getComponent(int componentId) throws SQLException {
        ResultSet component = connectionDriver.executeQuery("SELECT * FROM components WHERE componentid=%d LIMIT 1;", componentId);
        if (component.next()) {
            String otherTable = component.getString("kind");
            return connectionDriver.executeQuery("SELECT * FROM components NATURAL JOIN %s WHERE componentid=%d;", otherTable, componentId);
        }
        return component;
    }

    /**
     * Returns all components of the given kind, plus extended data for these.
     *
     * @param type The type of component
     * @return All components found or an empty ResultSet
     * @throws SQLException
     */
    public ResultSet getComponents(String type) throws SQLException {
        return connectionDriver.executeQuery("SELECT * FROM components NATURAL JOIN %s WHERE kind='%s';", type, type);
    }

    /**
     * Returns the price, name and type of all components in the components table. The result is ordered first by type then by price.
     *
     * @return A ResultSet of components that is ordered
     * @throws SQLException
     */
    public ResultSet getAllComponentsOrdered() throws SQLException {
        return connectionDriver.executeQuery("SELECT name, kind, price FROM components ORDER BY kind, price");
    }

    /**
     * Returns whether a component is in stock
     *
     * @param name the name of the component, case sensitive.
     * @return true if in stock otherwise false
     * @throws SQLException
     */
    public boolean isInStock(String name) throws SQLException {
        ResultSet rs = connectionDriver.executeQuery("SELECT * FROM components WHERE name='%s' AND amount > 0", name);
        return rs.next();
    }

    /**
     * Returns the max amount the shop can build of a given system in their database
     *
     * @param name the name of the system, case sensitive.
     * @return the amount of systems buildable.
     * @throws SQLException
     */
    public int getMaxSystemsBuildable(String name) throws SQLException {
        ResultSet rs = connectionDriver.executeQuery("SELECT min(amount) FROM (SELECT amount FROM components c WHERE exists (SELECT ramid,caseid,mainboardid,graphicsid,cpuid FROM computersystems WHERE name='%s' AND (c.componentid=ramid OR c.componentid=caseid OR c.componentid=mainboardid OR c.componentid=graphicsid OR c.componentid=cpuid))) AS sub1;", name);
        rs.next();
        return rs.getInt(1);
    }

    /**
     * Creates a sale in the database by decrementing the amount of a given component which is in stock.
     *
     * @param name the components name, case sensitive.
     * @throws SQLException
     */
    public void sellComponent(String name) throws SQLException {
        if (isInStock(name)) {
            connectionDriver.executeUpdate("UPDATE components SET amount=(SELECT amount FROM components WHERE name='%s')-1 WHERE name='%s';", name, name);
        }
    }

    /**
     * Creates a sale in the database by decrementing the amount of a given component.<br> <u>Does not verify that it is actually in stock.</u>
     *
     * @param componentId the components id
     * @throws SQLException
     */
    public void sellComponent(int componentId) throws SQLException {
        connectionDriver.executeUpdate("UPDATE components SET amount=(SELECT amount FROM components WHERE componentid=%d)-1 WHERE componentid=%d;", componentId, componentId);
    }

    /**
     * Sells a computer system by decrementing all of components it contains by 1. <br> <u>Does not verify that it is actually in stock.</u>
     *
     * @param systemName the name of the computer system, case sensitive.
     * @throws SQLException
     */
    public void sellComputerSystem(String systemName) throws SQLException {
        ResultSet rs = connectionDriver.executeQuery("SELECT * FROM computersystems WHERE name='%s';", systemName);
        rs.next();
        for (int i = 2; i < 7; i++) {
            if (rs.getObject(i) != null) {
                sellComponent(rs.getInt(i));
            }
        }
    }

    /**
     * Gets the price of a computer system from the database.
     *
     * @param systemName the name of the computer system, case sensitive.
     * @return the maximum copies of the system that is buildable with current components.
     * @throws SQLException
     */
    public int getPriceForSystem(String systemName) throws SQLException {
        int price = 0;
        ResultSet rs = connectionDriver.executeQuery("SELECT * FROM computersystems WHERE name='%s';", systemName);
        rs.next();
        for (int i = 2; i < 7; i++) {
            if (rs.getObject(i) != null) {
                ResultSet rs2 = connectionDriver.executeQuery("SELECT price FROM components WHERE componentid=%d;", rs.getInt(i));
                rs2.next();
                price += rs2.getInt("price") * TextDriver.PRICEMULTIPLIER;
            }
        }
        price = (int) (Math.ceil(price / 100) * 100) - 1;
        return price;
    }

    /**
     * Get the amount that needs to be bought to have filled up to prefered levels for a given component.
     * @param componentId the ID of the component
     * @return Returns the amount or 0 if there is equal to or more than the prefered level
     * @throws SQLException
     */
    public int getAmountNeededForRestock(int componentId) throws SQLException {
        int needed;
        ResultSet rs = connectionDriver.executeQuery("SELECT sum(prefered-amount) FROM minimumstock,components WHERE minimumstock.componentid=%d AND components.componentid=%d;",componentId, componentId);
        if(rs.next()) {
            needed = rs.getInt(1);
            return (needed <= 0) ? 0 : needed;
        }
        return 0;
    }

    /**
     * Gets the max componentId in the system. There is no guarantee that all components below that number exists, but nothing exists above it.
     * @return the max ID
     * @throws SQLException
     */
    public int getMaxComponentId() throws SQLException {
        ResultSet rs = connectionDriver.executeQuery("SELECT max(componentid) FROM components;");
        rs.next();
        return rs.getInt(1);
    }

    /**
     * Returns a computer system by id
     * @param systemID the ID of the system
     * @return
     * @throws SQLException
     */
    public ResultSet getComputerSystem(int systemID) throws SQLException {
        return connectionDriver.executeQuery("SELECT * FROM computersystems WHERE systemid=%d;", systemID);
    }

    /**
     * Gets the max systemId in the system. There is no guarantee that all system below that number exists, but nothing exists above it.
     * @return the max ID
     * @throws SQLException
     */
    public int getMaxComputerSystemId() throws SQLException {
        ResultSet rs = connectionDriver.executeQuery("SELECT max(systemid) FROM computersystems;");
        rs.next();
        return rs.getInt(1);
    }

    public ResultSet getAllComputerSystems() throws SQLException {
        return connectionDriver.executeQuery("SELECT * from computersystems;");
    }
}

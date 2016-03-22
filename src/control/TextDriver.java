package control;

import javafx.application.Application;

import java.io.NotActiveException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Created 3/22/16
 *
 * @author Niels Norberg
 */
public class TextDriver {
    private boolean running = true;
    public static void main(String[] args) throws SQLException, NotActiveException {
        TextDriver driver = new TextDriver();
        driver.run();
    }

    public void run() throws SQLException, NotActiveException {
        while (running) {
            printOptions();
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    printAllComponents();
                    break;

                case 2:
                    printAllSystems();
                    break;

                case 3:
                    printPrices();
                    break;

                case 4:
                    // TODO Price offer
                    break;

                case 5:
                    // TODO Sell component
                    System.out.println("Please type the name of the component to sell: ");
                    String name = scanner.next();
                    if(DatabaseManager.getInstance().isInStock(name)) {

                    } else {
                        System.out.println("Unable to sell as none is in stock.");
                    }
                    break;

                case 6:
                    printRestockingList();
                    break;

                case 7:
                    if(ConnectionDriver.getInstance().getConnection() != null) {
                        ConnectionDriver.getInstance().getConnection().close();
                    }
                    System.exit(0);
                    break;
            }
        }
    }

    private void printRestockingList() {
        // TODO
    }

    private void printOptions() {
        System.out.println("Please choose one of the following options: \n" +
                "1. List all components\n" +
                "2. List all computer systems\n" +
                "3. List prices\n" +
                "4. Price offer\n" +
                "5. Sell system or component\n" +
                "6. List restocking\n" +
                "7. Exit program");
    }

    private void printAllComponents() throws SQLException {
        ResultSet rs;
        rs = DatabaseManager.getInstance().fetchAllFromTable("components");
        System.out.format("%40s%10s%14s","Name","Amount","Type");
        System.out.println();
        while (rs.next()) {
            System.out.format("%40s%10d%14s", rs.getString("name"),rs.getInt("amount"),rs.getString("kind"));
            System.out.println();
        }
    }

    private void printAllSystems() {
        // TODO
    }

    private void printPrices() {
        // TODO
    }
}

package control;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Created 3/22/16
 *
 * @author Niels Norberg
 */
public class TextDriver {
    public static final double PRICEMULTIPLIER = 1.3;
    @SuppressWarnings("FieldCanBeLocal")
    private boolean running = true;

    public static void main(String[] args) throws SQLException {
        TextDriver driver = new TextDriver();
        driver.run();
    }

    public void run() throws SQLException {
        DatabaseManager.getInstance();
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
                    printComponentPrices();
                    printSystemPrices();
                    break;

                case 4:
                    handlePriceOffer(scanner);
                    break;

                case 5:
                    handleSellComponentOrSystem(scanner);
                    break;

                case 6:
                    printRestockingList();
                    break;

                case 7:
                    exitProgram();
                    break;
            }
        }
    }

    private void handlePriceOffer(Scanner scanner) throws SQLException {
        int choice;
        System.out.println("What is the name of the system you wish to sell?");
        scanner.nextLine();
        String name = scanner.nextLine();
        if (DatabaseManager.getInstance().getMaxSystemsBuildable(name) > 0) {
            System.out.println("How many of this do you wish to sell?");
            choice = scanner.nextInt();
            if (choice < 1) {
                System.out.println("Invalid amount.");
                return;
            }
            double reduction = Math.max(1 - (choice - 1) * 0.02, 0.8);
            int price = (int) (DatabaseManager.getInstance().getPriceForSystem(name) * choice * reduction);
            System.out.println("Final price for " + choice + " of " + name + " is " + price);
        } else {
            System.out.println("This system does not exist or is not in stock.");
        }
    }

    private void handleSellComponentOrSystem(Scanner scanner) throws SQLException {
        int choice;
        String name;
        while (true) {
            System.out.println("Type 1 to sell a component, 2 to sell a complete system and 3 to abort:");
            choice = scanner.nextInt();
            scanner.nextLine();
            if (choice == 1) {
                System.out.println("Please type the name of the component to sell: ");
                name = scanner.nextLine();
                if (DatabaseManager.getInstance().isInStock(name)) {
                    DatabaseManager.getInstance().sellComponent(name);
                    System.out.println("One " + name + " was sold.");
                } else {
                    System.out.println("Unable to sell as none is in stock.");
                }
                break;
            } else if (choice == 2) {
                System.out.println("Please type the name of the component to sell: ");
                name = scanner.nextLine();
                if (DatabaseManager.getInstance().getMaxSystemsBuildable(name) > 0) {
                    DatabaseManager.getInstance().sellComputerSystem(name);
                    System.out.println("One " + name + " was sold.");
                } else {
                    System.out.println("Unable to sell as some components are not in stock");
                }
                break;
            } else if (choice == 3) {
                break;
            }
        }
    }

    private void exitProgram() throws SQLException {
        if (ConnectionDriver.getInstance().getConnection() != null) {
            ConnectionDriver.getInstance().getConnection().close();
        }
        running = false;
        //System.exit(0);
    }

    private void printRestockingList() throws SQLException {
        System.out.format("%40s%10s", "Name", "Needed");
        System.out.println();
        for (int i = 1; i < DatabaseManager.getInstance().getMaxComponentId()+1; i++) {
            ResultSet component = DatabaseManager.getInstance().getComponent(i);
            if(component.next()) {
                int needed = DatabaseManager.getInstance().getAmountNeededForRestock(i);
                System.out.printf("%40s%10d", component.getString("name"), needed);
                System.out.println();
            }
        }
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
        ResultSet rs = DatabaseManager.getInstance().getAllFromTable("components");
        System.out.format("%40s%10s%14s", "Name", "Amount", "Type");
        System.out.println();
        while (rs.next()) {
            System.out.format("%40s%10d%14s", rs.getString("name"), rs.getInt("amount"), rs.getString("kind"));
            System.out.println();
        }
    }

    private void printAllSystems() throws SQLException {
        ResultSet rs = DatabaseManager.getInstance().getAllFromTable("computersystems");
        System.out.format("%40s%10s", "Name", "Buildable");
        System.out.println();
        while (rs.next()) {
            System.out.format("%40s%10d", rs.getString("name"), DatabaseManager.getInstance().getMaxSystemsBuildable(rs.getString("name")));
            System.out.println();
        }
    }

    private void printComponentPrices() throws SQLException {
        ResultSet allItems = DatabaseManager.getInstance().getAllComponentsOrdered();
        System.out.println("Components: \n");
        System.out.format("%40s%10s%14s", "Name", "Price", "Type");
        System.out.println();
        while (allItems.next()) {
            System.out.format("%40s%10.1f%14s", allItems.getString("name"), allItems.getInt("price") * PRICEMULTIPLIER, allItems.getString("kind"));
            System.out.println();
        }
    }

    private void printSystemPrices() throws SQLException {
        ResultSet allSystems = DatabaseManager.getInstance().getAllComputerSystems();
        System.out.println("Systems: \n");
        System.out.println();
        while(allSystems.next()) {
            if(DatabaseManager.getInstance().getMaxSystemsBuildable(allSystems.getString("name")) > 0) {
                System.out.println(allSystems.getString("name"));
                for (int i = 2; i < 7; i++) {
                    if (allSystems.getObject(i) != null) {
                        ResultSet rs = DatabaseManager.getInstance().getComponent(allSystems.getInt(i));
                        rs.next();
                        System.out.println(" -> " + rs.getString("name"));
                    }
                }
                System.out.println("Total price: " + DatabaseManager.getInstance().getPriceForSystem(allSystems.getString("name")));
                System.out.println();
            }
        }
    }
}

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
    private boolean running = true;
    public static void main(String[] args) throws SQLException {
        TextDriver driver = new TextDriver();
        driver.run();
    }

    public void run() throws SQLException {
        while (running) {
            printOptions();
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    printAllComponents();
                    break;

                case 2:

                    break;

                case 3:

                    break;
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
        ResultSet rs;
        rs = DatabaseManager.getInstance().fetchAllFromTable("components");
        System.out.println("Name\t\t\t| Amount\t| Type");
        while (rs.next()) {
            System.out.println(rs.getString("name") + "\t\t\t| " +rs.getInt("amount") + "\t| " + rs.getString("kind"));
        }
    }
}

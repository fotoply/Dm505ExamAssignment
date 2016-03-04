package control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created 3/4/16
 *
 * @author Niels Norberg
 */
public class MainDriver {
    public static void main(String[] args) {
        ConnectionDriver connectionDriver = ConnectionDriver.getInstance();
        connectionDriver.connect();
    }
}

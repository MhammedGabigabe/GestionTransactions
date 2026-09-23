package main.java.com.albaraka.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static String URL = "jdbc:postgresql://localhost:5432/albaraka";
    private static String USER = "postgres";
    private static String PASSWORD = "admin";

    public static Connection getConnexion()
        throws SQLException{
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

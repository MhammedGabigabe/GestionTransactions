package main.java.com.albaraka.ui;

import main.java.com.albaraka.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try (Connection cnx = DatabaseConnection.getConnexion()){
            System.out.println("Connexion réeussie !!");
        } catch (SQLException e) {
            System.out.println("Erreur de connexion: "
            + e.getMessage());
        }
    }
}

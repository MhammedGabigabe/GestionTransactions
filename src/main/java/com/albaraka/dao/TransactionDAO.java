package main.java.com.albaraka.dao;

import main.java.com.albaraka.entites.Transaction;
import main.java.com.albaraka.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionDAO {
    public void save(Transaction transaction) {

        String sql = """
                INSERT INTO transactions
                (date_transaction, montant, type, lieu, id_compte)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = DatabaseConnection.getConnexion();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setDate(1, Date.valueOf(transaction.date()));
            statement.setDouble(2, transaction.montant());
            statement.setString(3, transaction.type().name());
            statement.setString(4, transaction.lieu());
            statement.setLong(5, transaction.idCompte());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erreur lors de l'ajout de la transaction", e
            );
        }
    }
}

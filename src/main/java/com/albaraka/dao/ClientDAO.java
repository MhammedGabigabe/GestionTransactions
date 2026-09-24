package main.java.com.albaraka.dao;

import main.java.com.albaraka.entites.Client;
import main.java.com.albaraka.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class ClientDAO {

    public void save(Client client) {

        String sql = """
                INSERT INTO clients (nom, email)
                VALUES (?, ?)
                """;

        try (
                Connection connection = DatabaseConnection.getConnexion();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, client.nom());
            statement.setString(2, client.email());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erreur lors de l'ajout du client",
                    e
            );
        }
    }

    public Optional<Client> findById(Long id) {

        String sql = """
            SELECT id, nom, email
            FROM clients
            WHERE id = ?
            """;

        try (
                Connection connection = DatabaseConnection.getConnexion();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setLong(1, id);

            try (var resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Client client = new Client(
                            resultSet.getLong("id"),
                            resultSet.getString("nom"),
                            resultSet.getString("email")
                    );

                    return Optional.of(client);
                }

                return Optional.empty();
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erreur lors de la recherche du client",
                    e
            );
        }
    }


}

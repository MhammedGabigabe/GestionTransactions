package main.java.com.albaraka.dao;

import main.java.com.albaraka.entites.Client;
import main.java.com.albaraka.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    public List<Client> findAll() {

        String sql = """
            SELECT id, nom, email
            FROM clients
            """;

        List<Client> clients = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnexion();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Client client = new Client(
                        resultSet.getLong("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("email")
                );

                clients.add(client);
            }

            return clients;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erreur lors de la récupération des clients",
                    e
            );
        }
    }


}

package main.java.com.albaraka.dao;

import main.java.com.albaraka.entites.Compte;
import main.java.com.albaraka.entites.CompteCourant;
import main.java.com.albaraka.entites.CompteEpargne;
import main.java.com.albaraka.utils.DatabaseConnection;

import java.sql.*;
import java.util.Optional;

public class CompteDAO {

    public void save(Compte compte) {

        String sql = " INSERT INTO comptes (numero, solde, id_client, type_compte, decouvert_autorise, taux_interet) VALUES (?, ?, ?, ?, ?, ?) ";

        try (
                Connection connection = DatabaseConnection.getConnexion();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, compte.getNumero());
            statement.setDouble(2, compte.getSolde());
            statement.setLong(3, compte.getIdClient());

            if (compte instanceof CompteCourant compteCourant) {

                statement.setString(4, "COURANT");

                statement.setDouble(
                        5,
                        compteCourant.getDecouvertAutorise()
                );

                statement.setNull(6, Types.DECIMAL);

            } else if (compte instanceof CompteEpargne compteEpargne) {

                statement.setString(4, "EPARGNE");

                statement.setNull(5, Types.DECIMAL);

                statement.setDouble(
                        6,
                        compteEpargne.getTauxInteret()
                );
            }

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erreur lors de l'ajout du compte",
                    e
            );
        }
    }

    private Compte mapCompte(ResultSet resultSet) throws SQLException {

        Long id = resultSet.getLong("id");
        String numero = resultSet.getString("numero");
        double solde = resultSet.getDouble("solde");
        Long idClient = resultSet.getLong("id_client");

        String typeCompte = resultSet.getString("type_compte");

        if ("COURANT".equals(typeCompte)) {

            double decouvertAutorise =
                    resultSet.getDouble("decouvert_autorise");

            return new CompteCourant(
                    id,
                    numero,
                    solde,
                    idClient,
                    decouvertAutorise
            );
        }

        if ("EPARGNE".equals(typeCompte)) {

            double tauxInteret =
                    resultSet.getDouble("taux_interet");

            return new CompteEpargne(
                    id,
                    numero,
                    solde,
                    idClient,
                    tauxInteret
            );
        }

        throw new IllegalArgumentException(
                "Type de compte inconnu : " + typeCompte
        );
    }
}



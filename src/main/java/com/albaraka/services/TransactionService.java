package main.java.com.albaraka.services;

import main.java.com.albaraka.dao.CompteDAO;
import main.java.com.albaraka.dao.TransactionDAO;
import main.java.com.albaraka.entites.Transaction;
import main.java.com.albaraka.entites.TypeTransaction;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionService {
    private final TransactionDAO transactionDAO;
    private final CompteDAO compteDAO;

    public TransactionService(
            TransactionDAO transactionDAO,
            CompteDAO compteDAO
    ) {
        this.transactionDAO = transactionDAO;
        this.compteDAO = compteDAO;
    }

    public void enregistrer(Transaction transaction) {
        transactionDAO.save(transaction);
    }

    public void modifier(Transaction transaction) {
        transactionDAO.update(transaction);
    }

    public void supprimer(Long id) {
        transactionDAO.delete(id);
    }

    public Optional<Transaction> rechercherParId(Long id) {
        return transactionDAO.findById(id);
    }

    public List<Transaction> listerParCompte(Long idCompte) {

        return transactionDAO.findByCompteId(idCompte)
                .stream()
                .sorted(Comparator.comparing(Transaction::date))
                .toList();
    }

    public List<Transaction> listerParClient(Long idClient) {

        return compteDAO.findByClientId(idClient)
                .stream()
                .flatMap(compte ->
                        transactionDAO
                                .findByCompteId(compte.getId())
                                .stream()
                )
                .sorted(Comparator.comparing(Transaction::date))
                .toList();
    }

    public List<Transaction> filtrerParType(
            List<Transaction> transactions,
            TypeTransaction type
    ) {

        return transactions.stream()
                .filter(transaction ->
                        transaction.type() == type
                )
                .toList();
    }

    public List<Transaction> filtrerParMontant(
            List<Transaction> transactions,
            double montantMinimum
    ) {

        return transactions.stream()
                .filter(transaction ->
                        transaction.montant() >= montantMinimum
                )
                .toList();
    }

    public List<Transaction> filtrerParPeriode(
            List<Transaction> transactions,
            LocalDate debut,
            LocalDate fin
    ) {

        return transactions.stream()
                .filter(transaction ->
                        !transaction.date().isBefore(debut)
                                && !transaction.date().isAfter(fin)
                )
                .toList();
    }

    public List<Transaction> filtrerParLieu(
            List<Transaction> transactions,
            String lieu
    ) {

        return transactions.stream()
                .filter(transaction ->
                        transaction.lieu().equalsIgnoreCase(lieu)
                )
                .toList();
    }

    public double calculerTotal(List<Transaction> transactions) {

        return transactions.stream()
                .mapToDouble(Transaction::montant)
                .sum();
    }

    public OptionalDouble calculerMoyenne(
            List<Transaction> transactions
    ) {

        return transactions.stream()
                .mapToDouble(Transaction::montant)
                .average();
    }

    public Map<TypeTransaction, List<Transaction>> regrouperParType(
            List<Transaction> transactions
    ) {

        return transactions.stream()
                .collect(
                        Collectors.groupingBy(
                                Transaction::type
                        )
                );
    }
}

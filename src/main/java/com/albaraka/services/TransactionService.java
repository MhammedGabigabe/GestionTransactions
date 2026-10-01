package main.java.com.albaraka.services;

import main.java.com.albaraka.dao.CompteDAO;
import main.java.com.albaraka.dao.TransactionDAO;
import main.java.com.albaraka.entites.Transaction;
import main.java.com.albaraka.entites.TypeTransaction;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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

}

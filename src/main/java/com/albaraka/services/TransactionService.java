package main.java.com.albaraka.services;

import main.java.com.albaraka.dao.CompteDAO;
import main.java.com.albaraka.dao.TransactionDAO;
import main.java.com.albaraka.entites.Transaction;

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
}

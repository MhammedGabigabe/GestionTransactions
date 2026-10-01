package main.java.com.albaraka.services;

import main.java.com.albaraka.dao.CompteDAO;
import main.java.com.albaraka.dao.TransactionDAO;

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
}

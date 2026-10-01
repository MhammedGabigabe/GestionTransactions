package main.java.com.albaraka;

import main.java.com.albaraka.dao.ClientDAO;
import main.java.com.albaraka.dao.CompteDAO;
import main.java.com.albaraka.dao.TransactionDAO;
import main.java.com.albaraka.services.ClientService;
import main.java.com.albaraka.services.CompteService;
import main.java.com.albaraka.services.TransactionService;
import main.java.com.albaraka.ui.MenuUI;

public class Main {

    public static void main(String[] args) {

        ClientDAO clientDAO = new ClientDAO();
        CompteDAO compteDAO = new CompteDAO();
        TransactionDAO transactionDAO = new TransactionDAO();

        ClientService clientService = new ClientService(clientDAO);
        CompteService compteService = new CompteService(compteDAO);
        TransactionService transactionService =
                new TransactionService(transactionDAO, compteDAO);

        MenuUI menuUI = new MenuUI(clientService, compteService, transactionService);

        menuUI.demarrer();
    }
}
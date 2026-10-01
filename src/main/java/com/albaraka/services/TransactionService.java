package main.java.com.albaraka.services;

import main.java.com.albaraka.dao.CompteDAO;
import main.java.com.albaraka.dao.TransactionDAO;
import main.java.com.albaraka.entites.Compte;
import main.java.com.albaraka.entites.CompteCourant;
import main.java.com.albaraka.entites.CompteEpargne;
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


    public void effectuerDepot(Long idCompte, double montant, String lieu) {

        verifierMontant(montant);
        Compte compte = trouverCompte(idCompte);

        compte.setSolde(compte.getSolde() + montant);
        compteDAO.update(compte);

        transactionDAO.save(new Transaction(
                null, LocalDate.now(), montant,
                TypeTransaction.DEPOT, lieu, idCompte));
    }

    public void effectuerRetrait(Long idCompte, double montant, String lieu) {

        verifierMontant(montant);
        Compte compte = trouverCompte(idCompte);
        verifierDebitPossible(compte, montant);

        compte.setSolde(compte.getSolde() - montant);
        compteDAO.update(compte);

        transactionDAO.save(new Transaction(
                null, LocalDate.now(), montant,
                TypeTransaction.RETRAIT, lieu, idCompte));
    }

    public void effectuerVirement(
            Long idSource, Long idDestination, double montant, String lieu
    ) {

        verifierMontant(montant);

        if (idSource.equals(idDestination)) {
            throw new IllegalArgumentException(
                    "Le compte source et le compte destination doivent être différents.");
        }

        Compte source = trouverCompte(idSource);
        Compte destination = trouverCompte(idDestination);
        verifierDebitPossible(source, montant);

        source.setSolde(source.getSolde() - montant);
        destination.setSolde(destination.getSolde() + montant);

        compteDAO.update(source);
        compteDAO.update(destination);

        LocalDate aujourdhui = LocalDate.now();

        transactionDAO.save(new Transaction(
                null, aujourdhui, montant, TypeTransaction.VIREMENT,
                "Virement vers " + destination.getNumero(), idSource));

        transactionDAO.save(new Transaction(
                null, aujourdhui, montant, TypeTransaction.VIREMENT,
                "Virement depuis " + source.getNumero(), idDestination));
    }

    private void verifierMontant(double montant) {
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0.");
        }
    }

    private Compte trouverCompte(Long idCompte) {
        return compteDAO.findById(idCompte)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Compte introuvable (id = " + idCompte + ")."));
    }

    private void verifierDebitPossible(Compte compte, double montant) {

        double nouveauSolde = compte.getSolde() - montant;

        if (compte instanceof CompteCourant courant) {
            if (nouveauSolde < -courant.getDecouvertAutorise()) {
                throw new IllegalStateException(
                        "Retrait refusé : découvert autorisé dépassé.");
            }
        } else if (compte instanceof CompteEpargne) {
            if (nouveauSolde < 0) {
                throw new IllegalStateException(
                        "Retrait refusé : solde insuffisant (compte épargne).");
            }
        }
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

    public List<Transaction> listerToutes() {
        return transactionDAO.findAllGlobal();
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
                .filter(transaction -> transaction.type() == type)
                .toList();
    }

    public List<Transaction> filtrerParMontant(
            List<Transaction> transactions,
            double montantMinimum
    ) {

        return transactions.stream()
                .filter(transaction -> transaction.montant() >= montantMinimum)
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
                        transaction.lieu() != null
                                && transaction.lieu().equalsIgnoreCase(lieu)
                )
                .toList();
    }

    public double calculerTotal(List<Transaction> transactions) {

        return transactions.stream()
                .mapToDouble(Transaction::montant)
                .sum();
    }

    public OptionalDouble calculerMoyenne(List<Transaction> transactions) {

        return transactions.stream()
                .mapToDouble(Transaction::montant)
                .average();
    }

    public Map<TypeTransaction, List<Transaction>> regrouperParType(
            List<Transaction> transactions
    ) {

        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::type));
    }
}
package main.java.com.albaraka.ui;

import main.java.com.albaraka.entites.Client;
import main.java.com.albaraka.entites.Compte;
import main.java.com.albaraka.entites.CompteCourant;
import main.java.com.albaraka.entites.CompteEpargne;
import main.java.com.albaraka.entites.Transaction;
import main.java.com.albaraka.entites.TypeTransaction;
import main.java.com.albaraka.services.ClientService;
import main.java.com.albaraka.services.CompteService;
import main.java.com.albaraka.services.TransactionService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MenuUI {

    private final ClientService clientService;
    private final CompteService compteService;
    private final TransactionService transactionService;

    private final Scanner scanner;

    public MenuUI(
            ClientService clientService,
            CompteService compteService,
            TransactionService transactionService
    ) {
        this.clientService = clientService;
        this.compteService = compteService;
        this.transactionService = transactionService;

        this.scanner = new Scanner(System.in);
    }


    public void demarrer() {

        boolean continuer = true;

        while (continuer) {

            System.out.println();
            System.out.println("======================================");
            System.out.println("       ALBARAKA - GESTION BANCAIRE");
            System.out.println("======================================");
            System.out.println("1. Gestion des clients");
            System.out.println("2. Gestion des comptes");
            System.out.println("3. Gestion des transactions");
            System.out.println("4. Historique des transactions");
            System.out.println("5. Analyse");
            System.out.println("6. Alertes");
            System.out.println("0. Quitter");

            int choix = lireEntier("Votre choix : ");

            switch (choix) {
                case 1 -> menuClients();
                case 2 -> menuComptes();
                case 3 -> menuTransactions();
                case 4 -> menuHistorique();
                case 5 -> menuAnalyse();
                case 6 -> menuAlertes();
                case 0 -> {
                    continuer = false;
                    System.out.println("Au revoir !");
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }


    private void menuClients() {

        boolean retour = false;

        while (!retour) {

            System.out.println();
            System.out.println("===== GESTION DES CLIENTS =====");
            System.out.println("1. Ajouter un client");
            System.out.println("2. Modifier un client");
            System.out.println("3. Supprimer un client");
            System.out.println("4. Rechercher un client");
            System.out.println("5. Lister les clients");
            System.out.println("0. Retour");

            int choix = lireEntier("Votre choix : ");

            try {
                switch (choix) {
                    case 1 -> ajouterClient();
                    case 2 -> modifierClient();
                    case 3 -> supprimerClient();
                    case 4 -> rechercherClient();
                    case 5 -> listerClients();
                    case 0 -> retour = true;
                    default -> System.out.println("Choix invalide.");
                }
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
    }

    private void ajouterClient() {

        System.out.print("Nom : ");
        String nom = scanner.nextLine();

        System.out.print("Email : ");
        String email = scanner.nextLine();

        Client client = new Client(null, nom, email);
        clientService.ajouter(client);

        System.out.println("Client ajouté.");
    }

    private void modifierClient() {

        Long id = lireLong("ID du client : ");

        Optional<Client> optional = clientService.rechercherParId(id);

        if (optional.isEmpty()) {
            System.out.println("Client introuvable.");
            return;
        }

        System.out.print("Nouveau nom : ");
        String nom = scanner.nextLine();

        System.out.print("Nouvel email : ");
        String email = scanner.nextLine();

        clientService.modifier(new Client(id, nom, email));

        System.out.println("Client modifié.");
    }

    private void supprimerClient() {

        Long id = lireLong("ID du client : ");

        clientService.supprimer(id);

        System.out.println("Client supprimé.");
    }

    private void rechercherClient() {

        Long id = lireLong("ID du client : ");

        Optional<Client> optional = clientService.rechercherParId(id);

        if (optional.isPresent()) {
            afficherClient(optional.get());
        } else {
            System.out.println("Client introuvable.");
        }
    }

    private void listerClients() {

        List<Client> clients = clientService.listerTous();

        if (clients.isEmpty()) {
            System.out.println("Aucun client.");
        }

        for (Client client : clients) {
            afficherClient(client);
        }
    }

    private void afficherClient(Client client) {
        System.out.println(client.id() + " | " + client.nom() + " | " + client.email());
    }


    private void menuComptes() {

        boolean retour = false;

        while (!retour) {

            System.out.println();
            System.out.println("===== GESTION DES COMPTES =====");
            System.out.println("1. Créer un compte");
            System.out.println("2. Modifier un compte");
            System.out.println("3. Supprimer un compte");
            System.out.println("4. Rechercher par numéro");
            System.out.println("5. Rechercher par client");
            System.out.println("6. Lister les comptes");
            System.out.println("7. Compte avec solde maximum");
            System.out.println("8. Compte avec solde minimum");
            System.out.println("0. Retour");

            int choix = lireEntier("Votre choix : ");

            try {
                switch (choix) {
                    case 1 -> creerCompte();
                    case 2 -> modifierCompte();
                    case 3 -> supprimerCompte();
                    case 4 -> rechercherCompteParNumero();
                    case 5 -> rechercherComptesParClient();
                    case 6 -> listerComptes();
                    case 7 -> afficherCompteSoldeMaximum();
                    case 8 -> afficherCompteSoldeMinimum();
                    case 0 -> retour = true;
                    default -> System.out.println("Choix invalide.");
                }
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
    }

    private void creerCompte() {

        Long idClient = lireLong("ID du client : ");

        if (clientService.rechercherParId(idClient).isEmpty()) {
            System.out.println("Client introuvable.");
            return;
        }

        System.out.println("1. Compte courant");
        System.out.println("2. Compte épargne");
        int type = lireEntier("Type de compte : ");

        System.out.print("Numéro du compte : ");
        String numero = scanner.nextLine();

        double solde = lireDouble("Solde initial : ");

        if (type == 1) {
            double decouvert = lireDouble("Découvert autorisé : ");
            compteService.creer(new CompteCourant(null, numero, solde, idClient, decouvert));
            System.out.println("Compte courant créé.");

        } else if (type == 2) {
            double taux = lireDouble("Taux d'intérêt : ");
            compteService.creer(new CompteEpargne(null, numero, solde, idClient, taux));
            System.out.println("Compte épargne créé.");

        } else {
            System.out.println("Type invalide.");
        }
    }

    private void modifierCompte() {

        Long id = lireLong("ID du compte : ");

        Optional<Compte> optional = compteService.rechercherParId(id);

        if (optional.isEmpty()) {
            System.out.println("Compte introuvable.");
            return;
        }

        Compte compte = optional.get();

        System.out.print("Nouveau numéro : ");
        String numero = scanner.nextLine();

        if (compte instanceof CompteCourant) {
            double decouvert = lireDouble("Nouveau découvert autorisé : ");
            compteService.modifier(new CompteCourant(
                    id, numero, compte.getSolde(), compte.getIdClient(), decouvert));

        } else if (compte instanceof CompteEpargne) {
            double taux = lireDouble("Nouveau taux d'intérêt : ");
            compteService.modifier(new CompteEpargne(
                    id, numero, compte.getSolde(), compte.getIdClient(), taux));
        }

        System.out.println("Compte modifié.");
    }

    private void supprimerCompte() {

        Long id = lireLong("ID du compte : ");

        compteService.supprimer(id);

        System.out.println("Compte supprimé.");
    }

    private void rechercherCompteParNumero() {

        System.out.print("Numéro du compte : ");
        String numero = scanner.nextLine();

        Optional<Compte> optional = compteService.rechercherParNumero(numero);

        if (optional.isPresent()) {
            afficherCompte(optional.get());
        } else {
            System.out.println("Compte introuvable.");
        }
    }

    private void rechercherComptesParClient() {

        Long idClient = lireLong("ID du client : ");

        List<Compte> comptes = compteService.rechercherParClient(idClient);

        if (comptes.isEmpty()) {
            System.out.println("Aucun compte pour ce client.");
        }

        for (Compte compte : comptes) {
            afficherCompte(compte);
        }
    }

    private void listerComptes() {

        List<Compte> comptes = compteService.listerTous();

        if (comptes.isEmpty()) {
            System.out.println("Aucun compte.");
        }

        for (Compte compte : comptes) {
            afficherCompte(compte);
        }
    }

    private void afficherCompteSoldeMaximum() {

        Optional<Compte> optional = compteService.trouverSoldeMaximum();

        if (optional.isPresent()) {
            afficherCompte(optional.get());
        } else {
            System.out.println("Aucun compte.");
        }
    }

    private void afficherCompteSoldeMinimum() {

        Optional<Compte> optional = compteService.trouverSoldeMinimum();

        if (optional.isPresent()) {
            afficherCompte(optional.get());
        } else {
            System.out.println("Aucun compte.");
        }
    }

    private void afficherCompte(Compte compte) {

        String type = "";

        if (compte instanceof CompteCourant) {
            type = "Courant";
        } else if (compte instanceof CompteEpargne) {
            type = "Epargne";
        }

        System.out.println(compte.getId() + " | " + compte.getNumero()
                + " | " + type
                + " | solde : " + compte.getSolde()
                + " | client : " + compte.getIdClient());
    }


    private void menuTransactions() {

        boolean retour = false;

        while (!retour) {

            System.out.println();
            System.out.println("===== GESTION DES TRANSACTIONS =====");
            System.out.println("1. Enregistrer une transaction");
            System.out.println("2. Modifier une transaction");
            System.out.println("3. Supprimer une transaction");
            System.out.println("4. Rechercher par ID");
            System.out.println("5. Lister les transactions d'un compte");
            System.out.println("6. Lister les transactions d'un client");
            System.out.println("7. Lister toutes les transactions");
            System.out.println("0. Retour");

            int choix = lireEntier("Votre choix : ");

            try {
                switch (choix) {
                    case 1 -> enregistrerTransaction();
                    case 2 -> modifierTransaction();
                    case 3 -> supprimerTransaction();
                    case 4 -> rechercherTransaction();
                    case 5 -> listerTransactionsCompte();
                    case 6 -> listerTransactionsClient();
                    case 7 -> listerToutesTransactions();
                    case 0 -> retour = true;
                    default -> System.out.println("Choix invalide.");
                }
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
    }

    private void enregistrerTransaction() {

        System.out.println("1. Dépôt");
        System.out.println("2. Retrait");
        System.out.println("3. Virement");
        int type = lireEntier("Type de transaction : ");

        Long idCompte = lireLong("ID du compte : ");
        double montant = lireDouble("Montant : ");

        if (type == 1) {
            System.out.print("Lieu : ");
            String lieu = scanner.nextLine();
            transactionService.effectuerDepot(idCompte, montant, lieu);
            System.out.println("Dépôt effectué.");

        } else if (type == 2) {
            System.out.print("Lieu : ");
            String lieu = scanner.nextLine();
            transactionService.effectuerRetrait(idCompte, montant, lieu);
            System.out.println("Retrait effectué.");

        } else if (type == 3) {
            Long idDestination = lireLong("ID du compte destinataire : ");
            transactionService.effectuerVirement(idCompte, idDestination, montant, null);
            System.out.println("Virement effectué.");

        } else {
            System.out.println("Type invalide.");
        }
    }

    private void modifierTransaction() {

        Long id = lireLong("ID de la transaction : ");

        Optional<Transaction> optional = transactionService.rechercherParId(id);

        if (optional.isEmpty()) {
            System.out.println("Transaction introuvable.");
            return;
        }

        Transaction t = optional.get();

        System.out.print("Nouveau lieu : ");
        String lieu = scanner.nextLine();

        transactionService.modifier(new Transaction(
                t.id(), t.date(), t.montant(), t.type(), lieu, t.idCompte()));

        System.out.println("Transaction modifiée.");
    }

    private void supprimerTransaction() {

        Long id = lireLong("ID de la transaction : ");

        transactionService.supprimer(id);

        System.out.println("Transaction supprimée.");
    }

    private void rechercherTransaction() {

        Long id = lireLong("ID de la transaction : ");

        Optional<Transaction> optional = transactionService.rechercherParId(id);

        if (optional.isPresent()) {
            afficherTransaction(optional.get());
        } else {
            System.out.println("Transaction introuvable.");
        }
    }

    private void listerTransactionsCompte() {

        Long idCompte = lireLong("ID du compte : ");

        afficherTransactions(transactionService.listerParCompte(idCompte));
    }

    private void listerTransactionsClient() {

        Long idClient = lireLong("ID du client : ");

        afficherTransactions(transactionService.listerParClient(idClient));
    }

    private void listerToutesTransactions() {

        afficherTransactions(transactionService.listerToutes());
    }

    private void afficherTransaction(Transaction t) {

        System.out.println(t.id() + " | " + t.date()
                + " | " + t.type()
                + " | " + t.montant()
                + " | " + t.lieu()
                + " | compte : " + t.idCompte());
    }

    private void afficherTransactions(List<Transaction> transactions) {

        if (transactions.isEmpty()) {
            System.out.println("Aucune transaction.");
        }

        for (Transaction t : transactions) {
            afficherTransaction(t);
        }
    }


    private void menuHistorique() {

        boolean retour = false;

        while (!retour) {

            System.out.println();
            System.out.println("===== HISTORIQUE =====");
            System.out.println("1. Historique d'un compte");
            System.out.println("2. Historique d'un client");
            System.out.println("3. Filtrer par type");
            System.out.println("4. Filtrer par montant");
            System.out.println("5. Filtrer par période");
            System.out.println("6. Filtrer par lieu");
            System.out.println("0. Retour");

            int choix = lireEntier("Votre choix : ");

            try {
                switch (choix) {
                    case 1 -> listerTransactionsCompte();
                    case 2 -> listerTransactionsClient();
                    case 3 -> filtrerParType();
                    case 4 -> filtrerParMontant();
                    case 5 -> filtrerParPeriode();
                    case 6 -> filtrerParLieu();
                    case 0 -> retour = true;
                    default -> System.out.println("Choix invalide.");
                }
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
    }

    private void filtrerParType() {

        System.out.println("1. Retrait");
        System.out.println("2. Dépôt");
        System.out.println("3. Virement");
        int choix = lireEntier("Type : ");

        TypeTransaction type;

        if (choix == 1) {
            type = TypeTransaction.RETRAIT;
        } else if (choix == 2) {
            type = TypeTransaction.DEPOT;
        } else if (choix == 3) {
            type = TypeTransaction.VIREMENT;
        } else {
            System.out.println("Type invalide.");
            return;
        }

        afficherTransactions(transactionService.filtrerParType(
                transactionService.listerToutes(), type));
    }

    private void filtrerParMontant() {

        double minimum = lireDouble("Montant minimum : ");

        afficherTransactions(transactionService.filtrerParMontant(
                transactionService.listerToutes(), minimum));
    }

    private void filtrerParPeriode() {

        LocalDate debut = lireDate("Date de début (aaaa-mm-jj) : ");
        LocalDate fin = lireDate("Date de fin (aaaa-mm-jj) : ");

        afficherTransactions(transactionService.filtrerParPeriode(
                transactionService.listerToutes(), debut, fin));
    }

    private void filtrerParLieu() {

        System.out.print("Lieu : ");
        String lieu = scanner.nextLine();

        afficherTransactions(transactionService.filtrerParLieu(
                transactionService.listerToutes(), lieu));
    }


    private void menuAnalyse() {

        boolean retour = false;

        while (!retour) {

            System.out.println();
            System.out.println("===== ANALYSE =====");
            System.out.println("1. Total des transactions");
            System.out.println("2. Moyenne des transactions");
            System.out.println("3. Regrouper par type");
            System.out.println("4. Top 5 clients par solde");
            System.out.println("5. Rapport mensuel");
            System.out.println("0. Retour");

            int choix = lireEntier("Votre choix : ");

            try {
                switch (choix) {
                    case 1 -> calculerTotalTransactions();
                    case 2 -> calculerMoyenneTransactions();
                    case 3 -> regrouperTransactionsParType();
                    case 4 -> afficherTop5Clients();
                    case 5 -> afficherRapportMensuel();
                    case 0 -> retour = true;
                    default -> System.out.println("Choix invalide.");
                }
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
    }

    private void calculerTotalTransactions() {

        double total = transactionService.calculerTotal(transactionService.listerToutes());

        System.out.println("Total des montants : " + total);
    }

    private void calculerMoyenneTransactions() {

        List<Transaction> transactions = transactionService.listerToutes();

        if (transactions.isEmpty()) {
            System.out.println("Aucune transaction.");
            return;
        }

        double moyenne = transactionService.calculerMoyenne(transactions).getAsDouble();

        System.out.println("Montant moyen : " + moyenne);
    }

    private void regrouperTransactionsParType() {

        List<Transaction> transactions = transactionService.listerToutes();

        TypeTransaction[] types = TypeTransaction.values();

        for (TypeTransaction type : types) {

            System.out.println();
            System.out.println("--- " + type + " ---");

            boolean trouve = false;

            for (Transaction t : transactions) {
                if (t.type() == type) {
                    afficherTransaction(t);
                    trouve = true;
                }
            }

            if (!trouve) {
                System.out.println("Aucune transaction.");
            }
        }
    }

    private void afficherTop5Clients() {

        List<Client> clients = clientService.listerTous();

        if (clients.isEmpty()) {
            System.out.println("Aucun client.");
            return;
        }

        int n = clients.size();
        double[] totaux = new double[n];

        // calcul du solde total de chaque client
        for (int i = 0; i < n; i++) {
            List<Compte> comptes = compteService.rechercherParClient(clients.get(i).id());
            double somme = 0;
            for (Compte compte : comptes) {
                somme = somme + compte.getSolde();
            }
            totaux[i] = somme;
        }

        // tri à bulles (du plus grand au plus petit)
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (totaux[j] < totaux[j + 1]) {

                    double tempTotal = totaux[j];
                    totaux[j] = totaux[j + 1];
                    totaux[j + 1] = tempTotal;

                    Client tempClient = clients.get(j);
                    clients.set(j, clients.get(j + 1));
                    clients.set(j + 1, tempClient);
                }
            }
        }

        int limite = Math.min(5, n);

        for (int i = 0; i < limite; i++) {
            System.out.println((i + 1) + ". " + clients.get(i).nom() + " : " + totaux[i]);
        }
    }

    private void afficherRapportMensuel() {

        int annee = lireEntier("Année : ");
        int mois = lireEntier("Mois (1-12) : ");

        List<Transaction> transactions = transactionService.listerToutes();

        int nbDepots = 0;
        int nbRetraits = 0;
        int nbVirements = 0;
        double totalDepots = 0;
        double totalRetraits = 0;
        double totalVirements = 0;

        for (Transaction t : transactions) {

            if (t.date().getYear() == annee && t.date().getMonthValue() == mois) {

                if (t.type() == TypeTransaction.DEPOT) {
                    nbDepots++;
                    totalDepots = totalDepots + t.montant();
                } else if (t.type() == TypeTransaction.RETRAIT) {
                    nbRetraits++;
                    totalRetraits = totalRetraits + t.montant();
                } else if (t.type() == TypeTransaction.VIREMENT) {
                    nbVirements++;
                    totalVirements = totalVirements + t.montant();
                }
            }
        }

        System.out.println("===== RAPPORT " + mois + "/" + annee + " =====");
        System.out.println("Dépôts    : " + nbDepots + " (total " + totalDepots + ")");
        System.out.println("Retraits  : " + nbRetraits + " (total " + totalRetraits + ")");
        System.out.println("Virements : " + nbVirements + " (total " + totalVirements + ")");
    }


    private void menuAlertes() {

        boolean retour = false;

        while (!retour) {

            System.out.println();
            System.out.println("===== ALERTES =====");
            System.out.println("1. Transactions suspectes");
            System.out.println("2. Comptes inactifs");
            System.out.println("3. Transactions montant élevé");
            System.out.println("4. Transactions avec lieu inhabituel");
            System.out.println("5. Fréquence excessive");
            System.out.println("0. Retour");

            int choix = lireEntier("Votre choix : ");

            try {
                switch (choix) {
                    case 1 -> detecterTransactionsSuspectes();
                    case 2 -> detecterComptesInactifs();
                    case 3 -> detecterMontantsEleves();
                    case 4 -> detecterLieuxInhabituels();
                    case 5 -> detecterFrequenceExcessive();
                    case 0 -> retour = true;
                    default -> System.out.println("Choix invalide.");
                }
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
    }

    // une transaction est suspecte si son montant dépasse 10000
    private void detecterTransactionsSuspectes() {

        detecterMontantsEleves();
    }

    private void detecterMontantsEleves() {

        double seuil = lireDouble("Seuil (ex: 10000) : ");

        List<Transaction> transactions = transactionService.listerToutes();

        boolean trouve = false;

        for (Transaction t : transactions) {
            if (t.montant() >= seuil) {
                System.out.println("ALERTE montant élevé :");
                afficherTransaction(t);
                trouve = true;
            }
        }

        if (!trouve) {
            System.out.println("Aucune transaction au-dessus de ce seuil.");
        }
    }

    // un compte est inactif s'il n'a aucune transaction depuis X jours
    private void detecterComptesInactifs() {

        int jours = lireEntier("Nombre de jours d'inactivité : ");

        LocalDate limite = LocalDate.now().minusDays(jours);

        List<Compte> comptes = compteService.listerTous();

        boolean trouve = false;

        for (Compte compte : comptes) {

            List<Transaction> transactions = transactionService.listerParCompte(compte.getId());

            boolean actif = false;

            for (Transaction t : transactions) {
                if (!t.date().isBefore(limite)) {
                    actif = true;
                }
            }

            if (!actif) {
                System.out.println("Compte inactif :");
                afficherCompte(compte);
                trouve = true;
            }
        }

        if (!trouve) {
            System.out.println("Aucun compte inactif.");
        }
    }

    // on demande le lieu habituel du compte, puis on affiche les transactions faites ailleurs
    private void detecterLieuxInhabituels() {

        Long idCompte = lireLong("ID du compte : ");

        System.out.print("Lieu habituel : ");
        String lieuHabituel = scanner.nextLine();

        List<Transaction> transactions = transactionService.listerParCompte(idCompte);

        boolean trouve = false;

        for (Transaction t : transactions) {
            if (t.lieu() != null && !t.lieu().equalsIgnoreCase(lieuHabituel)) {
                System.out.println("ALERTE lieu inhabituel :");
                afficherTransaction(t);
                trouve = true;
            }
        }

        if (!trouve) {
            System.out.println("Aucune transaction dans un lieu inhabituel.");
        }
    }

    // plus de 3 transactions le même jour sur le même compte
    private void detecterFrequenceExcessive() {

        List<Compte> comptes = compteService.listerTous();

        boolean trouve = false;

        for (Compte compte : comptes) {

            List<Transaction> transactions = transactionService.listerParCompte(compte.getId());

            for (Transaction t1 : transactions) {

                int compteur = 0;

                for (Transaction t2 : transactions) {
                    if (t1.date().equals(t2.date())) {
                        compteur++;
                    }
                }

                if (compteur > 3) {
                    System.out.println("ALERTE fréquence excessive : compte " + compte.getNumero()
                            + " a " + compteur + " transactions le " + t1.date());
                    trouve = true;
                    break;
                }
            }
        }

        if (!trouve) {
            System.out.println("Aucune fréquence excessive détectée.");
        }
    }


    private int lireEntier(String message) {

        while (true) {

            try {

                System.out.print(message);

                return Integer.parseInt(scanner.nextLine());

            } catch (NumberFormatException e) {

                System.out.println("Veuillez saisir un nombre valide.");
            }
        }
    }

    private Long lireLong(String message) {

        while (true) {

            try {

                System.out.print(message);

                return Long.parseLong(scanner.nextLine());

            } catch (NumberFormatException e) {

                System.out.println("Veuillez saisir un nombre valide.");
            }
        }
    }

    private double lireDouble(String message) {

        while (true) {

            try {

                System.out.print(message);

                return Double.parseDouble(scanner.nextLine());

            } catch (NumberFormatException e) {

                System.out.println("Veuillez saisir un nombre valide.");
            }
        }
    }

    private LocalDate lireDate(String message) {

        while (true) {

            try {

                System.out.print(message);

                return LocalDate.parse(scanner.nextLine());

            } catch (Exception e) {

                System.out.println("Format attendu : aaaa-mm-jj.");
            }
        }
    }
}
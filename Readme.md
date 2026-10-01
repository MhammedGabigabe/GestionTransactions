# Albaraka – Gestion bancaire

Application console Java de gestion bancaire : clients, comptes (courant et épargne), transactions, historique, analyses et alertes. Les données sont stockées dans une base de données relationnelle via JDBC.

## Description

L'application permet de :

- **Clients** : ajouter, modifier, supprimer, rechercher et lister.
- **Comptes** : créer un compte courant (avec découvert autorisé) ou épargne (avec taux d'intérêt), rechercher par numéro ou par client, trouver les soldes maximum et minimum.
- **Transactions** : effectuer des dépôts, retraits et virements avec mise à jour automatique du solde.
- **Historique** : consulter l'historique d'un compte ou d'un client et filtrer par type, montant, période ou lieu.
- **Analyse** : total, moyenne, regroupement par type, top 5 des clients par solde, rapport mensuel.
- **Alertes** : transactions suspectes, comptes inactifs, montants élevés, lieux inhabituels, fréquence excessive.

### Règles métier

- Le montant d'une transaction doit être supérieur à 0.
- Un **compte courant** ne peut pas dépasser son découvert autorisé.
- Un **compte épargne** ne peut pas avoir un solde négatif.
- Un virement nécessite deux comptes différents.

## Technologies

- Java 17 ou supérieur (`record`, `sealed`, `instanceof` avec pattern)
- JDBC
- Base de données relationnelle (MySQL ou PostgreSQL, selon votre configuration)
- Maven ou IntelliJ IDEA (au choix)
- Git

## Structure du projet

```
albaraka/
├── src/
│   └── main/java/com/albaraka/
│       ├── Main.java                 # Point d'entrée
│       ├── entites/
│       │   ├── Client.java           # record
│       │   ├── Compte.java           # classe scellée abstraite
│       │   ├── CompteCourant.java
│       │   ├── CompteEpargne.java
│       │   ├── Transaction.java      # record
│       │   └── TypeTransaction.java  # RETRAIT, DEPOT, VIREMENT
│       ├── dao/
│       │   ├── ClientDAO.java
│       │   ├── CompteDAO.java
│       │   └── TransactionDAO.java
│       ├── services/
│       │   ├── ClientService.java
│       │   ├── CompteService.java
│       │   └── TransactionService.java
│       ├── ui/
│       │   └── MenuUI.java           # Menus console
│       └── utils/
│           └── DatabaseConnection.java
└── README.md
```

Architecture en couches : **UI → Services → DAO → Base de données**.

## Prérequis

- JDK 17 ou supérieur
- Un serveur de base de données (MySQL ou PostgreSQL) en cours d'exécution
- Le driver JDBC correspondant ajouté au projet
- Un IDE (IntelliJ IDEA recommandé) ou Maven

## Installation et lancement

1. Cloner le dépôt :
   ```bash
   git clone https://github.com/MhammedGabigabe/GestionTransactions.git
   cd albaraka
   ```

2. Créer la base de données et les tables `clients`, `comptes` et `transactions` avec les colonnes suivantes :

   | Table | Colonnes |
      |---|---|
   | `clients` | `id`, `nom`, `email` |
   | `comptes` | `id`, `numero`, `solde`, `id_client`, `type_compte` (`COURANT` / `EPARGNE`), `decouvert_autorise`, `taux_interet` |
   | `transactions` | `id`, `date_transaction`, `montant`, `type`, `lieu`, `id_compte` |

3. Configurer la connexion (URL, utilisateur, mot de passe) dans `DatabaseConnection.java`.



## Auteur

Projet réalisé par **Mhammed Gabigabe** dans le cadre de la formation YouCode.
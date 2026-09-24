package main.java.com.albaraka.entites;

public sealed abstract class Compte
        permits CompteCourant, CompteEpargne {

    protected Long id;
    protected String numero;
    protected double solde;
    protected Long idClient;
}

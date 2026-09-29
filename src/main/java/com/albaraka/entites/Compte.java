package main.java.com.albaraka.entites;

public sealed abstract class Compte
        permits CompteCourant, CompteEpargne {

    protected Long id;
    protected String numero;
    protected double solde;
    protected Long idClient;

    public Compte(Long id, String numero, double solde, Long idClient) {
        this.id = id;
        this.numero = numero;
        this.solde = solde;
        this.idClient = idClient;
    }

    public Long getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public double getSolde() {
        return solde;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

}

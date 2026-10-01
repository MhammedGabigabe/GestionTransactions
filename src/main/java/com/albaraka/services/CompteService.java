package main.java.com.albaraka.services;

import main.java.com.albaraka.dao.CompteDAO;
import main.java.com.albaraka.entites.Compte;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class CompteService {
    private final CompteDAO compteDAO;

    public CompteService(CompteDAO compteDAO) {
        this.compteDAO = compteDAO;
    }

    public void creer(Compte compte) {
        compteDAO.save(compte);
    }

    public void modifier(Compte compte) {
        compteDAO.update(compte);
    }

    public void supprimer(Long id) {
        compteDAO.delete(id);
    }

    public Optional<Compte> rechercherParId(Long id) {
        return compteDAO.findById(id);
    }

    public List<Compte> rechercherParClient(Long idClient) {
        return compteDAO.findByClientId(idClient);
    }

    public List<Compte> listerTous() {
        return compteDAO.findAll();
    }

    public Optional<Compte> rechercherParNumero(String numero) {
        return compteDAO.findByNumero(numero);
    }

    public Optional<Compte> trouverSoldeMaximum() {

        return compteDAO.findAll()
                .stream()
                .max(Comparator.comparingDouble(Compte::getSolde));
    }

    public Optional<Compte> trouverSoldeMinimum() {

        return compteDAO.findAll()
                .stream()
                .min(Comparator.comparingDouble(Compte::getSolde));
    }
}

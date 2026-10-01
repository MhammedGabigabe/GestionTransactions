package main.java.com.albaraka.services;

import main.java.com.albaraka.dao.CompteDAO;
import main.java.com.albaraka.entites.Compte;

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
}

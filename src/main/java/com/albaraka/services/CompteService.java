package main.java.com.albaraka.services;

import main.java.com.albaraka.dao.CompteDAO;

public class CompteService {
    private final CompteDAO compteDAO;

    public CompteService(CompteDAO compteDAO) {
        this.compteDAO = compteDAO;
    }
}

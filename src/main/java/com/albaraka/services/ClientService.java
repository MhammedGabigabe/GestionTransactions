package main.java.com.albaraka.services;

import main.java.com.albaraka.dao.ClientDAO;
import main.java.com.albaraka.entites.Client;

public class ClientService {
    private final ClientDAO clientDAO;

    public ClientService(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    public void ajouter(Client client) {
        clientDAO.save(client);
    }

    public void modifier(Client client) {
        clientDAO.update(client);
    }
}

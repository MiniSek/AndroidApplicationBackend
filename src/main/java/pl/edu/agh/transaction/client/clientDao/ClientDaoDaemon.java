package pl.edu.agh.transaction.client.clientDao;

import pl.edu.agh.transaction.client.clientModels.Client;

import java.util.List;

public interface ClientDaoDaemon {
    List<Client> getClients();
    void update(String email, Client newClient);
}

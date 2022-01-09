package pl.edu.agh.transaction.client.clientDao;

import pl.edu.agh.transaction.client.clientModels.Client;

import java.util.List;

public interface ClientDao extends ClientDaoServiceLayer, ClientDaoDaemon {
    void insert(Client client);
    Client getClientByEmail(String email);
    boolean isClientInDatabase(String email);
    void update(String email, Client newClient);
    List<Client> getClients();
}

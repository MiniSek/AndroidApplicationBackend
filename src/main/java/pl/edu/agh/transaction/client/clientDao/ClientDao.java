package pl.edu.agh.transaction.client.clientDao;

import pl.edu.agh.transaction.client.clientModels.Client;

import java.util.List;

public interface ClientDao {
    void insert(Client client);
    Client getClientByEmail(String email);
    boolean isClientInDatabase(String email);
    List<Client> getClients();
    void update(String email, Client newClient);
}

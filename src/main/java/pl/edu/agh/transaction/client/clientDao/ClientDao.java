package pl.edu.agh.transaction.client.clientDao;

import pl.edu.agh.transaction.client.clientModels.Client;


public interface ClientDao {
    void insert(Client client);
    Client getClientByEmail(String email);
    boolean isClientInDatabase(String email);
    void update(String email, Client newClient);
}

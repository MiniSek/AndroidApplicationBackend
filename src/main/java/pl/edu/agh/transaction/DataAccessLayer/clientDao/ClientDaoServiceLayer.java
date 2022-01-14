package pl.edu.agh.transaction.DataAccessLayer.clientDao;

import pl.edu.agh.transaction.Utils.Model.Client;


public interface ClientDaoServiceLayer {
    void insert(Client client);
    Client getClientByEmail(String email);
    boolean isEmailTaken(String email);
    void update(String email, Client newClient);
}

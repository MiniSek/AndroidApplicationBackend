package pl.edu.agh.transaction.DataAccessLayer.clientDao;

import pl.edu.agh.transaction.Utils.Model.Client;

import java.util.List;

public interface ClientDaoDaemon {
    List<Client> getClients();
    void update(String email, Client newClient);
}

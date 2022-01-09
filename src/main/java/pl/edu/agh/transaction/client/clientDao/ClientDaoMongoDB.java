package pl.edu.agh.transaction.client.clientDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.client.clientModels.Client;
import pl.edu.agh.transaction.client.clientModels.ClientRepository;
import pl.edu.agh.transaction.exception.IllegalDatabaseState;
import pl.edu.agh.transaction.exception.ObjectNotFoundException;

import java.util.List;

@Service
public class ClientDaoMongoDB implements ClientDao {
    protected final ClientRepository clientRepository;

    @Autowired
    public ClientDaoMongoDB(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void insert(Client client) {
        clientRepository.insert(client);
    }

    @Override
    public Client getClientByEmail(String email) throws IllegalDatabaseState, ObjectNotFoundException {
        List<Client> clientsWithEmail = clientRepository.findByEmail(email);
        if(clientsWithEmail.size() > 1)
            throw new IllegalDatabaseState(String.format("There are %d clients with the same email %s", clientsWithEmail.size(), email));
        else if(clientsWithEmail.size() == 1)
            return clientsWithEmail.get(0);
        else
            throw new ObjectNotFoundException(String.format("There is no such client with email %s", email));
    }

    @Override
    public boolean isClientInDatabase(String email) throws IllegalDatabaseState {
        List<Client> clientsWithEmail = clientRepository.findByEmail(email);
        if(clientsWithEmail.size() > 1)
            throw new IllegalDatabaseState(String.format("There is %d clients with the same email %s", clientsWithEmail.size(), email));
        else return clientsWithEmail.size() == 1;
    }

    @Override
    public void update(String email, Client newClient) throws IllegalDatabaseState, ObjectNotFoundException {
        getClientByEmail(email);
        clientRepository.save(newClient);
    }

    @Override
    public List<Client> getClients() {
        return clientRepository.findAll();
    }
}

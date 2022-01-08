package pl.edu.agh.transaction.security.userDetailsService;

import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.client.clientModels.Client;
import pl.edu.agh.transaction.client.clientDao.ClientDao;
import pl.edu.agh.transaction.exception.IllegalDatabaseState;
import pl.edu.agh.transaction.exception.ObjectNotFoundException;

@Service
public class UserDetailsServiceDecorated extends UserDetailsServiceImpl {
    public UserDetailsServiceDecorated(ClientDao clientDao) {
        super(clientDao);
    }

    public boolean isClientLogged(String email) throws IllegalDatabaseState, ObjectNotFoundException {
        Client client = clientDao.getClientByEmail(email);
        return client.getJwtToken() != null;
    }
}

package pl.edu.agh.transaction.logout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.client.clientDao.ClientDao;
import pl.edu.agh.transaction.client.clientModels.Client;
import pl.edu.agh.transaction.exception.IllegalDatabaseState;
import pl.edu.agh.transaction.exception.ObjectNotFoundException;


@Service
public class LogoutService {
    private final ClientDao clientDao;

    @Autowired
    public LogoutService(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public ResponseEntity<String> logout() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            Client client = clientDao.getClientByEmail(email);
            client.setJwtToken(null);
            clientDao.update(email, client);

            return new ResponseEntity<>("Logout successful", HttpStatus.OK);
        } catch(IllegalDatabaseState e) {
            return new ResponseEntity<>("Logout failure - server internal failure", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(ObjectNotFoundException e) {
            return new ResponseEntity<>("Login failure - client not found", HttpStatus.NOT_FOUND);
        }
    }
}
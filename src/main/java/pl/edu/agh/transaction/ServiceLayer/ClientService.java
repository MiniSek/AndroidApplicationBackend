package pl.edu.agh.transaction.ServiceLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.Utils.Model.Client;
import pl.edu.agh.transaction.DataAccessLayer.clientDao.ClientDaoServiceLayer;
import pl.edu.agh.transaction.Utils.ServerException.IllegalDatabaseState;
import pl.edu.agh.transaction.Utils.ServerException.ObjectNotFoundException;


@Service
public class ClientService {
    private final ClientDaoServiceLayer clientDaoServiceLayer;

    @Autowired
    public ClientService(ClientDaoServiceLayer clientDaoServiceLayer) {
        this.clientDaoServiceLayer = clientDaoServiceLayer;
    }

    public ResponseEntity<Client> getClient() {
        try {
            String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Client client = clientDaoServiceLayer.getClientByEmail(email);
            return new ResponseEntity<>(client, HttpStatus.OK);
        } catch(IllegalDatabaseState e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(ObjectNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}

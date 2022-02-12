package pl.edu.agh.transaction.client;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.client.clientDao.ClientDao;
import pl.edu.agh.transaction.client.clientModels.Client;
import pl.edu.agh.transaction.exception.IllegalDatabaseState;
import pl.edu.agh.transaction.exception.ObjectNotFoundException;

import static pl.edu.agh.transaction.client.clientModels.roles.ClientRole.PREMIUM;


@Service
public class ClientService {
    private final ClientDao clientDao;

    @Autowired
    public ClientService(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public ResponseEntity<LocalDate> getSubscriptionEndDate() {
        try {
            String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Client client = clientDao.getClientByEmail(email);
            if(client.getRoles().contains(new SimpleGrantedAuthority(PREMIUM.name())))
                return new ResponseEntity<>(client.getSubscriptionEndDate(), HttpStatus.OK);
            else
                return new ResponseEntity<>(null, HttpStatus.OK);
        } catch(IllegalDatabaseState e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(ObjectNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}

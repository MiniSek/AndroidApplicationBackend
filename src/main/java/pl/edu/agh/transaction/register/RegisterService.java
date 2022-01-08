package pl.edu.agh.transaction.register;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.client.clientModels.Client;
import pl.edu.agh.transaction.client.clientDao.ClientDao;
import pl.edu.agh.transaction.exception.IllegalDatabaseState;

import static pl.edu.agh.transaction.client.clientModels.roles.ClientRole.NON_PREMIUM;

@Service
public class RegisterService {
    private final PasswordEncoder passwordEncoder;
    private final ClientDao clientDao;

    @Autowired
    public RegisterService(PasswordEncoder passwordEncoder, ClientDao clientDao) {
        this.passwordEncoder = passwordEncoder;
        this.clientDao = clientDao;
    }

    public ResponseEntity<String> addNewClient(RegisterRequest registerRequest) {
        if(!isConsistentWithRFC822(registerRequest.getEmail()))
            return new ResponseEntity<>("Register failure - email is not consistent with RFC822", HttpStatus.BAD_REQUEST);

        try {
            if(clientDao.isClientInDatabase(registerRequest.getEmail()))
                return new ResponseEntity<>("Register failure - email taken", HttpStatus.BAD_REQUEST);
        } catch(IllegalDatabaseState e) {
            return new ResponseEntity<>("Register failure - server internal failure", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Client newClient = new Client(registerRequest.getFirstName(), registerRequest.getLastName(),
                registerRequest.getEmail(), passwordEncoder.encode(registerRequest.getPassword()),
                Sets.newHashSet(new SimpleGrantedAuthority(NON_PREMIUM.name())), null, null, null);
        clientDao.insert(newClient);

        return new ResponseEntity<>("Registration successful", HttpStatus.OK);
    }

    //TODO : fix it
    private boolean isConsistentWithRFC822(String email) {
        return email.contains("@");
    }
}

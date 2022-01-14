package pl.edu.agh.transaction.ServiceLayer;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.Utils.Model.RegisterRequest;
import pl.edu.agh.transaction.Utils.Model.Client;
import pl.edu.agh.transaction.DataAccessLayer.clientDao.ClientDaoServiceLayer;

import static pl.edu.agh.transaction.Utils.Model.clientRole.ClientRole.NON_PREMIUM;

@Service
public class RegisterService {
    private final PasswordEncoder passwordEncoder;
    private final ClientDaoServiceLayer clientDaoServiceLayer;

    @Autowired
    public RegisterService(PasswordEncoder passwordEncoder, ClientDaoServiceLayer clientDaoServiceLayer) {
        this.passwordEncoder = passwordEncoder;
        this.clientDaoServiceLayer = clientDaoServiceLayer;
    }

    public ResponseEntity<String> register(RegisterRequest registerRequest) {
        if(!isEmailConsistent(registerRequest.getEmail()))
            return new ResponseEntity<>("Register failure - email is not consistent with RFC822", HttpStatus.BAD_REQUEST);

        if(clientDaoServiceLayer.isEmailTaken(registerRequest.getEmail()))
            return new ResponseEntity<>("Register failure - email taken", HttpStatus.BAD_REQUEST);

        Client newClient = new Client(registerRequest.getFirstName(), registerRequest.getLastName(),
                registerRequest.getEmail(), passwordEncoder.encode(registerRequest.getPassword()),
                Sets.newHashSet(new SimpleGrantedAuthority(NON_PREMIUM.name())), null, null, null);
        clientDaoServiceLayer.insert(newClient);

        return new ResponseEntity<>("Registration successful", HttpStatus.OK);
    }

    //TODO : fix it
    private boolean isEmailConsistent(String email) {
        return email.contains("@");
    }
}

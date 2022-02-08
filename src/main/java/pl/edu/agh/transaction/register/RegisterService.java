package pl.edu.agh.transaction.register;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.client.clientDao.ClientDao;
import pl.edu.agh.transaction.client.clientModels.Client;
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
        if(!isRequestCorrect(registerRequest))
            return new ResponseEntity<>("Register failure - sth is not correct", HttpStatus.BAD_REQUEST);

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

    private boolean isRequestCorrect(RegisterRequest registerRequest) {
        String firstName = registerRequest.getFirstName(),
                lastName = registerRequest.getLastName(),
                email = registerRequest.getEmail(),
                password = registerRequest.getPassword();

        for(int i=0; i<firstName.length(); i++)
            if(!isLetter(firstName.charAt(i)))
                return false;

        for(int i=0; i<lastName.length(); i++)
            if(!(isLetter(lastName.charAt(i)) || isDash(lastName.charAt(i))))
                return false;

        for(int i=0; i<email.length(); i++)
            if(!(isLetter(email.charAt(i)) || isDash(email.charAt(i)) ||
                    isDigit(email.charAt(i)) || email.charAt(i) == '@'))
                return false;
        if(!email.contains("@") || (email.indexOf('@') > 0 && email.indexOf('@') < email.length()-1))
            return false;

        for(int i=0; i<password.length(); i++)
            if(!(isLetter(password.charAt(i)) || isDash(password.charAt(i)) || isDigit(password.charAt(i))))
                return false;
        return true;
    }

    private boolean isLetter(char c) {
        return (c >= 65 && c <= 90) || (c >= 97 && c <= 122);
    }

    private boolean isDash(char c) {
        return c == 45;
    }

    private boolean isDigit(char c) {
        return c >= 48 && c <= 57;
    }
}

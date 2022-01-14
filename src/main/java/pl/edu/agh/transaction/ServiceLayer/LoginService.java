package pl.edu.agh.transaction.ServiceLayer;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.Utils.Model.LoginRequest;
import pl.edu.agh.transaction.Utils.Model.Client;
import pl.edu.agh.transaction.DataAccessLayer.clientDao.ClientDaoServiceLayer;
import pl.edu.agh.transaction.Utils.ServerException.IllegalDatabaseState;
import pl.edu.agh.transaction.Utils.ServerException.ObjectNotFoundException;

import java.time.LocalDate;
import java.util.Date;

@Service
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final ClientDaoServiceLayer clientDaoServiceLayer;

    @Value("${JWT_KEY}")
    private String JWT_KEY;

    @Value("${JWT_VALID_DAYS}")
    private Integer JWT_VALID_DAYS;

    @Value("${JWT_AUTH_HEADER_PREFIX}")
    private String JWT_AUTH_HEADER_PREFIX;

    @Autowired
    public LoginService(AuthenticationManager authenticationManager, ClientDaoServiceLayer clientDaoServiceLayer) {
        this.authenticationManager = authenticationManager;
        this.clientDaoServiceLayer = clientDaoServiceLayer;
    }

    public ResponseEntity<String> login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            Client client = clientDaoServiceLayer.getClientByEmail(loginRequest.getEmail());

            java.sql.Date expirationDate = java.sql.Date.valueOf(LocalDate.now().plusDays(JWT_VALID_DAYS));

            String jwtToken = Jwts.builder()
                    .setSubject(client.getEmail())
                    .claim("authorities", client.getRoles())
                    .setIssuedAt(new Date())
                    .setExpiration(expirationDate)
                    .signWith(Keys.hmacShaKeyFor(JWT_KEY.getBytes()))
                    .compact();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", JWT_AUTH_HEADER_PREFIX + jwtToken);

            client.setJwtToken(jwtToken);

            String clientEmail = loginRequest.getEmail();
            clientDaoServiceLayer.update(clientEmail, client);

            return new ResponseEntity<>("Login successful", headers, HttpStatus.OK);
        } catch(BadCredentialsException | UsernameNotFoundException e) {
            return new ResponseEntity<>("Login failure - authentication failure", HttpStatus.FORBIDDEN);
        } catch(IllegalDatabaseState e) {
            return new ResponseEntity<>("Login failure - server internal failure", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(ObjectNotFoundException e) {
            return new ResponseEntity<>("Login failure - client not found", HttpStatus.NOT_FOUND);
        }
    }
}
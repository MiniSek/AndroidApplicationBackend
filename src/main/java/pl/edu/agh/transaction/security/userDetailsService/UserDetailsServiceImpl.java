package pl.edu.agh.transaction.security.userDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.client.clientDao.ClientDao;
import pl.edu.agh.transaction.client.clientModels.Client;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserDetailsServiceFilter {
    private final ClientDao clientDao;

    @Autowired
    public UserDetailsServiceImpl(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Client client = clientDao.getClientByEmail(email);
            return new User(client.getEmail(), client.getPassword(), client.getRoles());
        } catch(IllegalStateException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    @Override
    public boolean isClientLogged(String clientEmail, String token) {
        Client client = clientDao.getClientByEmail(clientEmail);
        return client.getJwtToken().equals(token);
    }
}

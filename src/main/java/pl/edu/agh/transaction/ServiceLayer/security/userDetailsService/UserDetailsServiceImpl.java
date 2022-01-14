package pl.edu.agh.transaction.ServiceLayer.security.userDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.DataAccessLayer.clientDao.ClientDaoServiceLayer;
import pl.edu.agh.transaction.Utils.Model.Client;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserDetailsServiceFilter {
    private final ClientDaoServiceLayer clientDaoServiceLayer;

    @Autowired
    public UserDetailsServiceImpl(ClientDaoServiceLayer clientDaoServiceLayer) {
        this.clientDaoServiceLayer = clientDaoServiceLayer;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Client client = clientDaoServiceLayer.getClientByEmail(email);
            return new User(client.getEmail(), client.getPassword(), client.getRoles());
        } catch(IllegalStateException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    @Override
    public boolean isClientLogged(String clientEmail) {
        Client client = clientDaoServiceLayer.getClientByEmail(clientEmail);
        return client.getJwtToken() != null;
    }
}

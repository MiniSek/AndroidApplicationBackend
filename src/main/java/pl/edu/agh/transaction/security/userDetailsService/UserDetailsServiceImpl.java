package pl.edu.agh.transaction.security.userDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.edu.agh.transaction.client.clientDao.ClientDao;
import pl.edu.agh.transaction.client.clientModels.Client;

public class UserDetailsServiceImpl implements UserDetailsService {
    protected final ClientDao clientDao;

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

}

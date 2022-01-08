package pl.edu.agh.transaction.daemon.clientRoleCleaner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pl.edu.agh.transaction.client.clientDao.ClientDao;
import pl.edu.agh.transaction.client.clientDao.ClientDaoDecorator;
import pl.edu.agh.transaction.client.clientModels.Client;
import pl.edu.agh.transaction.client.clientModels.roles.ClientRole;

import java.util.Set;
import java.util.TimerTask;

import static pl.edu.agh.transaction.client.clientModels.roles.ClientRole.PREMIUM;


public class ClientRoleCleaner extends TimerTask {
    private final ClientDaoDecorator clientDaoDecorator;
    private final ClientDao clientDao;

    @Autowired
    public ClientRoleCleaner(ClientDaoDecorator clientDaoDecorator, ClientDao clientDao) {
        this.clientDaoDecorator = clientDaoDecorator;
        this.clientDao = clientDao;
    }

    @Override
    public void run() {
        //TODO add synchronized
        for (Client client : clientDaoDecorator.getClients())
            if(client.getRoles().contains(new SimpleGrantedAuthority(PREMIUM.name())) && client.getSubscriptionEndDate().isBeforeNow()) {
                String clientEmail = client.getEmail();
                Set<GrantedAuthority> roles = client.getRoles();
                roles.remove(new SimpleGrantedAuthority(ClientRole.PREMIUM.name()));
                client.setRoles(roles);

                clientDao.update(clientEmail, client);
            }
    }
}

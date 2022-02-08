package pl.edu.agh.transaction.daemon.clientRoleCleaner;

import org.joda.time.LocalDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pl.edu.agh.transaction.client.clientDao.ClientDao;
import pl.edu.agh.transaction.client.clientModels.Client;
import pl.edu.agh.transaction.client.clientModels.roles.ClientRole;

import java.util.Set;
import java.util.TimerTask;

import static pl.edu.agh.transaction.client.clientModels.roles.ClientRole.PREMIUM;


public class ClientRoleCleaner extends TimerTask {
    private final ClientDao clientDao;

    public ClientRoleCleaner(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Override
    public void run() {
        LocalDate todayDate = new LocalDate();
        for (Client client : clientDao.getClients())
            if(client.getRoles().contains(new SimpleGrantedAuthority(PREMIUM.name())) &&
                    client.getSubscriptionEndDate().isBefore(todayDate)) {

                String clientEmail = client.getEmail();
                Set<GrantedAuthority> roles = client.getRoles();
                roles.remove(new SimpleGrantedAuthority(ClientRole.PREMIUM.name()));
                client.setRoles(roles);
                client.setSubscriptionStartDate(null);
                client.setSubscriptionEndDate(null);

                clientDao.update(clientEmail, client);
            }
    }
}

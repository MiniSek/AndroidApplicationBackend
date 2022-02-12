package pl.edu.agh.transaction.security;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.edu.agh.transaction.client.clientDao.ClientDao;
import pl.edu.agh.transaction.client.clientModels.Client;

import javax.annotation.PostConstruct;

import static pl.edu.agh.transaction.client.clientModels.roles.ClientRole.*;

@Component
public class AdminConfiguration {
    private final ClientDao clientDao;
    private final PasswordEncoder passwordEncoder;

    private final String adminFirstName;
    private final String adminLastName;

    private final String adminEmail;
    private final String adminPassword;

    @Autowired
    public AdminConfiguration(ClientDao clientDao, PasswordEncoder passwordEncoder,
                              @Value("${ADMIN_FIRSTNAME}") String adminFirstName, @Value("${ADMIN_LASTNAME}") String adminLastName,
                              @Value("${ADMIN_EMAIL}") String adminEmail, @Value("${ADMIN_PASSWORD}") String adminPassword) {
        this.clientDao = clientDao;
        this.passwordEncoder = passwordEncoder;

        this.adminFirstName = adminFirstName;
        this.adminLastName = adminLastName;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @PostConstruct
    public void setAdmin() {
        for (Client client : clientDao.getClients()) {
            if (client.getRoles().contains(new SimpleGrantedAuthority(ADMIN.name())))
                return;
        }

        clientDao.insert(new Client(adminFirstName, adminLastName, adminEmail, passwordEncoder.encode(adminPassword),
                Sets.newHashSet(new SimpleGrantedAuthority(NON_PREMIUM.name()), new SimpleGrantedAuthority(PREMIUM.name()),
                        new SimpleGrantedAuthority(ADMIN.name())), null, null, null));
    }
}

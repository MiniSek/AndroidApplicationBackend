package pl.edu.agh.transaction.daemon.paymentReminder;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pl.edu.agh.transaction.client.clientDao.ClientDaoDaemon;
import pl.edu.agh.transaction.client.clientModels.Client;
import pl.edu.agh.transaction.daemon.EmailService;

import java.util.TimerTask;

import static pl.edu.agh.transaction.client.clientModels.roles.ClientRole.PREMIUM;

public class PaymentReminder extends TimerTask {
    private final ClientDaoDaemon clientDaoDaemon;
    private final EmailService emailService;

    private String subject = "Subscription reminder", text = "Your subscription time is ending. Consider resubscribe our app.";

    public PaymentReminder(ClientDaoDaemon clientDaoDaemon, EmailService emailService) {
        this.clientDaoDaemon = clientDaoDaemon;
        this.emailService = emailService;
    }

    @Override
    public void run() {
        //TODO add synchronized
        for (Client client : clientDaoDaemon.getClients())
            if (client.getRoles().contains(new SimpleGrantedAuthority(PREMIUM.name())) && client.getSubscriptionEndDate().minusHours(24).isBeforeNow())
                emailService.sendMessage(client.getEmail(), subject, text);
    }
}

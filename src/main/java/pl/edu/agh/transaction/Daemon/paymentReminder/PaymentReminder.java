package pl.edu.agh.transaction.Daemon.paymentReminder;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pl.edu.agh.transaction.DataAccessLayer.clientDao.ClientDaoDaemon;
import pl.edu.agh.transaction.Utils.Model.Client;
import pl.edu.agh.transaction.Daemon.EmailService;

import java.util.TimerTask;

import static pl.edu.agh.transaction.Utils.Model.clientRole.ClientRole.PREMIUM;

public class PaymentReminder extends TimerTask {
    private final ClientDaoDaemon clientDaoDaemon;
    private final EmailService emailService;

    private final String SUBJECT = "Subscription reminder";
    private final String TEXT = "Your subscription time is ending. Consider resubscribing our app.";

    public PaymentReminder(ClientDaoDaemon clientDaoDaemon, EmailService emailService) {
        this.clientDaoDaemon = clientDaoDaemon;
        this.emailService = emailService;
    }

    @Override
    public void run() {
        //TODO add synchronized
        for (Client client : clientDaoDaemon.getClients())
            if (client.getRoles().contains(new SimpleGrantedAuthority(PREMIUM.name())) && client.getSubscriptionEndDate().minusHours(24).isBeforeNow())
                emailService.sendMessage(client.getEmail(), SUBJECT, TEXT);
    }
}

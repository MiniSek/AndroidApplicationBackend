package pl.edu.agh.transaction.daemon.paymentReminder;

import org.joda.time.LocalDate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pl.edu.agh.transaction.client.clientDao.ClientDao;
import pl.edu.agh.transaction.client.clientModels.Client;
import pl.edu.agh.transaction.daemon.utils.EmailService;
import pl.edu.agh.transaction.exception.MessageSendingException;

import java.util.TimerTask;

import static pl.edu.agh.transaction.client.clientModels.roles.ClientRole.PREMIUM;

public class PaymentReminder extends TimerTask {
    private final ClientDao clientDao;
    private final EmailService emailService;

    private final String SUBJECT;
    private final String TEXT;

    public PaymentReminder(ClientDao clientDao, EmailService emailService, String SUBJECT, String TEXT) {
        this.clientDao = clientDao;
        this.emailService = emailService;

        this.SUBJECT = SUBJECT;
        this.TEXT = TEXT;
    }

    @Override
    public void run() {
        LocalDate todayDate = new LocalDate();
        for (Client client : clientDao.getClients())
            if (client.getRoles().contains(new SimpleGrantedAuthority(PREMIUM.name())) && client.getSubscriptionEndDate() != null &&
                    client.getSubscriptionEndDate().minusDays(2).isBefore(todayDate))
                try {
                    emailService.sendEmail(client.getEmail(), SUBJECT, TEXT);
                } catch(MessageSendingException e) {
                    System.out.println("Error during sending email with payment reminder");
                    e.printStackTrace();
                }
    }
}

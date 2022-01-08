package pl.edu.agh.transaction.daemon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.client.clientDao.ClientDao;
import pl.edu.agh.transaction.client.clientDao.ClientDaoDecorator;
import pl.edu.agh.transaction.daemon.clientRoleCleaner.ClientRoleCleaner;
import pl.edu.agh.transaction.daemon.invoiceSending.InvoiceSending;
import pl.edu.agh.transaction.daemon.orderCleaner.OrderCleaner;
import pl.edu.agh.transaction.daemon.paymentReminder.PaymentReminder;
import pl.edu.agh.transaction.invoice.invoiceDao.InvoiceDao;
import pl.edu.agh.transaction.order.orderDao.OrderDaoDecorator;

import java.util.Calendar;
import java.util.Timer;

@Service
public class DaemonManager {
    private final OrderDaoDecorator orderDaoDecorator;
    private final ClientDaoDecorator clientDaoDecorator;
    private final EmailService emailService;
    private final InvoiceDao invoiceDao;
    private final ClientDao clientDao;

    private final Timer timer;

    @Autowired
    public DaemonManager(OrderDaoDecorator orderDaoDecorator, ClientDaoDecorator clientDaoDecorator,
                         EmailService emailService, InvoiceDao invoiceDao, ClientDao clientDao) {
        this.orderDaoDecorator = orderDaoDecorator;
        this.clientDaoDecorator = clientDaoDecorator;
        this.emailService = emailService;
        this.invoiceDao = invoiceDao;
        this.clientDao = clientDao;

        //TODO make daemon
        timer = new Timer();

        runDaemons();
    }

    private void runDaemons() {
        Calendar calendar = Calendar.getInstance();
        //TODO fix it
        //calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        timer.scheduleAtFixedRate(new OrderCleaner(orderDaoDecorator), calendar.getTime(), 24 * 60 * 60 * 1000);

        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        timer.scheduleAtFixedRate(new PaymentReminder(clientDaoDecorator, emailService), calendar.getTime(), 24 * 60 * 60 * 1000);

        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 42);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        timer.scheduleAtFixedRate(new InvoiceSending(invoiceDao, emailService), calendar.getTime(), 24 * 60 * 60 * 1000);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        timer.scheduleAtFixedRate(new ClientRoleCleaner(clientDaoDecorator, clientDao), calendar.getTime(), 24 * 60 * 60 * 1000);
    }
}

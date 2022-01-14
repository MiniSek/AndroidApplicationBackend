package pl.edu.agh.transaction.Daemon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.DataAccessLayer.clientDao.ClientDaoDaemon;
import pl.edu.agh.transaction.Daemon.clientRoleCleaner.ClientRoleCleaner;
import pl.edu.agh.transaction.Daemon.invoiceSending.InvoiceSending;
import pl.edu.agh.transaction.Daemon.orderCleaner.OrderCleaner;
import pl.edu.agh.transaction.Daemon.paymentReminder.PaymentReminder;
import pl.edu.agh.transaction.DataAccessLayer.invoiceDao.InvoiceDao;
import pl.edu.agh.transaction.DataAccessLayer.paymentOrderDao.PaymentOrderDaoDaemon;

import java.util.Calendar;
import java.util.Timer;

@Service
public class DaemonManager {
    private final PaymentOrderDaoDaemon paymentOrderDaoDaemon;
    private final ClientDaoDaemon clientDaoDaemon;
    private final EmailService emailService;
    private final InvoiceDao invoiceDao;

    private final Timer timer;

    @Autowired
    public DaemonManager(PaymentOrderDaoDaemon paymentOrderDaoDaemon, ClientDaoDaemon clientDaoDaemon,
                         EmailService emailService, InvoiceDao invoiceDao) {
        this.paymentOrderDaoDaemon = paymentOrderDaoDaemon;
        this.clientDaoDaemon = clientDaoDaemon;
        this.emailService = emailService;
        this.invoiceDao = invoiceDao;

        timer = new Timer(true);
        runDaemons();
    }

    private void runDaemons() {
        Calendar calendar = Calendar.getInstance();

        //TODO fix it
        //calendar.add(Calendar.DATE, 1);

        /*setCalendarTime(calendar, ORDER_CLEANER_HOUR_OF_DAY, ORDER_CLEANER_MINUTE, ORDER_CLEANER_SECOND, ORDER_CLEANER_MILLISECOND);
        timer.scheduleAtFixedRate(new OrderCleaner(paymentOrderDaoDaemon), calendar.getTime(), 24 * 60 * 60 * 1000);

        setCalendarTime(calendar, PAYMENT_REMINDER_HOUR_OF_DAY, PAYMENT_REMINDER_MINUTE, PAYMENT_REMINDER_SECOND,
                PAYMENT_REMINDER_MILLISECOND);
        timer.scheduleAtFixedRate(new PaymentReminder(clientDaoDaemon, emailService),
                calendar.getTime(), 24 * 60 * 60 * 1000);

        setCalendarTime(calendar, INVOICE_SENDING_HOUR_OF_DAY, INVOICE_SENDING_MINUTE, INVOICE_SENDING_SECOND,
                INVOICE_SENDING_MILLISECOND);
        timer.scheduleAtFixedRate(new InvoiceSending(invoiceDao, emailService),
                calendar.getTime(), 24 * 60 * 60 * 1000);

        setCalendarTime(calendar, CLIENT_CLEANER_HOUR_OF_DAY, CLIENT_CLEANER_MINUTE, CLIENT_CLEANER_SECOND,
                CLIENT_CLEANER_MILLISECOND);
        timer.scheduleAtFixedRate(new ClientRoleCleaner(clientDaoDaemon),
                calendar.getTime(), 24 * 60 * 60 * 1000);*/
    }

    private void setCalendarTime(Calendar calendar, int hour, int minute, int second, int millisecond) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
    }
}

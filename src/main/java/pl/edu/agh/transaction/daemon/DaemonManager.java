package pl.edu.agh.transaction.daemon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.client.clientDao.ClientDaoDaemon;
import pl.edu.agh.transaction.daemon.clientRoleCleaner.ClientRoleCleaner;
import pl.edu.agh.transaction.daemon.invoiceSending.InvoiceSending;
import pl.edu.agh.transaction.daemon.orderCleaner.OrderCleaner;
import pl.edu.agh.transaction.daemon.paymentReminder.PaymentReminder;
import pl.edu.agh.transaction.invoice.invoiceDao.InvoiceDao;
import pl.edu.agh.transaction.order.orderDao.OrderDaoDaemon;

import java.util.Calendar;
import java.util.Timer;

@Service
public class DaemonManager {
    private final OrderDaoDaemon orderDaoDaemon;
    private final ClientDaoDaemon clientDaoDaemon;
    private final EmailService emailService;
    private final InvoiceDao invoiceDao;

    private final Timer timer;

    private final int orderCleanerHourOfDay = 1;
    private final int orderCleanerMinute = 0;
    private final int orderCleanerSecond = 0;
    private final int orderCleanerMillisecond = 0;

    private final int paymentReminderHourOfDay = 12;
    private final int paymentReminderMinute = 0;
    private final int paymentReminderSecond = 0;
    private final int paymentReminderMillisecond = 0;

    private final int invoiceSendingHourOfDay = 10;
    private final int invoiceSendingMinute = 0;
    private final int invoiceSendingSecond = 0;
    private final int invoiceSendingMillisecond = 0;

    private final int clientRoleCleanerHourOfDay = 0;
    private final int clientRoleCleanerMinute = 0;
    private final int clientRoleCleanerSecond = 0;
    private final int clientRoleCleanerMillisecond = 0;

    @Autowired
    public DaemonManager(OrderDaoDaemon orderDaoDaemon, ClientDaoDaemon clientDaoDaemon,
                         EmailService emailService, InvoiceDao invoiceDao) {
        this.orderDaoDaemon = orderDaoDaemon;
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

        setCalendarTime(calendar, orderCleanerHourOfDay, orderCleanerMinute,
                orderCleanerSecond, orderCleanerMillisecond);
        timer.scheduleAtFixedRate(new OrderCleaner(orderDaoDaemon),
                calendar.getTime(), 24 * 60 * 60 * 1000);

        setCalendarTime(calendar, paymentReminderHourOfDay, paymentReminderMinute,
                paymentReminderSecond, paymentReminderMillisecond);
        timer.scheduleAtFixedRate(new PaymentReminder(clientDaoDaemon, emailService),
                calendar.getTime(), 24 * 60 * 60 * 1000);

        setCalendarTime(calendar, invoiceSendingHourOfDay, invoiceSendingMinute,
                invoiceSendingSecond, invoiceSendingMillisecond);
        timer.scheduleAtFixedRate(new InvoiceSending(invoiceDao, emailService),
                calendar.getTime(), 24 * 60 * 60 * 1000);

        setCalendarTime(calendar, clientRoleCleanerHourOfDay, clientRoleCleanerMinute,
                clientRoleCleanerSecond, clientRoleCleanerMillisecond);
        timer.scheduleAtFixedRate(new ClientRoleCleaner(clientDaoDaemon),
                calendar.getTime(), 24 * 60 * 60 * 1000);
    }

    private void setCalendarTime(Calendar calendar, int hour, int minute, int second, int millisecond) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
    }
}

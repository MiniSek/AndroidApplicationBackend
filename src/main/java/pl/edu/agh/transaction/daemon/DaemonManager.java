package pl.edu.agh.transaction.daemon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.client.clientDao.ClientDao;
import pl.edu.agh.transaction.daemon.clientRoleCleaner.ClientRoleCleaner;
import pl.edu.agh.transaction.daemon.invoiceSending.InvoiceSending;
import pl.edu.agh.transaction.daemon.orderCleaner.OrderCleaner;
import pl.edu.agh.transaction.daemon.paymentReminder.PaymentReminder;
import pl.edu.agh.transaction.daemon.utils.DaemonTimerHelper;
import pl.edu.agh.transaction.daemon.utils.EmailService;
import pl.edu.agh.transaction.daemon.utils.InvoiceBuilder;
import pl.edu.agh.transaction.invoice.invoiceDao.InvoiceDao;
import pl.edu.agh.transaction.order.orderDao.PaymentOrderDao;

import java.util.Calendar;
import java.util.Timer;

@Service
public class DaemonManager {
    private final PaymentOrderDao paymentOrderDao;
    private final ClientDao clientDao;
    private final EmailService emailService;
    private final InvoiceDao invoiceDao;
    private final DaemonTimerHelper daemonTimerHelper;
    private final InvoiceBuilder invoiceBuilder;

    private final Timer timer;

    private final String INVOICE_SENDING_SUBJECT;
    private final String INVOICE_SENDING_TEXT;

    private final String PAYMENT_REMINDER_SUBJECT;
    private final String PAYMENT_REMINDER_TEXT;

    @Autowired
    public DaemonManager(PaymentOrderDao paymentOrderDao, ClientDao clientDao,
                         EmailService emailService, InvoiceDao invoiceDao, DaemonTimerHelper daemonTimerHelper,
                         InvoiceBuilder invoiceBuilder,
                         @Value("${INVOICE_SENDING_SUBJECT}") String INVOICE_SENDING_SUBJECT,
                         @Value("${INVOICE_SENDING_TEXT}") String INVOICE_SENDING_TEXT,
                         @Value("${PAYMENT_REMINDER_SUBJECT}") String PAYMENT_REMINDER_SUBJECT,
                         @Value("${PAYMENT_REMINDER_TEXT}") String PAYMENT_REMINDER_TEXT) {

        this.paymentOrderDao = paymentOrderDao;
        this.clientDao = clientDao;
        this.emailService = emailService;
        this.invoiceDao = invoiceDao;
        this.daemonTimerHelper = daemonTimerHelper;
        this.invoiceBuilder = invoiceBuilder;

        this.INVOICE_SENDING_SUBJECT = INVOICE_SENDING_SUBJECT;
        this.INVOICE_SENDING_TEXT = INVOICE_SENDING_TEXT;

        this.PAYMENT_REMINDER_SUBJECT = PAYMENT_REMINDER_SUBJECT;
        this.PAYMENT_REMINDER_TEXT = PAYMENT_REMINDER_TEXT;

        timer = new Timer(true);

        runDaemons();
    }

    private void runDaemons() {
        Calendar calendar = Calendar.getInstance();

        //calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);

        daemonTimerHelper.setOrderCleanerTime(calendar);
        timer.scheduleAtFixedRate(new OrderCleaner(paymentOrderDao),
                calendar.getTime(), 24 * 60 * 60 * 1000);

        daemonTimerHelper.setPaymentReminderTime(calendar);
        timer.scheduleAtFixedRate(new PaymentReminder(clientDao, emailService, PAYMENT_REMINDER_SUBJECT, PAYMENT_REMINDER_TEXT),
                calendar.getTime(), 24 * 60 * 60 * 1000);

        daemonTimerHelper.setInvoiceSendingTime(calendar);
        timer.scheduleAtFixedRate(new InvoiceSending(invoiceDao, emailService, invoiceBuilder,
                        INVOICE_SENDING_SUBJECT, INVOICE_SENDING_TEXT), calendar.getTime(), 24 * 60 * 60 * 1000);

        daemonTimerHelper.setClientRoleCleanerTime(calendar);
        timer.scheduleAtFixedRate(new ClientRoleCleaner(clientDao),
                calendar.getTime(), 24 * 60 * 60 * 1000);
    }
}

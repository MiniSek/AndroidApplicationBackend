package pl.edu.agh.transaction.daemon.invoiceSending;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import pl.edu.agh.transaction.daemon.utils.EmailService;
import pl.edu.agh.transaction.daemon.utils.InvoiceBuilder;
import pl.edu.agh.transaction.exception.InvoiceCreatingException;
import pl.edu.agh.transaction.exception.MessageSendingException;
import pl.edu.agh.transaction.invoice.invoiceDao.InvoiceDao;
import pl.edu.agh.transaction.invoice.invoiceModels.Invoice;

import java.util.*;


public class InvoiceSending extends TimerTask {
    private final InvoiceDao invoiceDao;
    private final EmailService emailService;
    private final InvoiceBuilder invoiceBuilder;

    private final String SUBJECT;
    private final String TEXT;

    public InvoiceSending(InvoiceDao invoiceDao, EmailService emailService, InvoiceBuilder invoiceBuilder,
                          String SUBJECT, String TEXT) {
        this.invoiceDao = invoiceDao;
        this.emailService = emailService;
        this.invoiceBuilder = invoiceBuilder;

        this.SUBJECT = SUBJECT;
        this.TEXT = TEXT;
    }

    @Override
    public void run() {
        DateTime todayDate = new DateTime();
        if(todayDate.getDayOfMonth() == 1) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            LocalDate monthEnd = new LocalDate(calendar);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            LocalDate monthStart = new LocalDate(calendar);

            List<Invoice> invoices = invoiceDao.getInvoicesByDate(monthStart, monthEnd);
            List<Invoice> picked = new ArrayList<>();

            String currentClientEmail;
            int j;
            for (int i = 0; i < invoices.size(); i++) {
                picked.clear();
                currentClientEmail = invoices.get(i).getClientEmail();
                picked.add(invoices.get(i));
                j = i + 1;
                while (j < invoices.size()) {
                    if (invoices.get(j).getClientEmail().equals(currentClientEmail)) {
                        picked.add(invoices.get(j));
                        invoices.remove(j);
                    } else
                        j++;
                }
                sendBigInvoice(picked);
            }

            invoiceDao.deleteByDate(monthStart, monthEnd);
            invoiceBuilder.clearInvoiceNumber();
        }
    }

    private void sendBigInvoice(List<Invoice> clientInvoices) {
        //TODO : change to original
        String clientEmail = "dominiksulik20@gmail.com";//clientInvoices.get(0).getClientEmail();
        String clientNameAndSurname = clientInvoices.get(0).getClientFirstName() + " " +
                clientInvoices.get(0).getClientLastName();

        try {
            String pathToInvoiceFile = invoiceBuilder.createPdfInvoice(clientNameAndSurname, clientInvoices);
            emailService.sendEmailWithAttachment(clientEmail, SUBJECT, TEXT, pathToInvoiceFile);
        } catch(InvoiceCreatingException e) {
            System.out.println("Error during creating pdf invoice");
            e.printStackTrace();
        } catch(MessageSendingException e) {
            System.out.println("Error during sending email with invoice attachment");
            e.printStackTrace();
        }
    }
}

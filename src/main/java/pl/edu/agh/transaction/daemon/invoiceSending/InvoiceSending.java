package pl.edu.agh.transaction.daemon.invoiceSending;

import com.lowagie.text.DocumentException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import pl.edu.agh.transaction.daemon.EmailService;
import pl.edu.agh.transaction.invoice.invoiceDao.InvoiceDao;
import pl.edu.agh.transaction.invoice.invoiceModels.Invoice;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;


public class InvoiceSending extends TimerTask {
    private final InvoiceDao invoiceDao;
    private final EmailService emailService;

    private final int hours = 24;

    @Autowired
    public InvoiceSending(InvoiceDao invoiceDao, EmailService emailService) {
        this.invoiceDao = invoiceDao;
        this.emailService = emailService;

        //generatePdfFromHtml(parseThymeleafTemplate());
    }

    @Override
    public void run() {
        //TODO add synchronized
        //TODO create better invoice
        //TODO change to original
        //new DateTime().getDayOfMonth() == 1
        if(true) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);

            DateTime monthEnd = new DateTime(calendar);

            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            DateTime monthStart = new DateTime(calendar);

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
        }
    }

    private void sendBigInvoice(List<Invoice> clientInvoices) {
        String pathToInvoiceFile = "";
        //TODO : change to original
        String clientEmail = "dominiksulik20@gmail.com";//clientInvoices.get(0).getClientEmail();
        String subject = "Invoice";
        StringBuilder text = new StringBuilder(clientInvoices.get(0).getClientFirstName() + clientInvoices.get(0).getClientLastName() + "\n");

        for(Invoice invoice : clientInvoices)
            text.append(invoice.getPaymentDate()).append(" ").append(invoice.getPrice()).append(" ").
                    append(invoice.getSubscriptionStartDate()).append(" ").append(invoice.getSubscriptionEndDate()).append("\n");

        emailService.sendMessageWithAttachment(clientEmail, subject, text.toString(), pathToInvoiceFile);
    }

    private String parseThymeleafTemplate() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("number", "42");

        return templateEngine.process("invoice", context);
    }

    public void generatePdfFromHtml(String html) {
        String outputFolder =  "thymeleaf.pdf";
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outputFolder);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        try {
            renderer.createPDF(outputStream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

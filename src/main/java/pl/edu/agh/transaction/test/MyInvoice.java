package pl.edu.agh.transaction.test;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.transaction.invoice.invoiceDao.InvoiceDao;
import pl.edu.agh.transaction.invoice.invoiceModels.Invoice;

@RestController
@RequestMapping(path="api/v1/invoices")
public class MyInvoice {
    private final InvoiceDao invoiceDao;

    @Autowired
    public MyInvoice(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }

    @GetMapping
    public void getInvoices() {
        for(Invoice invoice : invoiceDao.getInvoicesByDate((new DateTime()).minusMinutes(30), new DateTime()))
            System.out.println(invoice);
    }
}

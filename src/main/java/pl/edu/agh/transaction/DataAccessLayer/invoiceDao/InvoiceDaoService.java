package pl.edu.agh.transaction.DataAccessLayer.invoiceDao;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.Utils.Model.Invoice;

import java.util.List;

@Service
public class InvoiceDaoService implements InvoiceDao{
    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceDaoService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public void addInvoice(Invoice invoice) {
        invoiceRepository.insert(invoice);
    }

    @Override
    public List<Invoice> getInvoicesByDate(DateTime startDate, DateTime endTime) {
        return invoiceRepository.findByDate(startDate, endTime);
    }

    @Override
    public void deleteByDate(DateTime startDate, DateTime endTime) {
        List<Invoice> invoices = invoiceRepository.findByDate(startDate, endTime);
        for(Invoice invoice : invoices)
            invoiceRepository.delete(invoice);
    }
}

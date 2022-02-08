package pl.edu.agh.transaction.invoice.invoiceDao;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.invoice.invoiceModels.Invoice;
import pl.edu.agh.transaction.invoice.invoiceModels.InvoiceRepository;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class InvoiceDaoService implements InvoiceDao{
    private final InvoiceRepository invoiceRepository;

    private final Lock lock;

    @Autowired
    public InvoiceDaoService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
        this.lock = new ReentrantLock();
    }

    @Override
    public void insert(Invoice invoice) {
        lock.lock();
        invoiceRepository.insert(invoice);
        lock.unlock();
    }

    @Override
    public List<Invoice> getInvoicesByDate(LocalDate startDate, LocalDate endTime) {
        lock.lock();
        List<Invoice> invoices = invoiceRepository.findByDate(startDate, endTime);
        lock.unlock();
        return invoices;
    }

    @Override
    public void deleteByDate(LocalDate startDate, LocalDate endTime) {
        lock.lock();
        List<Invoice> invoices = invoiceRepository.findByDate(startDate, endTime);
        for(Invoice invoice : invoices)
            invoiceRepository.delete(invoice);
        lock.unlock();
    }
}

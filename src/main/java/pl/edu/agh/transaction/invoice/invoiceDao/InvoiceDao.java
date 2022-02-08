package pl.edu.agh.transaction.invoice.invoiceDao;

import org.joda.time.LocalDate;
import pl.edu.agh.transaction.invoice.invoiceModels.Invoice;

import java.util.List;

public interface InvoiceDao {
    void insert(Invoice invoice);
    void deleteByDate(LocalDate startDate, LocalDate endTime);
    List<Invoice> getInvoicesByDate(LocalDate startDate, LocalDate endTime);
}

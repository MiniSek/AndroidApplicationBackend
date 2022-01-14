package pl.edu.agh.transaction.DataAccessLayer.invoiceDao;

import org.joda.time.DateTime;
import pl.edu.agh.transaction.Utils.Model.Invoice;

import java.util.List;

public interface InvoiceDao {
    void addInvoice(Invoice invoice);
    void deleteByDate(DateTime startDate, DateTime endTime);
    List<Invoice> getInvoicesByDate(DateTime startDate, DateTime endTime);
}

package pl.edu.agh.transaction.DataAccessLayer.invoiceDao;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import pl.edu.agh.transaction.Utils.Model.Invoice;

import java.util.List;

public interface InvoiceRepository extends MongoRepository<Invoice, String> {
    @Query(value = "{\"paymentDate\": {\"$gt\" : ?0, \"$lt\" : ?1}}")
    List<Invoice> findByDate(DateTime startDate, DateTime endDate);
}

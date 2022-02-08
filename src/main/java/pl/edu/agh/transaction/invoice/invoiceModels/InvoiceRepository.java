package pl.edu.agh.transaction.invoice.invoiceModels;

import org.joda.time.LocalDate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface InvoiceRepository extends MongoRepository<Invoice, String> {
    @Query(value = "{\"paymentDate\": {\"$gte\" : ?0, \"$lte\" : ?1}}")
    List<Invoice> findByDate(LocalDate startDate, LocalDate endDate);
}

package pl.edu.agh.transaction.invoice.invoiceModels;

import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Invoice {
    @Id
    private String id;
    private String clientEmail;
    private String clientFirstName;
    private String clientLastName;

    private DateTime paymentDate;
    private double price;

    private DateTime subscriptionStartDate;
    private DateTime subscriptionEndDate;

    public Invoice(String clientEmail, String clientFirstName, String clientLastName, DateTime paymentDate,
                   double price, DateTime subscriptionStartDate, DateTime subscriptionEndDate) {
        this.clientEmail = clientEmail;
        this.clientFirstName = clientFirstName;
        this.clientLastName = clientLastName;
        this.paymentDate = paymentDate;
        this.price = price;
        this.subscriptionStartDate = subscriptionStartDate;
        this.subscriptionEndDate = subscriptionEndDate;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id='" + id + '\'' +
                ", clientEmail='" + clientEmail + '\'' +
                ", clientFirstName='" + clientFirstName + '\'' +
                ", clientLastName='" + clientLastName + '\'' +
                ", paymentDate=" + paymentDate +
                ", price=" + price +
                ", subscriptionStartDate=" + subscriptionStartDate +
                ", subscriptionEndDate=" + subscriptionEndDate +
                '}';
    }
}

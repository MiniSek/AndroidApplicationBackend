package pl.edu.agh.transaction.order.orderModels;

import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Order {
    @Id
    private String id;
    private String orderID;
    private Double price;
    private Integer monthNumber;
    private DateTime creationDate;

    public Order(String orderID, double price, int monthNumber, DateTime creationDate) {
        this.orderID = orderID;
        this.price = price;
        this.monthNumber = monthNumber;
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", orderID='" + orderID + '\'' +
                ", price=" + price +
                ", monthNumber=" + monthNumber +
                ", creationDate=" + creationDate +
                '}';
    }
}

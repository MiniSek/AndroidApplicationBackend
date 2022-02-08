package pl.edu.agh.transaction.order.orderModels;

import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class PaymentOrder {
    @Id
    private String id;
    private String orderId;
    private Integer premiumMonthNumber;
    private Double premiumPrice;
    private DateTime expirationDate;

    public PaymentOrder(String orderId, Integer premiumMonthNumber, Double premiumPrice, DateTime expirationDate) {
        this.orderId = orderId;
        this.premiumMonthNumber = premiumMonthNumber;
        this.premiumPrice = premiumPrice;
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "PaymentOrder{" +
                "id='" + id + '\'' +
                ", orderId='" + orderId + '\'' +
                ", premiumMonthNumber=" + premiumMonthNumber +
                ", premiumPrice=" + premiumPrice +
                ", creationDate=" + expirationDate +
                '}';
    }
}
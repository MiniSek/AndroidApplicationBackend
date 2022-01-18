package pl.edu.agh.transaction.order.orderDao;

import org.joda.time.DateTime;
import pl.edu.agh.transaction.order.orderModels.PaymentOrder;

public interface PaymentOrderDaoServiceLayer {
    void addOrder(String orderID, Integer premiumMonthNumber, Double premiumPrice, DateTime creationDate);
    PaymentOrder getOrder(String orderID);
    void delete(String orderID);
}

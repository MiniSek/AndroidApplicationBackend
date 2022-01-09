package pl.edu.agh.transaction.order.orderDao;

import org.joda.time.DateTime;
import pl.edu.agh.transaction.order.orderModels.Order;

public interface OrderDaoServiceLayer {
    void addOrder(String orderID, double price, int monthNumber, DateTime creationDate);
    Order getOrder(String orderID);
    void delete(String orderID);
}

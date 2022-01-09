package pl.edu.agh.transaction.order.orderDao;

import pl.edu.agh.transaction.order.orderModels.Order;

import java.util.List;

public interface OrderDaoDaemon {
    List<Order> getOrders();
    void delete(String orderID);
}

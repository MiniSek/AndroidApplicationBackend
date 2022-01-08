package pl.edu.agh.transaction.order.orderDao;

import pl.edu.agh.transaction.order.orderModels.Order;

import java.util.List;

public interface OrderDaoDecorator extends OrderDeleteDao {
    List<Order> getOrders();
}

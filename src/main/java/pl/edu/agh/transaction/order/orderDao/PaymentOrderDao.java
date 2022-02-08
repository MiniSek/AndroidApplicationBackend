package pl.edu.agh.transaction.order.orderDao;

import pl.edu.agh.transaction.order.orderModels.PaymentOrder;

import java.util.List;

public interface PaymentOrderDao {
    void insert(PaymentOrder paymentOrder);
    PaymentOrder getOrder(String orderID);
    List<PaymentOrder> getOrders();
    void delete(String orderID);
}

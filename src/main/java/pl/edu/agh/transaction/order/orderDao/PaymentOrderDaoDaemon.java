package pl.edu.agh.transaction.order.orderDao;

import pl.edu.agh.transaction.order.orderModels.PaymentOrder;

import java.util.List;

public interface PaymentOrderDaoDaemon {
    List<PaymentOrder> getOrders();
    void delete(String orderID);
}

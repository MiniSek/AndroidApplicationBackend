package pl.edu.agh.transaction.DataAccessLayer.paymentOrderDao;

import pl.edu.agh.transaction.Utils.Model.PaymentOrder;

import java.util.List;

public interface PaymentOrderDaoDaemon {
    List<PaymentOrder> getOrders();
    void delete(String orderID);
}

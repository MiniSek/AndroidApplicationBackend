package pl.edu.agh.transaction.daemon.orderCleaner;

import pl.edu.agh.transaction.order.orderDao.PaymentOrderDao;
import pl.edu.agh.transaction.order.orderModels.PaymentOrder;

import java.util.TimerTask;

public class OrderCleaner extends TimerTask {
    private final PaymentOrderDao paymentOrderDao;

    public OrderCleaner(PaymentOrderDao paymentOrderDao) {
        this.paymentOrderDao = paymentOrderDao;
    }

    @Override
    public void run() {
        for(PaymentOrder order : paymentOrderDao.getOrders())
            if (order.getExpirationDate().isBeforeNow())
                paymentOrderDao.delete(order.getOrderId());
    }
}

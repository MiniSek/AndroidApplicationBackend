package pl.edu.agh.transaction.daemon.orderCleaner;

import pl.edu.agh.transaction.order.orderDao.PaymentOrderDaoDaemon;
import pl.edu.agh.transaction.order.orderModels.PaymentOrder;

import java.util.TimerTask;

public class OrderCleaner extends TimerTask {
    private final PaymentOrderDaoDaemon paymentOrderDaoDaemon;

    public OrderCleaner(PaymentOrderDaoDaemon paymentOrderDaoDaemon) {
        this.paymentOrderDaoDaemon = paymentOrderDaoDaemon;
    }

    @Override
    public void run() {
        for(PaymentOrder order : paymentOrderDaoDaemon.getOrders())
            if(order.getCreationDate().plusHours(3).isBeforeNow())
                paymentOrderDaoDaemon.delete(order.getOrderId());
    }
}

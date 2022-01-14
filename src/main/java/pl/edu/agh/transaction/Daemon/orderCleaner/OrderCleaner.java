package pl.edu.agh.transaction.Daemon.orderCleaner;

import pl.edu.agh.transaction.DataAccessLayer.paymentOrderDao.PaymentOrderDaoDaemon;
import pl.edu.agh.transaction.Utils.Model.PaymentOrder;

import java.util.TimerTask;

public class OrderCleaner extends TimerTask {
    private final PaymentOrderDaoDaemon paymentOrderDaoDaemon;

    public OrderCleaner(PaymentOrderDaoDaemon paymentOrderDaoDaemon) {
        this.paymentOrderDaoDaemon = paymentOrderDaoDaemon;
    }

    @Override
    public void run() {
        //TODO : add synchronized
        for(PaymentOrder order : paymentOrderDaoDaemon.getOrders())
            if(order.getCreationDate().plusHours(3).isBeforeNow())
                paymentOrderDaoDaemon.delete(order.getOrderId());
    }
}

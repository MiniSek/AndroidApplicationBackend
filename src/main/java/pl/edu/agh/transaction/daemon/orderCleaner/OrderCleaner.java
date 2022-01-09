package pl.edu.agh.transaction.daemon.orderCleaner;

import pl.edu.agh.transaction.order.orderDao.OrderDaoDaemon;
import pl.edu.agh.transaction.order.orderModels.Order;

import java.util.TimerTask;

public class OrderCleaner extends TimerTask {
    private final OrderDaoDaemon orderDaoDaemon;

    public OrderCleaner(OrderDaoDaemon orderDaoDaemon) {
        this.orderDaoDaemon = orderDaoDaemon;
    }

    @Override
    public void run() {
        for(Order order : orderDaoDaemon.getOrders())
            if(order.getCreationDate().plusHours(3).isBeforeNow())
                orderDaoDaemon.delete(order.getOrderID());
    }
}

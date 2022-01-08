package pl.edu.agh.transaction.daemon.orderCleaner;

import pl.edu.agh.transaction.order.orderDao.OrderDaoDecorator;
import pl.edu.agh.transaction.order.orderModels.Order;

import java.util.TimerTask;

public class OrderCleaner extends TimerTask {
    private final OrderDaoDecorator orderDaoDecorator;

    public OrderCleaner(OrderDaoDecorator orderDaoDecorator) {
        this.orderDaoDecorator = orderDaoDecorator;
    }

    @Override
    public void run() {
        for(Order order : orderDaoDecorator.getOrders())
            if(order.getCreationDate().plusHours(3).isBeforeNow())
                orderDaoDecorator.delete(order.getOrderID());
    }
}

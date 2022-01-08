package pl.edu.agh.transaction.order.orderDao;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.exception.IllegalDatabaseState;
import pl.edu.agh.transaction.exception.ObjectNotFoundException;
import pl.edu.agh.transaction.order.orderModels.Order;
import pl.edu.agh.transaction.order.orderModels.OrderRepository;

import java.util.List;

@Service
public class OrderDaoService implements OrderDao, OrderDaoDecorator {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderDaoService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void addOrder(String orderID, double price, int monthNumber, DateTime creationDate) {
        orderRepository.insert(new Order(orderID, price, monthNumber, creationDate));
    }

    @Override
    public Order getOrder(String orderID) throws IllegalDatabaseState, ObjectNotFoundException {
        List<Order> orders = orderRepository.findByOrderID(orderID);
        if(orders.size() > 1)
            throw new IllegalDatabaseState(String.format("There is %d orders with the same id %s", orders.size(), orderID));
        else if(orders.size() == 1)
            return orders.get(0);
        throw new ObjectNotFoundException(String.format("There is no such order with ID %s", orderID));
    }

    @Override
    public void delete(String orderID) throws IllegalDatabaseState, ObjectNotFoundException {
        Order order = getOrder(orderID);
        orderRepository.delete(order);
    }

    @Override
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }
}

package pl.edu.agh.transaction.order.orderDao;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.exception.IllegalDatabaseState;
import pl.edu.agh.transaction.exception.ObjectNotFoundException;
import pl.edu.agh.transaction.order.orderModels.PaymentOrder;
import pl.edu.agh.transaction.order.orderModels.PaymentOrderRepository;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class PaymentOrderDaoMongoDB implements PaymentOrderDao {
    protected final PaymentOrderRepository paymentOrderRepository;

    private final Lock lock;

    @Autowired
    public PaymentOrderDaoMongoDB(PaymentOrderRepository paymentOrderRepository) {
        this.paymentOrderRepository = paymentOrderRepository;
        this.lock = new ReentrantLock();
    }

    @Override
    public void insert(PaymentOrder paymentOrder) {
        lock.lock();
        paymentOrderRepository.insert(paymentOrder);
        lock.unlock();
    }

    @Override
    public PaymentOrder getOrder(String orderID) throws IllegalDatabaseState, ObjectNotFoundException {
        lock.lock();
        List<PaymentOrder> orders = paymentOrderRepository.findByOrderId(orderID);
        lock.unlock();
        if(orders.size() > 1)
            throw new IllegalDatabaseState(String.format("There is %d orders with the same id %s", orders.size(), orderID));
        else if(orders.size() == 1)
            return orders.get(0);
        throw new ObjectNotFoundException(String.format("There is no such order with ID %s", orderID));
    }


    @Override
    public void delete(String orderID) throws IllegalDatabaseState, ObjectNotFoundException {
        lock.lock();
        PaymentOrder order = getOrder(orderID);
        paymentOrderRepository.delete(order);
        lock.unlock();
    }

    @Override
    public List<PaymentOrder> getOrders() {
        lock.lock();
        List<PaymentOrder> paymentOrders = paymentOrderRepository.findAll();
        lock.unlock();
        return paymentOrders;
    }
}
package pl.edu.agh.transaction.DataAccessLayer.paymentOrderDao;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.Utils.ServerException.IllegalDatabaseState;
import pl.edu.agh.transaction.Utils.ServerException.ObjectNotFoundException;
import pl.edu.agh.transaction.Utils.Model.PaymentOrder;

import java.util.List;

@Service
public class PaymentOrderDaoMongoDB implements PaymentOrderDao {
    protected final PaymentOrderRepository paymentOrderRepository;

    @Autowired
    public PaymentOrderDaoMongoDB(PaymentOrderRepository paymentOrderRepository) {
        this.paymentOrderRepository = paymentOrderRepository;
    }

    @Override
    public void addOrder(String orderID, Integer premiumMonthNumber, Double premiumPrice, DateTime creationDate) {
        paymentOrderRepository.insert(new PaymentOrder(orderID, premiumMonthNumber, premiumPrice, creationDate));
    }

    @Override
    public PaymentOrder getOrder(String orderID) throws IllegalDatabaseState, ObjectNotFoundException {
        List<PaymentOrder> orders = paymentOrderRepository.findByOrderID(orderID);
        if(orders.size() > 1)
            throw new IllegalDatabaseState(String.format("There is %d orders with the same id %s", orders.size(), orderID));
        else if(orders.size() == 1)
            return orders.get(0);
        throw new ObjectNotFoundException(String.format("There is no such order with ID %s", orderID));
    }


    @Override
    public void delete(String orderID) throws IllegalDatabaseState, ObjectNotFoundException {
        PaymentOrder order = getOrder(orderID);
        paymentOrderRepository.delete(order);
    }

    @Override
    public List<PaymentOrder> getOrders() {
        return paymentOrderRepository.findAll();
    }
}

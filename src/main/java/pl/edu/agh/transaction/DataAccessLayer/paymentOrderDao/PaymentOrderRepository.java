package pl.edu.agh.transaction.DataAccessLayer.paymentOrderDao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import pl.edu.agh.transaction.Utils.Model.PaymentOrder;

import java.util.List;

public interface PaymentOrderRepository extends MongoRepository<PaymentOrder, String> {
    @Query(value = "{'orderID': ?0}")
    List<PaymentOrder> findByOrderID(String orderID);
}

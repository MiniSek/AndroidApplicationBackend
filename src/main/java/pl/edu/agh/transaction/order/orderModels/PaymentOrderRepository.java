package pl.edu.agh.transaction.order.orderModels;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PaymentOrderRepository extends MongoRepository<PaymentOrder, String> {
    @Query(value = "{'orderId': ?0}")
    List<PaymentOrder> findByOrderId(String orderID);
}

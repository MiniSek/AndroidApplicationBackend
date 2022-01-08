package pl.edu.agh.transaction.order.orderModels;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    @Query(value = "{'orderID': ?0}")
    List<Order> findByOrderID(String orderID);
}

package pl.edu.agh.transaction.DataAccessLayer.clientDao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import pl.edu.agh.transaction.Utils.Model.Client;

import java.util.List;

public interface ClientRepository extends MongoRepository<Client, String> {
    @Query(value = "{'email': ?0}")
    List<Client> findByEmail(String email);
}

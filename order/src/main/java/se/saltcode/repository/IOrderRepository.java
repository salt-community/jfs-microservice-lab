package se.saltcode.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import se.saltcode.model.order.Orders;
import java.util.UUID;

@Repository
public interface IOrderRepository extends ListCrudRepository<Orders, UUID> {
}

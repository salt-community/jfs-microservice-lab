package se.saltcode.repository;

import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import se.saltcode.model.order.Order;

@Repository
public interface IOrderRepository extends ListCrudRepository<Order, UUID> {}

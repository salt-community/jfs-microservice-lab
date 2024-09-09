package se.saltcode.repository;

import org.springframework.data.repository.ListCrudRepository;
import se.saltcode.model.order.Order;

import java.util.UUID;

public interface IOrderRepository extends ListCrudRepository<Order, UUID> {
}

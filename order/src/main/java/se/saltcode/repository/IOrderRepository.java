package se.saltcode.repository;

import org.springframework.data.repository.ListCrudRepository;
import se.saltcode.model.order.Orders;

import java.util.UUID;

public interface IOrderRepository extends ListCrudRepository<Orders, UUID> {
}

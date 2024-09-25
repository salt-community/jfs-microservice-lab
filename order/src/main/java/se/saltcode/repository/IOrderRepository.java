package se.saltcode.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.saltcode.model.order.Order;

@Repository
public interface IOrderRepository extends JpaRepository<Order, UUID> {}

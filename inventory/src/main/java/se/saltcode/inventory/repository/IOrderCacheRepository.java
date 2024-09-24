package se.saltcode.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.saltcode.inventory.model.cache.OrderCache;
import se.saltcode.inventory.model.inventory.Inventory;

import java.util.UUID;

@Repository
public interface IOrderCacheRepository extends JpaRepository<OrderCache, UUID> {
}

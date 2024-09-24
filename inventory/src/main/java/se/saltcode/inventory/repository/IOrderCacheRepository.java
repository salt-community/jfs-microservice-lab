package se.saltcode.inventory.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.saltcode.inventory.model.cache.OrderCache;

@Repository
public interface IOrderCacheRepository extends JpaRepository<OrderCache, UUID> {}

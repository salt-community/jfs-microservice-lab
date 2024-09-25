package se.saltcode.inventory.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.saltcode.inventory.model.inventory.Inventory;

@Repository
public interface IInventoryRepository extends JpaRepository<Inventory, UUID> {}

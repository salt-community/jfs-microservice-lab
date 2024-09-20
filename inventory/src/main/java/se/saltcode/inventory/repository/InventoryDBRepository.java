package se.saltcode.inventory.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.saltcode.inventory.model.Inventory;

@Repository
public interface InventoryDBRepository extends JpaRepository<Inventory, UUID> {
  // refactored to JPA does ListCrud but more

  // Custom method to get item by its product name
  List<Inventory> findByProduct(String Product);
}

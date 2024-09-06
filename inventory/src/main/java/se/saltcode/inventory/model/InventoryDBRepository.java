package se.saltcode.inventory.model;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InventoryDBRepository extends ListCrudRepository<Inventory, UUID> {

}

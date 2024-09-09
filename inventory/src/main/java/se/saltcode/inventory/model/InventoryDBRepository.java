package se.saltcode.inventory.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InventoryDBRepository extends JpaRepository<Inventory, UUID> {
    // refactored to JPA does ListCrud but more
}
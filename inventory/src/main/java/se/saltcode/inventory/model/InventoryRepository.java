package se.saltcode.inventory.model;

import org.springframework.stereotype.Repository;

@Repository
public class InventoryRepository {

    InventoryDBRepository inventoryDBRepository;

    // constructor
    public InventoryRepository(InventoryDBRepository inventoryDBRepository) {
        this.inventoryDBRepository = inventoryDBRepository;
    }




}

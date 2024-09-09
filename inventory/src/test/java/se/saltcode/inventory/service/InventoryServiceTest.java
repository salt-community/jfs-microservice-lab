package se.saltcode.inventory.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.saltcode.inventory.model.Inventory;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {

    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
      //  inventoryService = new InventoryService();
    }

    @Test
    void testGetAllInventoryItems_emptyList() {
        List<Inventory> result = inventoryService.getAllInventoryItems();
        assertTrue(result.isEmpty(), "Inventory list should be empty initially");
    }

    @Test
    void testCreateInventoryItem() {
        Inventory newItem = new Inventory();
        newItem.setProduct("Test Product");
        newItem.setQuantity(10);
        newItem.setReservedQuantity(2);

        Inventory createdItem = inventoryService.createInventoryItem(newItem);

        assertNotNull(createdItem.getId(), "Created item should have a UUID");
        assertEquals("Test Product", createdItem.getProduct());
        assertEquals(10, createdItem.getQuantity());
        assertEquals(2, createdItem.getReservedQuantity());
        assertEquals(1, inventoryService.getAllInventoryItems().size(), "One item should exist in the inventory");
    }

    @Test
    void testGetInventoryItemById_found() {
        Inventory newItem = new Inventory();
        newItem.setProduct("Test Product");
        newItem.setQuantity(10);
        newItem.setReservedQuantity(2);
        Inventory createdItem = inventoryService.createInventoryItem(newItem);

        Inventory foundItem = inventoryService.getInventoryItemById(createdItem.getId());

        assertNotNull(foundItem);
        assertEquals(createdItem.getId(), foundItem.getId());
    }

    @Test
    void testGetInventoryItemById_notFound() {
        Inventory foundItem = inventoryService.getInventoryItemById(UUID.randomUUID());
        assertNull(foundItem, "Item should not be found if it doesn't exist");
    }

    @Test
    void testUpdateInventoryItem_found() {
        Inventory newItem = new Inventory();
        newItem.setProduct("Test Product");
        newItem.setQuantity(10);
        newItem.setReservedQuantity(2);
        Inventory createdItem = inventoryService.createInventoryItem(newItem);

        Inventory updatedItemData = new Inventory();
        updatedItemData.setProduct("Updated Product");
        updatedItemData.setQuantity(15);
        updatedItemData.setReservedQuantity(3);

        Inventory updatedItem = inventoryService.updateInventoryItem(createdItem.getId(), updatedItemData);

        assertNotNull(updatedItem);
        assertEquals("Updated Product", updatedItem.getProduct());
        assertEquals(15, updatedItem.getQuantity());
        assertEquals(3, updatedItem.getReservedQuantity());
    }

    @Test
    void testUpdateInventoryItem_notFound() {
        Inventory updatedItemData = new Inventory();
        updatedItemData.setProduct("Updated Product");
        updatedItemData.setQuantity(15);
        updatedItemData.setReservedQuantity(3);

        Inventory updatedItem = inventoryService.updateInventoryItem(UUID.randomUUID(), updatedItemData);

        assertNull(updatedItem, "Update should return null if item is not found");
    }

    @Test
    void testDeleteInventoryItem_found() {
        Inventory newItem = new Inventory();
        newItem.setProduct("Test Product");
        newItem.setQuantity(10);
        newItem.setReservedQuantity(2);
        Inventory createdItem = inventoryService.createInventoryItem(newItem);

        boolean deleted = inventoryService.deleteInventoryItem(createdItem.getId());

        assertTrue(deleted, "Item should be successfully deleted");
        assertTrue(inventoryService.getAllInventoryItems().isEmpty(), "Inventory should be empty after deletion");
    }

    @Test
    void testDeleteInventoryItem_notFound() {
        boolean deleted = inventoryService.deleteInventoryItem(UUID.randomUUID());
        assertFalse(deleted, "Item should not be deleted if not found");
    }
}
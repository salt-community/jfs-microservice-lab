package se.saltcode.inventory.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.saltcode.inventory.model.Inventory;
import se.saltcode.inventory.service.InventoryService;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    // Test to check if an empty inventory list returns a 200 OK status and an empty list
    @Test
    void testGetAllInventoryItems_emptyList() throws Exception {
        // Arrange
        when(inventoryService.getAllInventoryItems()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // Test to check if a new inventory item can be created successfully and returns a 201 Created status
    @Test
    void testCreateInventoryItem() throws Exception {
        // Arrange
        Inventory newItem = new Inventory();
        newItem.setId(UUID.randomUUID());
        newItem.setProduct("New Product");
        newItem.setQuantity(10);
        newItem.setReservedQuantity(1);

        when(inventoryService.createInventoryItem(any(Inventory.class))).thenReturn(newItem);

        // Act & Assert
        mockMvc.perform(post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"product\":\"New Product\",\"quantity\":10,\"reservedQuantity\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.product").value("New Product"))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.reservedQuantity").value(1));
    }

    // Test for retrieving an existing inventory item by ID and ensuring a 200 OK status
    @Test
    void testGetInventoryItemById_found() throws Exception {
        // Arrange
        Inventory item = new Inventory();
        item.setId(UUID.randomUUID());
        item.setProduct("Existing Product");
        item.setQuantity(10);
        item.setReservedQuantity(1);

        when(inventoryService.getInventoryItemById(any(UUID.class))).thenReturn(item);

        // Act & Assert
        mockMvc.perform(get("/api/inventory/{id}", item.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product").value("Existing Product"));
    }

    // Test for retrieving an inventory item by ID when the item is not found, ensuring a 404 Not Found status
    @Test
    void testGetInventoryItemById_notFound() throws Exception {
        // Arrange
        when(inventoryService.getInventoryItemById(any(UUID.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/inventory/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // Test for deleting an existing inventory item and ensuring a 204 No Content status
    @Test
    void testDeleteInventoryItem() throws Exception {
        // Arrange
        when(inventoryService.deleteInventoryItem(any(UUID.class))).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/inventory/{id}", UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }

    // Test for updating an existing inventory item successfully and ensuring a 200 OK status
    @Test
    void testUpdateInventoryItem() throws Exception {
        // Arrange
        Inventory updatedItem = new Inventory();
        updatedItem.setId(UUID.randomUUID());
        updatedItem.setProduct("Updated Product");
        updatedItem.setQuantity(15);
        updatedItem.setReservedQuantity(2);

        when(inventoryService.updateInventoryItem(any(UUID.class), any(Inventory.class))).thenReturn(updatedItem);

        // Act & Assert
        mockMvc.perform(put("/api/inventory/{id}", updatedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"product\":\"Updated Product\",\"quantity\":15,\"reservedQuantity\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product").value("Updated Product"))
                .andExpect(jsonPath("$.quantity").value(15))
                .andExpect(jsonPath("$.reservedQuantity").value(2));
    }

    // Test for updating an inventory item that does not exist, ensuring a 404 Not Found status
    @Test
    void testUpdateInventoryItem_notFound() throws Exception {
        // Arrange
        when(inventoryService.updateInventoryItem(any(UUID.class), any(Inventory.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/api/inventory/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"product\":\"Updated Product\",\"quantity\":15,\"reservedQuantity\":2}"))
                .andExpect(status().isNotFound());
    }
}
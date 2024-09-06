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

    @Test
    void testGetAllInventoryItems_emptyList() throws Exception {
        when(inventoryService.getAllInventoryItems()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testCreateInventoryItem() throws Exception {
        Inventory newItem = new Inventory();
        newItem.setId(UUID.randomUUID());
        newItem.setProduct("New Product");
        newItem.setQuantity(10);
        newItem.setReservedQuantity(1);

        when(inventoryService.createInventoryItem(any(Inventory.class))).thenReturn(newItem);

        mockMvc.perform(post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"product\":\"New Product\",\"quantity\":10,\"reservedQuantity\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.product").value("New Product"))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.reservedQuantity").value(1));
    }

    @Test
    void testGetInventoryItemById_found() throws Exception {
        Inventory item = new Inventory();
        item.setId(UUID.randomUUID());
        item.setProduct("Existing Product");
        item.setQuantity(10);
        item.setReservedQuantity(1);

        when(inventoryService.getInventoryItemById(any(UUID.class))).thenReturn(item);

        mockMvc.perform(get("/api/inventory/{id}", item.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product").value("Existing Product"));
    }

    @Test
    void testGetInventoryItemById_notFound() throws Exception {
        when(inventoryService.getInventoryItemById(any(UUID.class))).thenReturn(null);

        mockMvc.perform(get("/api/inventory/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteInventoryItem() throws Exception {
        when(inventoryService.deleteInventoryItem(any(UUID.class))).thenReturn(true);

        mockMvc.perform(delete("/api/inventory/{id}", UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }
}
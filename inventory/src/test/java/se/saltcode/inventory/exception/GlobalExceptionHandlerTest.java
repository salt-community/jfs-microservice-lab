package se.saltcode.inventory.exception;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ExceptionHandler;
import se.saltcode.inventory.model.cache.OrderCache;
import se.saltcode.inventory.service.InventoryService;
import se.saltcode.inventory.service.OrderCacheService;

@EnableRuleMigrationSupport
@WebMvcTest
class GlobalExceptionHandlerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private InventoryService inventoryService;
  @MockBean private OrderCacheService orderCacheService;

  // Test to handle IllegalArgumentException thrown by the service, ensuring a 400 Bad Request
  // status
  @Test
  void testHandleIllegalArgumentException() throws Exception {
    // Arrange
    when(inventoryService.getInventoryItemById(any(UUID.class)))
        .thenThrow(new IllegalArgumentException("Invalid UUID"));

    // Act & Assert
    mockMvc
        .perform(
            get("/api/inventory/{id}", UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Invalid input: Invalid UUID"));
  }

  // Test to handle RuntimeException thrown by the service, ensuring a 400 Bad Request status
  @Test
  void testHandleRuntimeException() throws Exception {
    // Arrange
    when(inventoryService.getInventoryItemById(any(UUID.class)))
        .thenThrow(new RuntimeException("Unexpected error"));

    // Act & Assert
    mockMvc
        .perform(
            get("/api/inventory/{id}", UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Unexpected error"));
  }
}

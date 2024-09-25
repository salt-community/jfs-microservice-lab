package se.saltcode.inventory.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.saltcode.inventory.model.inventory.Inventory;
import se.saltcode.inventory.repository.IInventoryRepository;
import se.saltcode.inventory.repository.IOrderCacheRepository;

class InventoryServiceTest {

  private InventoryService inventoryService;
  private IInventoryRepository mockRepo;
  private IOrderCacheRepository mockCacheRepo;

  @BeforeEach
  void setUp() {
    // Arrange
    mockRepo = Mockito.mock(IInventoryRepository.class);
    mockCacheRepo = Mockito.mock(IOrderCacheRepository.class);
    inventoryService = new InventoryService(mockRepo,mockCacheRepo);
  }

  @Test
  void shouldReturnEmptyList_whenNoInventoryItemsExist() {
    // Arrange
    when(mockRepo.findAll()).thenReturn(List.of());

    // Act
    List<Inventory> result = inventoryService.getAllInventoryItems();

    // Assert
    assertTrue(result.isEmpty(), "Inventory list should be empty initially");
    verify(mockRepo, times(1)).findAll();
  }

  @Test
  void shouldCreateInventoryItemSuccessfully() {
    // Arrange
    Inventory newItem = new Inventory(inventoryDTO);
    newItem.setProduct("Test Product");
    newItem.setQuantity(10);
    newItem.setReservedQuantity(2);
    when(mockRepo.save(any(Inventory.class))).thenReturn(newItem);

    // Act
    Inventory createdItem = inventoryService.createInventoryItem(newItem);

    // Assert
    assertNotNull(createdItem.getId(), "Created item should have a UUID");
    assertEquals("Test Product", createdItem.getProduct());
    assertEquals(10, createdItem.getQuantity());
    assertEquals(2, createdItem.getReservedQuantity());
    verify(mockRepo, times(1)).save(any(Inventory.class));
  }

  @Test
  void shouldThrowException_whenCreateInventoryItemWithNullInput() {
    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> inventoryService.createInventoryItem(null),
        "Should throw an IllegalArgumentException when input is null");
  }

  @Test
  void shouldReturnInventoryItem_whenItemExists() {
    // Arrange
    Inventory newItem = new Inventory(inventoryDTO);
    newItem.setProduct("Test Product");
    newItem.setQuantity(10);
    newItem.setReservedQuantity(2);
    UUID id = UUID.randomUUID();
    newItem.setId(id);
    when(mockRepo.findById(id)).thenReturn(Optional.of(newItem));

    // Act
    Inventory foundItem = inventoryService.getInventoryItemById(id);

    // Assert
    assertNotNull(foundItem, "Inventory item should be returned when it exists");
    assertEquals(id, foundItem.getId());
    verify(mockRepo, times(1)).findById(id);
  }

  @Test
  void shouldReturnNull_whenItemDoesNotExist() {
    // Arrange
    UUID id = UUID.randomUUID();
    when(mockRepo.findById(id)).thenReturn(Optional.empty());

    // Act
    Inventory foundItem = inventoryService.getInventoryItemById(id);

    // Assert
    assertNull(foundItem, "Inventory item should return null when the item is not found");
    verify(mockRepo, times(1)).findById(id);
  }

  @Test
  void shouldUpdateInventoryItem_whenItemExists() {
    // Arrange
    UUID id = UUID.randomUUID();

    Inventory existingItem = new Inventory(inventoryDTO);
    existingItem.setProduct("Existing Product");
    existingItem.setQuantity(10);
    existingItem.setReservedQuantity(1);

    Inventory updatedItemData = new Inventory(inventoryDTO);
    updatedItemData.setProduct("Updated Product");
    updatedItemData.setQuantity(15);
    updatedItemData.setReservedQuantity(3);

    when(mockRepo.findById(id)).thenReturn(Optional.of(existingItem));
    when(mockRepo.save(existingItem)).thenReturn(updatedItemData);

    // Act
    Inventory updatedItem = inventoryService.updateInventoryItem(id, updatedItemData);

    // Assert
    assertNotNull(updatedItem, "Updated item should not be null when the item exists");
    assertEquals("Updated Product", updatedItem.getProduct());
    assertEquals(15, updatedItem.getQuantity());
    verify(mockRepo, times(1)).findById(id);
    verify(mockRepo, times(1)).save(existingItem);
  }

  @Test
  void shouldReturnNull_whenUpdateItemDoesNotExist() {
    // Arrange
    UUID id = UUID.randomUUID();
    Inventory updatedItemData = new Inventory(inventoryDTO);
    updatedItemData.setProduct("Updated Product");
    updatedItemData.setQuantity(15);
    updatedItemData.setReservedQuantity(3);
    when(mockRepo.findById(id)).thenReturn(Optional.empty());

    // Act
    Inventory updatedItem = inventoryService.updateInventoryItem(id, updatedItemData);

    // Assert
    assertNull(updatedItem, "Update should return null if item is not found");
    verify(mockRepo, times(1)).findById(id);
  }

  @Test
  void shouldDeleteInventoryItem_whenItemExists() {
    // Arrange
    UUID id = UUID.randomUUID();
    when(mockRepo.existsById(id)).thenReturn(true);

    // Act
    boolean deleted = inventoryService.deleteInventoryItem(id);

    // Assert
    assertTrue(deleted, "Inventory item should be successfully deleted");
    verify(mockRepo, times(1)).existsById(id);
    verify(mockRepo, times(1)).deleteById(id);
  }

  @Test
  void shouldNotDeleteInventoryItem_whenItemDoesNotExist() {
    // Arrange
    UUID id = UUID.randomUUID();
    when(mockRepo.existsById(id)).thenReturn(false);

    // Act
    boolean deleted = inventoryService.deleteInventoryItem(id);

    // Assert
    assertFalse(deleted, "Inventory item should not be deleted if not found");
    verify(mockRepo, times(1)).existsById(id);
  }

  @Test
  void shouldNotDeleteItemTwice() {
    // Arrange
    UUID id = UUID.randomUUID();
    when(mockRepo.existsById(id)).thenReturn(true).thenReturn(false);

    // Act & Assert
    boolean firstDelete = inventoryService.deleteInventoryItem(id);
    assertTrue(firstDelete, "Inventory item should be deleted the first time");

    boolean secondDelete = inventoryService.deleteInventoryItem(id);
    assertFalse(secondDelete, "Inventory item should not be deleted the second time");
    verify(mockRepo, times(2)).existsById(id);
    verify(mockRepo, times(1)).deleteById(id);
  }
}

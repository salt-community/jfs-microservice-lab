package se.saltcode.inventory.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.stereotype.Service;
import se.saltcode.inventory.model.enums.UpdateResult;
import se.saltcode.inventory.model.cache.OrderCache;
import se.saltcode.inventory.model.inventory.Inventory;
import se.saltcode.inventory.repository.IOrderCacheRepository;
import se.saltcode.inventory.repository.IInventoryRepository;

@Service
public class InventoryService {

  private final IInventoryRepository inventoryDBRepository;
  private final IOrderCacheRepository orderCacheRepository;

  public InventoryService(IInventoryRepository inventoryDBRepository, IOrderCacheRepository orderCacheRepository) {
    this.inventoryDBRepository = inventoryDBRepository;
      this.orderCacheRepository = orderCacheRepository;
  }

  public List<Inventory> getAllInventoryItems() {
    return inventoryDBRepository.findAll();
  }

  public Inventory getInventoryItemById(UUID id) {
    return inventoryDBRepository.findById(id).orElse(null);
  }

  public Inventory createInventoryItem(Inventory inventory) {
    if (inventory == null) {
      throw new IllegalArgumentException("Inventory item cannot be null");
    }
    inventory.setId(UUID.randomUUID()); // Generate a random UUID for the new item
    return inventoryDBRepository.save(inventory);
  }

  public Inventory updateInventoryItem(UUID id, Inventory inventory) {
    return inventoryDBRepository
        .findById(id)
        .map(
            existingItem -> {
              existingItem.setProduct(inventory.getProduct());
              existingItem.setQuantity(inventory.getQuantity());
              existingItem.setReservedQuantity(inventory.getReservedQuantity());
              return inventoryDBRepository.save(existingItem);
            })
        .orElse(null);
  }

  public boolean deleteInventoryItem(UUID id) {
    if (inventoryDBRepository.existsById(id)) {
      inventoryDBRepository.deleteById(id);
      return true;
    }
    return false;
  }

  public UpdateResult updateQuantityOfInventory(UUID id, int quantity, UUID transactionId) {
    if(orderCacheRepository.existsById(transactionId)){
     return UpdateResult.DUPLICATE_MESSAGE;
    }
    orderCacheRepository.save(new OrderCache(transactionId));
    if(!inventoryDBRepository.existsById(id)){
      return UpdateResult.NO_SUCH_INVENTORY;
    }
    Inventory inventory = inventoryDBRepository.findById(id).orElseThrow(NoSuchElementException::new);
    inventory.setQuantity(inventory.getQuantity() - quantity);
    if (inventory.getQuantity() < 0) {
      return UpdateResult.INSUFFICIENT_QUANTITY;
    }
    inventoryDBRepository.save(inventory);
    return UpdateResult.SUCCESS;
  }
}

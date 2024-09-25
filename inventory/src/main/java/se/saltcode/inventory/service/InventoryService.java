package se.saltcode.inventory.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import se.saltcode.inventory.exception.NoSuchInventoryException;
import se.saltcode.inventory.model.cache.OrderCache;
import se.saltcode.inventory.model.enums.UpdateResult;
import se.saltcode.inventory.model.inventory.Inventory;
import se.saltcode.inventory.repository.IInventoryRepository;
import se.saltcode.inventory.repository.IOrderCacheRepository;

@Service
public class InventoryService {

  private final IInventoryRepository inventoryRepository;
  private final IOrderCacheRepository orderCacheRepository;

  public InventoryService(
      IInventoryRepository inventoryRepository, IOrderCacheRepository orderCacheRepository) {
    this.inventoryRepository = inventoryRepository;
    this.orderCacheRepository = orderCacheRepository;
  }

  public List<Inventory> getAllInventoryItems() {
    return inventoryRepository.findAll();
  }

  public Inventory getInventoryItemById(UUID id) {
    return inventoryRepository.findById(id).orElseThrow(NoSuchInventoryException::new);
  }

  public Inventory createInventoryItem(Inventory inventory) {
    return inventoryRepository.save(inventory);
  }

  public Inventory updateInventoryItem(Inventory inventory) {
    if(!inventoryRepository.existsById(inventory.getId())) {
      throw new NoSuchInventoryException();
    }
    return inventoryRepository.save(inventory);
  }

  public boolean deleteInventoryItem(UUID id) {
    if (!inventoryRepository.existsById(id)) {
      throw new NoSuchInventoryException();
    }
    inventoryRepository.deleteById(id);
    return false;
  }

  public UpdateResult updateQuantityOfInventory(UUID inventoryId, int quantity, UUID transactionId) {
    if (orderCacheRepository.existsById(transactionId)) {
      return UpdateResult.DUPLICATE_MESSAGE;
    }
    orderCacheRepository.save(new OrderCache(transactionId));
    if (!inventoryRepository.existsById(inventoryId)) {
      return UpdateResult.NO_SUCH_INVENTORY;
    }
    Inventory inventory =
            inventoryRepository.findById(inventoryId).orElseThrow(NoSuchInventoryException::new);
    inventory.setQuantity(inventory.getQuantity() - quantity);
    if (inventory.getQuantity() < 0) {
      return UpdateResult.INSUFFICIENT_QUANTITY;
    }
    inventoryRepository.save(inventory);
    return UpdateResult.SUCCESS;
  }
}

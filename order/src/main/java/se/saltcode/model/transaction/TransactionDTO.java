package se.saltcode.model.transaction;

import java.util.UUID;
import se.saltcode.model.enums.Event;

public record TransactionDTO(UUID id, Event eventType, UUID orderId, UUID inventoryId, int change) {

  public static TransactionDTO fromTransaction(Transaction transaction) {
    return new TransactionDTO(
        transaction.getId(), transaction.getEventType(), transaction.getOrderId(), transaction.getInventoryId(), transaction.getChange());
  }
}

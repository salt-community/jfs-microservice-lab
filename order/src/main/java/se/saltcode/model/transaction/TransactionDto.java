package se.saltcode.model.transaction;

import java.time.LocalDateTime;
import java.util.UUID;
import se.saltcode.model.enums.Event;

public record TransactionDto(
    UUID id, Event eventType, UUID orderId, UUID inventoryId, int change, LocalDateTime createdAt) {

  public static TransactionDto fromTransaction(Transaction transaction) {
    return new TransactionDto(
        transaction.getId(),
        transaction.getEventType(),
        transaction.getOrderId(),
        transaction.getInventoryId(),
        transaction.getChange(),
        transaction.getCreatedAt());
  }
}

package se.saltcode.model.transaction;

import java.util.Map;
import java.util.UUID;
import se.saltcode.model.enums.Event;

public record TransactionDTO(UUID id, Event eventType, Map<String, String> payload) {

  public static TransactionDTO fromTransaction(Transaction transaction) {
    return new TransactionDTO(
        transaction.getId(), transaction.getEventType(), transaction.getPayload());
  }
}

package se.saltcode.model.transaction;

import se.saltcode.model.enums.Event;
import se.saltcode.model.enums.Status;

import java.util.Map;
import java.util.UUID;

public record TransactionDTO(UUID id, Event eventType, Map<String, String> payload) {

    public static TransactionDTO fromTransaction(Transaction transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getEventType(),
                transaction.getPayload()
        );
    }
}

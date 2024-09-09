package se.saltcode.model.transaction;

import se.saltcode.model.enums.Event;
import se.saltcode.model.enums.Status;

import java.util.UUID;

public record TransactionDTO(UUID eventID, UUID orderId, Event eventType, Status status, String payload) {

    public static TransactionDTO fromTransaction(Transaction transaction) {
        return new TransactionDTO(
                transaction.getEventID(),
                transaction.getOrderId(),
                transaction.getEventType(),
                transaction.getStatus(),
                transaction.getPayload()
        );
    }
}

package se.saltcode.model.transaction;

import se.saltcode.model.enums.Event;
import se.saltcode.model.enums.Status;

import java.util.UUID;

public record AddTransactionDTO(UUID orderId, Event eventType, Status status, String payload) {
}

package se.saltcode.order.model;

import se.saltcode.order.model.Event;
import se.saltcode.order.model.Status;

import java.util.UUID;

public record AddTransactionDTO(UUID orderId, Event eventType, Status status, String payload) {
}

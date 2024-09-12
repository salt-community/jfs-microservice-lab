package se.saltcode.model.transaction;

import se.saltcode.model.enums.Event;
import se.saltcode.model.enums.Status;

import java.util.Map;
import java.util.UUID;

public record AddTransactionDTO( Event eventType,  Map<String, String> payload) {
}

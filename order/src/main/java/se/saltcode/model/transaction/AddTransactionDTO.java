package se.saltcode.model.transaction;

import se.saltcode.model.enums.Event;
import java.util.Map;

public record AddTransactionDTO( Event eventType,  Map<String, String> payload) {
}

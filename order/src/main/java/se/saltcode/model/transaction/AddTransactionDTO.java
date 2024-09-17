package se.saltcode.model.transaction;

import java.util.Map;
import se.saltcode.model.enums.Event;

public record AddTransactionDTO(Event eventType, Map<String, String> payload) {}

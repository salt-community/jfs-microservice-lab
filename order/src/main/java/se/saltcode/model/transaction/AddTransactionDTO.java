package se.saltcode.model.transaction;

import org.springframework.util.MultiValueMap;
import se.saltcode.model.enums.Event;

public record AddTransactionDTO( Event eventType,  MultiValueMap<String, String> payload) {
}

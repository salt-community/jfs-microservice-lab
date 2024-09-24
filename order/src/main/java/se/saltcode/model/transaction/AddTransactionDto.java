package se.saltcode.model.transaction;

import java.util.UUID;
import se.saltcode.model.enums.Event;

public record AddTransactionDto(Event eventType, int change, UUID orderId) {}

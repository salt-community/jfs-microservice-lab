package se.saltcode.model.transaction;

import java.util.UUID;
import se.saltcode.model.enums.Event;

public record AddTransactionDTO( Event eventType, UUID orderId, UUID inventoryId, int change) {}

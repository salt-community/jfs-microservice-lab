package se.saltcode.model.order;

import java.util.UUID;

public record OrderCreationObject(
    UUID customerId, UUID inventoryId, int quantity, double totalCost) {}

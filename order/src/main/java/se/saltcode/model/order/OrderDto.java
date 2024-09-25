package se.saltcode.model.order;

import java.util.UUID;

public record OrderDto(UUID id, UUID inventoryId, int quantity, double totalCost) {}

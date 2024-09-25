package se.saltcode.model.order;

import java.util.UUID;

public record AddOrderDto(UUID inventoryId, int quantity, double totalCost) {}

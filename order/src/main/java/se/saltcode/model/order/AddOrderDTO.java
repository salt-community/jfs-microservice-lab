package se.saltcode.model.order;

import java.util.UUID;

public record AddOrderDTO(
    UUID customerId, UUID inventoryId, int quantity, double totalCost) {}

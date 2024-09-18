package se.saltcode.model.order;

import java.util.UUID;

public record OrderDTO(
    UUID id, UUID customerId, UUID inventoryId, int quantity, double totalCost) {}

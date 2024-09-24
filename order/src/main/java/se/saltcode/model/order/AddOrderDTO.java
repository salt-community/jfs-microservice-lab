package se.saltcode.model.order;

import java.util.UUID;

public record AddOrderDTO(
     UUID inventoryId, int quantity, double totalCost) {}

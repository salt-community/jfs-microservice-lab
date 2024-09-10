package se.saltcode.model.order;

import java.util.UUID;

public record OrderResponseObject(
        UUID id,
        UUID customerId,
        UUID productId,
        int quantity,
        double totalCost) { }

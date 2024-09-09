package se.saltcode.model.order;

import java.util.UUID;

public record OrderUpdateObject(
        UUID id,
        UUID customerId,
        int quantity,
        double totalCost) {}

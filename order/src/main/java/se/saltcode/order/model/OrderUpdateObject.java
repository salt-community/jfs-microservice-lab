package se.saltcode.order.model;

import java.util.UUID;

public record OrderUpdateObject(
        UUID id,
        UUID customerId,
        int quantity,
        double totalCost) {}

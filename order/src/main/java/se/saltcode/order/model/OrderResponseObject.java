package se.saltcode.order.model;

import java.util.UUID;

public record OrderResponseObject(
        UUID id,
        UUID customerId,
        int quantity,
        double totalCost) { }

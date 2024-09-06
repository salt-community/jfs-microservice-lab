package se.saltcode.order.model;

import java.util.UUID;

public record OrderCreationObject(
        UUID customerId,
        int quantity,
        double totalCost) {}

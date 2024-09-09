package se.saltcode.model.order;

import java.util.UUID;

public record OrderCreationObject(
        UUID customerId,
        int quantity,
        double totalCost) {}

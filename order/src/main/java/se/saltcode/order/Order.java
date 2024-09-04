package se.saltcode.order;

import jakarta.persistence.Entity;

import java.util.UUID;

@Entity
public class Order {

    private static UUID orderId;
    private static UUID customerId;
    private static int quantity;
    private static double totalCost;
}
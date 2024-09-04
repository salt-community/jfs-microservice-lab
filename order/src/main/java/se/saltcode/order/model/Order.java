package se.saltcode.order.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table
public class Order {

    private UUID orderId;
    private UUID customerId;
    private int quantity;
    private double totalCost;

    public Order() {
    }

}
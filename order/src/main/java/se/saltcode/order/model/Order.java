package se.saltcode.order.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table
public class Order {

    @UuidGenerator
    @Id
    private UUID id;
    private UUID customerId;
    private int quantity;
    private double totalCost;


    public Order(OrderCreationObject orderCreationObject) {
        this.customerId=orderCreationObject.customerId();
        this.quantity=orderCreationObject.quantity();
        this.totalCost=orderCreationObject.totalCost();
    }

    public OrderResponseObject toResponseObject() {
        return new OrderResponseObject(id, customerId, quantity, totalCost);
    }
}
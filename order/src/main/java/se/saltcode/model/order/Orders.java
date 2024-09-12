package se.saltcode.model.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name="orders")
public class Orders {

    @Id
    @UuidGenerator
    private UUID id;
    @NotEmpty(message = "customerId cant be empty")
    @Column(name="customer_id")
    private UUID customerId;
    @NotEmpty(message = "inventoryId cant be empty")
    @Column(name="inventory_id")
    private UUID inventoryId;
    @Positive(message = "quantity must be positive")
    @Column(name = "quantity")
    private int quantity;
    @Positive(message = "totalCost must be positive")
    @Column(name= "total_cost")
    private double totalCost;

    public Orders() {}
    public Orders(OrderCreationObject orderCreationObject) {
        this.customerId=orderCreationObject.customerId();
        this.inventoryId=orderCreationObject.inventoryId();
        this.quantity=orderCreationObject.quantity();
        this.totalCost=orderCreationObject.totalCost();
    }

    public Orders(OrderUpdateObject orderUpdateObject) {
        this.customerId=orderUpdateObject.customerId();
        this.inventoryId=orderUpdateObject.inventoryId();
        this.quantity=orderUpdateObject.quantity();
        this.totalCost=orderUpdateObject.totalCost();
    }

    public OrderResponseObject toResponseObject() {
        return new OrderResponseObject(id, customerId, inventoryId ,quantity, totalCost);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(UUID inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
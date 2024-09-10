package se.saltcode.model.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table
public class Orders {

    @UuidGenerator
    @Id
    private UUID id;
    @Column(name="customer_id")
    private UUID customerId;
    @Column(name="product_id")
    private UUID productId;
    @Column(name = "quantity")
    private int quantity;
    @Column(name= "total_cost")
    private double totalCost;

    public Orders() {}
    public Orders(OrderCreationObject orderCreationObject) {
        this.customerId=orderCreationObject.customerId();
        this.productId=orderCreationObject.productId();
        this.quantity=orderCreationObject.quantity();
        this.totalCost=orderCreationObject.totalCost();
    }

    public Orders(OrderUpdateObject orderUpdateObject) {
        this.customerId=orderUpdateObject.customerId();
        this.productId=orderUpdateObject.productId();
        this.quantity=orderUpdateObject.quantity();
        this.totalCost=orderUpdateObject.totalCost();
    }

    public Orders(UUID id, UUID customerId, UUID productId,int quantity, double totalCost) {
        this.id = id;
        this.customerId = customerId;
        this.productId =  productId;
        this.quantity = quantity;
        this.totalCost = totalCost;
    }


    public OrderResponseObject toResponseObject() {
        return new OrderResponseObject(id, customerId, productId ,quantity, totalCost);
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

    public UUID getProductId() {
        return productId;
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
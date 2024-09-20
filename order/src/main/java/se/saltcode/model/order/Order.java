package se.saltcode.model.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "orders")
public class Order{

  @Id @UuidGenerator private UUID id;

  @NotEmpty(message = "customerId cant be empty")
  @Column(name = "customer_id")
  private UUID customerId;

  @NotEmpty(message = "inventoryId cant be empty")
  @Column(name = "inventory_id")
  private UUID inventoryId;

  @Positive(message = "quantity must be positive")
  @Column(name = "quantity")
  private int quantity;

  @Positive(message = "totalCost must be positive")
  @Column(name = "total_cost")
  private double totalCost;

  public Order() {}

  public Order(AddOrderDTO addOrderDTO) {
    this.customerId = addOrderDTO.customerId();
    this.inventoryId = addOrderDTO.inventoryId();
    this.quantity = addOrderDTO.quantity();
    this.totalCost = addOrderDTO.totalCost();
  }

  public Order(UpdateOrderDTO updateOrderDTO) {
    this.customerId = updateOrderDTO.customerId();
    this.inventoryId = updateOrderDTO.inventoryId();
    this.quantity = updateOrderDTO.quantity();
    this.totalCost = updateOrderDTO.totalCost();
  }

  public OrderDTO toResponseObject() {
    return new OrderDTO(id, customerId, inventoryId, quantity, totalCost);
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

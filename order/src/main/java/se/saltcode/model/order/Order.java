package se.saltcode.model.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "user_order")
public class Order {

  @Id @UuidGenerator private UUID id;

  @NotEmpty(message = "inventoryId cant be empty")
  @Column(name = "inventory_id")
  private UUID inventoryId;

  @Positive(message = "quantity must be positive")
  @Column(name = "quantity")
  private int quantity;

  @PositiveOrZero(message = "totalCost must be positive or zero")
  @Column(name = "total_cost")
  private double totalCost;

  public Order() {}

  public Order(AddOrderDto addOrderDTO) {
    this.inventoryId = addOrderDTO.inventoryId();
    this.quantity = addOrderDTO.quantity();
    this.totalCost = addOrderDTO.totalCost();
  }

  public Order(OrderDto orderDTO) {
    this.id = orderDTO.id();
    this.inventoryId = orderDTO.inventoryId();
    this.quantity = orderDTO.quantity();
    this.totalCost = orderDTO.totalCost();
  }

  public OrderDto toDto() {
    return new OrderDto(id, inventoryId, quantity, totalCost);
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
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

package se.saltcode.model.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;
import se.saltcode.model.enums.Event;
import se.saltcode.model.transaction.Transaction;

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

  @OneToMany(mappedBy = "order",cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Transaction> transactions;

  public Order() {}

  public Order(AddOrderDto addOrderDTO) {
    this.inventoryId = addOrderDTO.inventoryId();
    this.quantity = addOrderDTO.quantity();
    this.totalCost = addOrderDTO.totalCost();
    this.transactions = List.of(new Transaction(Event.PURCHASE,quantity,this));
  }

  public Order(OrderDto orderDTO) {
    this.id = orderDTO.id();
    this.inventoryId = orderDTO.inventoryId();
    this.quantity = orderDTO.quantity();
    this.totalCost = orderDTO.totalCost();
  }

  public void update(Order order) {
    this.transactions.add(new Transaction(Event.CHANGE,order.getQuantity() - quantity,this)) ;
    this.quantity = order.getQuantity();
    this.totalCost = order.getTotalCost();
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

package se.saltcode.model.transaction;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import se.saltcode.model.enums.Event;
import se.saltcode.model.order.Order;

@Entity
@Table
public class Transaction implements Comparable<Transaction>{

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private Event eventType;

  private UUID orderId;

  private UUID inventoryId;

  private int change;

  @CreationTimestamp
  private LocalDateTime createdAt;

  public Transaction() {}

  public Transaction(Event eventType, UUID orderId, UUID inventoryId, int change) {
    this.eventType = eventType;
    this.orderId=orderId;
    this.inventoryId=inventoryId;
    this.change=change;
  }

  @Override
  public int compareTo(Transaction o) {
    return createdAt.compareTo(o.getCreatedAt());
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Event getEventType() {
    return eventType;
  }

  public void setEventType(Event eventType) {
    this.eventType = eventType;
  }

  public UUID getOrderId() {
    return orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public UUID getInventoryId() {
    return inventoryId;
  }

  public void setInventoryId(UUID inventoryId) {
    this.inventoryId = inventoryId;
  }

  public int getChange() {
    return change;
  }

  public void setChange(int change) {
    this.change = change;
  }
  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}

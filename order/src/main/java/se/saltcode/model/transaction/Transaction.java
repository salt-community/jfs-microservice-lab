package se.saltcode.model.transaction;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;
import se.saltcode.model.enums.Event;

@Entity
@Table
public class Transaction implements Comparable<Transaction> {

  @Id @UuidGenerator private UUID id;

  @NotNull(message = "eventType cant be null")
  @Column(name = "event_type")
  private Event eventType;

  @NotEmpty(message = "orderId cant be empty")
  @Column(name = "order_id")
  private UUID orderId;

  @NotEmpty(message = "inventoryId cant be empty")
  @Column(name = "inventory_id")
  private UUID inventoryId;

  @Column(name = "change")
  private int change;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  public Transaction() {}

  public Transaction(Event eventType, UUID orderId, UUID inventoryId, int change) {
    this.eventType = eventType;
    this.orderId = orderId;
    this.inventoryId = inventoryId;
    this.change = change;
    this.createdAt = LocalDateTime.now();
  }

  public Transaction(AddTransactionDto addTransactionDto) {
    this.eventType = addTransactionDto.eventType();
    this.orderId = addTransactionDto.orderId();
    this.inventoryId = addTransactionDto.inventoryId();
    this.change = addTransactionDto.change();
    this.createdAt = LocalDateTime.now();
  }

  public Transaction(TransactionDto transactionDto) {
    this.id = transactionDto.id();
    this.eventType = transactionDto.eventType();
    this.orderId = transactionDto.orderId();
    this.inventoryId = transactionDto.inventoryId();
    this.change = transactionDto.change();
    this.createdAt = transactionDto.createdAt();
  }

  public TransactionDto toDto() {
    return new TransactionDto(id, eventType, orderId, inventoryId, change, createdAt);
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

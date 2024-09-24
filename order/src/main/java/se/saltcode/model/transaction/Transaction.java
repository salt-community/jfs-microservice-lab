package se.saltcode.model.transaction;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import se.saltcode.model.enums.Event;

@Entity
@Table
public class Transaction implements Comparable<Transaction> {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private Event eventType;

  private UUID orderId;

  private UUID inventoryId;

  private int change;

  private LocalDateTime createdAt;

  public Transaction() {
    this.createdAt = LocalDateTime.now();
  }

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

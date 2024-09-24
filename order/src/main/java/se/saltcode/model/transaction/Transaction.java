package se.saltcode.model.transaction;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;
import se.saltcode.model.enums.Event;
import se.saltcode.model.order.Order;

@Entity
@Table
public class Transaction implements Comparable<Transaction> {

  @Id @UuidGenerator private UUID id;

  @NotNull(message = "eventType cant be null")
  @Column(name = "event_type")
  private Event eventType;

  @Column(name = "change")
  private int change;

  @Column(name = "created_at")
  private LocalDateTime createdAt;


  @ManyToOne(optional = false)
  @JoinColumn(name = "user_order")
  private Order order;

  public Transaction() {}

  public Transaction(Event eventType, int change, Order order) {
    this.eventType = eventType;
    this.change = change;
    this.createdAt = LocalDateTime.now();
    this.order = order;
  }

  public TransactionDto toDto() {
    return new TransactionDto(id, eventType,change, createdAt, order.getId());
  }

  @Override
  public int compareTo(Transaction o) {
    return o.getCreatedAt().compareTo(createdAt);
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

  public Order getOrder() {
    return this.order;
  }

  public void setOrder(Order order) {
     this.order=order;
  }
}

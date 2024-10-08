package se.saltcode.inventory.model.cache;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table
public class OrderCache {

  @Id private UUID id;

  @Column(name = "created_at")
  private ZonedDateTime createdAt;

  public OrderCache(UUID id) {
    this.id = id;
    this.createdAt = ZonedDateTime.now();
  }

  public OrderCache() {}

  public OrderCache(AddOrderCacheDto addOrderCacheDto) {
    this.id = addOrderCacheDto.id();
    this.createdAt = ZonedDateTime.now();
  }

  public OrderCacheDto toDto() {
    return new OrderCacheDto(id, createdAt);
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getId() {
    return id;
  }

  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(ZonedDateTime createdAt) {
    this.createdAt = createdAt;
  }
}

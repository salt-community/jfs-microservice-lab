package se.saltcode.inventory.model.cache;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
public class OrderCache {

    @Id
    private UUID id;
    @Column(unique=true)
    private LocalDateTime createdAt;

    public OrderCache(UUID id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
    }

    public OrderCache() {
    }
    public OrderCache(CreateOrderCacheDto createOrderCacheDto) {
        this.id = createOrderCacheDto.id();
        this.createdAt = LocalDateTime.now();
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

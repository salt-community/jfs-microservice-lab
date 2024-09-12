package se.saltcode.model.message;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private int quantityChange;
    private UUID inventoryId;

    public Message() {

    }

    public Message(UUID inventoryId, int quantityChange) {
        this.inventoryId = inventoryId;
        this.quantityChange = quantityChange;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(int quantityChange) {
        this.quantityChange = quantityChange;
    }

    public UUID getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(UUID inventoryId) {
        this.inventoryId = inventoryId;
    }
}


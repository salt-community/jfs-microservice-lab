package se.saltcode.model.transaction;

import jakarta.persistence.*;
import se.saltcode.model.enums.Event;
import se.saltcode.model.enums.Status;

import java.util.UUID;

@Entity
@Table
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID eventID;

    private UUID orderId;
    private Event eventType;
    private Status status;
    private String payload;

    public Transaction(UUID eventID, UUID orderId, Event eventType, Status status, String payload) {
        this.eventID = eventID;
        this.orderId = orderId;
        this.eventType = eventType;
        this.status = status;
        this.payload = payload;
    }

    public Transaction() {

    }

    public UUID getEventID() {
        return eventID;
    }

    public void setEventID(UUID eventID) {
        this.eventID = eventID;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public Event getEventType() {
        return eventType;
    }

    public void setEventType(Event eventType) {
        this.eventType = eventType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}

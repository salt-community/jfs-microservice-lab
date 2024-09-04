package se.saltcode.order.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table
public class Transaction {

    private UUID eventID;
    private UUID orderId;
    private Event eventType;
    private Status status;
    private String payload;
    
}

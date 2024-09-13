package se.saltcode.model.transaction;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.util.MultiValueMap;
import se.saltcode.model.enums.Event;
import java.util.UUID;

@Entity
@Table
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Event eventType;
    @JdbcTypeCode(SqlTypes.JSON)
    private MultiValueMap<String, String> payload;

    public Transaction() {}

    public Transaction(Event eventType,MultiValueMap<String, String>  payload) {
        this.eventType = eventType;
        this.payload = payload;
    }

    public UUID getId() {
        return id;
    }

    public Event getEventType() {
        return eventType;
    }

    public void setEventType(Event eventType) {
        this.eventType = eventType;
    }

    public MultiValueMap<String, String> getPayload() {
        return payload;
    }

    public void setPayload(MultiValueMap<String, String> payload) {
        this.payload = payload;
    }



}

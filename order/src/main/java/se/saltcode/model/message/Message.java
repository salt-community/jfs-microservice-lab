package se.saltcode.model.message;


import jakarta.persistence.*;
import se.saltcode.model.enums.MessageType;

import java.util.UUID;

@Entity
@Table
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String uri;
    private String payload;
    private MessageType messageType;

    public Message() {

    }

    public Message( String uri, String payload,MessageType messageType) {
        this.uri = uri;
        this.payload = payload;
        this.messageType = messageType;
    }

    public UUID getId() {
        return this.id;
    }
    public String getUri() {
        return this.uri;
    }
    public MessageType getMessageType() {
        return this.messageType;
    }
}


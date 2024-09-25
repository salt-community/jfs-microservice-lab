package se.saltcode.model.transaction;

import java.time.LocalDateTime;
import java.util.UUID;
import se.saltcode.model.enums.Event;

public record TransactionDto(
    UUID id,
    Event eventType,
    int change,
    LocalDateTime createdAt,
    UUID orderId) {}

package se.saltcode.model.transaction;

import org.springframework.util.MultiValueMap;
import se.saltcode.model.enums.Event;
import java.util.UUID;

public record TransactionDTO(UUID id, Event eventType, MultiValueMap<String, String> payload) {

    public static TransactionDTO fromTransaction(Transaction transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getEventType(),
                transaction.getPayload()
        );
    }
}

package se.saltcode.components;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import se.saltcode.model.transaction.Transaction;
import se.saltcode.repository.TransactionDbRepository;

import java.util.Collections;
import java.util.Map;

@Component
public class MessageRelay {

    private final WebClient webClient;
    private final TransactionDbRepository transactionRepository;

    public MessageRelay(TransactionDbRepository transactionRepository) {
        this.webClient = WebClient.create("http://localhost:5000/api/inventory/");
        this.transactionRepository = transactionRepository;
    }

    public void sendUnfinishedMessages() {
        transactionRepository.findAll().forEach(this::sendMessage);

    }

    private void sendMessage(Transaction transaction) {

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        Map<String, String> payload = transaction.getPayload();
        payload.keySet().forEach(key ->
                multiValueMap.put(key, Collections.singletonList(payload.get(key)))
        );

                webClient.put()
                        .uri(uriBuilder -> uriBuilder
                                .path("update/quantity")
                                .queryParams(multiValueMap)
                                .build())
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .onStatus(HttpStatusCode::is2xxSuccessful, clientResponse -> {
                            transactionRepository.deleteById(transaction.getId());
                            return Mono.empty();
                        })
                        .bodyToMono(Void.class)
                        .onErrorResume(Exception.class, ex -> Mono.error(new RuntimeException("Service B is unavailable")))
                        .subscribe();
    }
}

package se.saltcode.components;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;
import se.saltcode.model.transaction.Transaction;
import se.saltcode.repository.TransactionDbRepository;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

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
        HttpStatusCode response =  Objects.requireNonNull(
                webClient.put()
                        .uri(uriBuilder -> uriBuilder
                                .path("update/quantity")
                                .queryParams(multiValueMap)
                                .build())
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .toBodilessEntity()
                        .block()).getStatusCode();

        if(response.is2xxSuccessful()){
            transactionRepository.deleteById(transaction.getId());
        }

    }
}

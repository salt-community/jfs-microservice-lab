package se.saltcode.components;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;
import se.saltcode.model.transaction.Transaction;
import se.saltcode.repository.TransactionDbRepository;
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


        HttpStatusCode response =  Objects.requireNonNull(
                webClient.put()
                        .uri(uriBuilder -> uriBuilder
                                .path("update/quantity")
                                .queryParams(transaction.getPayload())
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

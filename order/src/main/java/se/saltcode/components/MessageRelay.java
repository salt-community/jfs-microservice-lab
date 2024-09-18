package se.saltcode.components;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import se.saltcode.model.transaction.Transaction;
import se.saltcode.repository.IOrderRepository;
import se.saltcode.repository.TransactionDbRepository;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Component
public class MessageRelay {

  private final WebClient webClient;
  private final TransactionDbRepository transactionRepository;
  private final IOrderRepository orderRepository;

  public MessageRelay(
      TransactionDbRepository transactionRepository, IOrderRepository orderRepository, WebClient webClient) {
    this.webClient = webClient;
    this.transactionRepository = transactionRepository;
    this.orderRepository = orderRepository;
  }

  public void sendUnfinishedMessages() {
    transactionRepository.findAll().forEach(this::sendMessage);
  }

  private void sendMessage(Transaction transaction) {
    webClient
        .put()
        .uri(uriBuilder -> uriBuilder.path("update/quantity")
                .queryParam("id", transaction.getInventoryId())
                .queryParam("change", transaction.getChange())
                .build())
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .onStatus(
            HttpStatusCode::is2xxSuccessful,
            clientResponse -> {
              transactionRepository.deleteById(transaction.getId());
              return Mono.empty();
            })
        .onStatus(
            HttpStatusCode::is4xxClientError,
            clientResponse -> {
              transactionRepository.deleteById(transaction.getId());
              orderRepository.deleteById(transaction.getOrderId());
              return Mono.empty();
            })
        .bodyToMono(Void.class)
            .doOnError(Throwable::printStackTrace)
            .onErrorResume(
            Exception.class, ex -> Mono.error(new RuntimeException("Service B is unavailable")))
        .subscribe();





  }
}

package se.saltcode.components;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import se.saltcode.exception.NoSuchOrderException;
import se.saltcode.model.enums.Event;
import se.saltcode.model.order.Order;
import se.saltcode.model.transaction.Transaction;
import se.saltcode.repository.IOrderRepository;
import se.saltcode.repository.ITransactionRepository;

@Component
public class MessageRelay {

  private final WebClient webClient;
  private final UriComponentsBuilder uriBuilder;
  private final ITransactionRepository transactionRepository;
  private final IOrderRepository orderRepository;

  public MessageRelay(
      ITransactionRepository transactionRepository,
      IOrderRepository orderRepository,
      WebClient webClient,
      UriComponentsBuilder uriBuilder) {
    this.webClient = webClient;
    this.transactionRepository = transactionRepository;
    this.orderRepository = orderRepository;
    this.uriBuilder = uriBuilder;
  }

  @Scheduled(fixedRate = 60000)
  public void sendUnfinishedMessages() {

    List<Transaction> transactions = transactionRepository.findAll();
    Collections.sort(transactions);
    transactions.forEach(this::sendMessage);
  }

  private String buildUpdateQuantityUri(Transaction transaction) {
    return uriBuilder
        .queryParam("id", transaction.getInventoryId())
        .queryParam("change", transaction.getChange())
        .queryParam("transactionId", transaction.getId())
        .toUriString();
  }

  private void sendMessage(Transaction transaction) {
    try {
      HttpStatusCode response =
          Objects.requireNonNull(
                  webClient
                      .put()
                      .uri(buildUpdateQuantityUri(transaction))
                      .accept(MediaType.APPLICATION_JSON)
                      .retrieve()
                      .toBodilessEntity()
                      .block())
              .getStatusCode();
      if (response.is2xxSuccessful()) {
        transactionRepository.deleteById(transaction.getId());
      }
      if (response.is4xxClientError()) {
        if (transaction.getEventType().equals(Event.PURCHASE)) {
          transactionRepository.deleteById(transaction.getId());
          orderRepository.deleteById(transaction.getOrderId());
        }
        if (transaction.getEventType().equals(Event.CHANGE)) {
          Order order =
              orderRepository
                  .findById(transaction.getOrderId())
                  .orElseThrow(NoSuchOrderException::new);
          order.setQuantity(order.getQuantity() - transaction.getChange());
          orderRepository.save(order);
          transactionRepository.deleteById(transaction.getId());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

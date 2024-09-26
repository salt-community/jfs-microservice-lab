package se.saltcode.components;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import se.saltcode.model.enums.UpdateResult;
import se.saltcode.model.order.Order;
import se.saltcode.model.transaction.Transaction;
import se.saltcode.repository.IOrderRepository;
import se.saltcode.repository.ITransactionRepository;

@Component
public class MessageRelay {

  private final WebClient webClient;
  private final UriComponentsBuilder uriComponentsBuilder;
  private final ITransactionRepository transactionRepository;
  private final IOrderRepository orderRepository;

  public MessageRelay(
      ITransactionRepository transactionRepository,
      IOrderRepository orderRepository,
      WebClient webClient,
      @Value("${inventory.endpoints.update-inventory-quantity}") String apiPath) {
    this.webClient = webClient;
    this.transactionRepository = transactionRepository;
    this.orderRepository = orderRepository;
    this.uriComponentsBuilder = UriComponentsBuilder.fromPath(apiPath);
  }

  @Scheduled(fixedRate = 60000)
  public void sendUnfinishedMessages() {
    List<Transaction> transactions = transactionRepository.findAll();
    Collections.sort(transactions);
    for (Transaction transaction : transactions) {
      if (!sendMessage(transaction)) {break;}
    }
  }

  private Boolean sendMessage(Transaction transaction) {
    return webClient
        .put()
        .uri(transactionUriBuilder(transaction))
        .retrieve()
        .bodyToMono(UpdateResult.class)
        .map(response -> {
              handleUpdateResult(transaction, response);
              return true;
            })
        .onErrorReturn(false)
        .block();
  }

  private String transactionUriBuilder(Transaction transaction){
    return uriComponentsBuilder
            .queryParam("inventoryId", transaction.getOrder().getInventoryId())
            .queryParam("change", transaction.getChange())
            .queryParam("transactionId", transaction.getId())
            .toUriString();
  }

  private void handleUpdateResult(Transaction transaction, UpdateResult updateResult) {
    switch (updateResult) {
      case DUPLICATE_MESSAGE, SUCCESS -> transactionRepository.deleteById(transaction.getId());
      case NO_SUCH_INVENTORY ->  orderRepository.deleteById(transaction.getOrder().getId());
      case INSUFFICIENT_QUANTITY -> handleInsufficientQuantity(transaction);
    };
  }

  private void handleInsufficientQuantity(Transaction transaction){
    switch (transaction.getEventType()) {
      case PURCHASE -> orderRepository.deleteById(transaction.getOrder().getId());
      case CHANGE -> {
        Order order = transaction.getOrder();
        order.setQuantity(order.getQuantity() - transaction.getChange());
        orderRepository.save(order);
        transactionRepository.deleteById(transaction.getId());}
    };
  }
}
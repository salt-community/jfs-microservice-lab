package se.saltcode.components;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import se.saltcode.exception.NoSuchOrderException;
import se.saltcode.model.enums.Event;
import se.saltcode.model.enums.UpdateResult;
import se.saltcode.model.order.Order;
import se.saltcode.model.transaction.Transaction;
import se.saltcode.repository.IOrderRepository;
import se.saltcode.repository.ITransactionRepository;

@Component
public class MessageRelay {

  private final WebClient webClient;
  private final String apiPath;
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
    this.apiPath = apiPath;
  }

  @Scheduled(fixedRate = 60000)
  public void sendUnfinishedMessages() {
    List<Transaction> transactions = transactionRepository.findAll();
    Collections.sort(transactions);
    for (Transaction transaction : transactions) {
      if (!sendMessage(transaction)) {
        break;
      }
    }
  }

  private Boolean sendMessage(Transaction transaction) {
    return webClient
        .put()
        .uri(
            UriComponentsBuilder.fromPath(apiPath)
                .queryParam("id", transaction.getInventoryId())
                .queryParam("change", transaction.getChange())
                .queryParam("transactionId", transaction.getId())
                .toUriString())
        .retrieve()
        .bodyToMono(UpdateResult.class)
        .map(
            response -> {
              handleUpdateResult(transaction, response);
              return true;
            })
        .onErrorReturn(false)
        .block();
  }

  private void handleUpdateResult(Transaction transaction, UpdateResult updateResult) {
    transactionRepository.deleteById(transaction.getId());
    switch (updateResult) {
      case NO_SUCH_INVENTORY -> orderRepository.deleteById(transaction.getOrderId());
      case INSUFFICIENT_QUANTITY -> {
        if (transaction.getEventType().equals(Event.PURCHASE)) {
          orderRepository.deleteById(transaction.getOrderId());
        }
        if (transaction.getEventType().equals(Event.CHANGE)) {
          Order order =
              orderRepository
                  .findById(transaction.getOrderId())
                  .orElseThrow(NoSuchOrderException::new);
          order.setQuantity(order.getQuantity() - transaction.getChange());
          orderRepository.save(order);
        }
      }
    }
    ;
  }
}

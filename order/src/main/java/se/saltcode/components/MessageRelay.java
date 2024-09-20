package se.saltcode.components;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.springframework.web.reactive.function.client.WebClient;
import se.saltcode.exception.NoSuchOrderException;
import se.saltcode.model.enums.Event;
import se.saltcode.model.order.Order;
import se.saltcode.model.transaction.Transaction;
import se.saltcode.repository.IOrderRepository;
import se.saltcode.repository.ITransactionRepository;

import java.util.Objects;

@Component
public class MessageRelay {

  private final WebClient webClient;
  private final ITransactionRepository transactionRepository;
  private final IOrderRepository orderRepository;

  public MessageRelay(
          ITransactionRepository transactionRepository, IOrderRepository orderRepository, WebClient webClient) {
    this.webClient = webClient;
    this.transactionRepository = transactionRepository;
    this.orderRepository = orderRepository;
  }

  @Scheduled(fixedRate = 60000)
  public void sendUnfinishedMessages() {
    transactionRepository.findAll().forEach(this::sendMessage);
  }

  private void sendMessage(Transaction transaction) {
    try {
      HttpStatusCode response =  Objects.requireNonNull(
              webClient.put()
                      .uri(uriBuilder -> uriBuilder.path("update/quantity")
                              .queryParam("id", transaction.getInventoryId())
                              .queryParam("change", transaction.getChange())
                              .build())
                      .accept(MediaType.APPLICATION_JSON)
                      .retrieve()
                      .toBodilessEntity()
                      .block()).getStatusCode();
      if(response.is2xxSuccessful()){
        transactionRepository.deleteById(transaction.getId());
      }
      if(response.is4xxClientError()){
        if (transaction.getEventType().equals(Event.PURCHASE)){
          transactionRepository.deleteById(transaction.getId());
          orderRepository.deleteById(transaction.getOrderId());
        }
        if(transaction.getEventType().equals(Event.CHANGE)){
          Order order = orderRepository.findById(transaction.getOrderId())
                  .orElseThrow(NoSuchOrderException::new);
          order.setQuantity(order.getQuantity() - transaction.getChange());
          orderRepository.save(order);
          transactionRepository.deleteById(transaction.getId());
       }
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
}

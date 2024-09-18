package se.saltcode.components;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import org.springframework.web.reactive.function.client.WebClient;
import se.saltcode.model.transaction.Transaction;
import se.saltcode.repository.IOrderRepository;
import se.saltcode.repository.TransactionDbRepository;

import java.util.Objects;

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
        transactionRepository.deleteById(transaction.getId());
        orderRepository.deleteById(transaction.getOrderId());

      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
}

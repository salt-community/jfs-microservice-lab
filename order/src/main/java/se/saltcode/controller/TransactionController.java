package se.saltcode.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.saltcode.model.transaction.AddTransactionDto;
import se.saltcode.model.transaction.Transaction;
import se.saltcode.model.transaction.TransactionDto;
import se.saltcode.service.OrderService;
import se.saltcode.service.TransactionService;

@RestController
@CrossOrigin
@RequestMapping("${api.base-path}${api.controllers.transactions}")
public class TransactionController {

  private final TransactionService service;
  private final OrderService orderService;
  public String apiUri;

  public TransactionController(
      TransactionService service,
      @Value("${this.base-uri}${api.base-path}${api.controllers.transactions}") String apiUri,
      OrderService orderService) {
    this.service = service;
    this.apiUri = apiUri;
    this.orderService = orderService;
  }

  @GetMapping
  public ResponseEntity<List<TransactionDto>> getAllTransactions() {
    return ResponseEntity.ok(
        service.getAllTransactions().stream().map(Transaction::toDto).toList());
  }

  @GetMapping("/{eventID}")
  public ResponseEntity<TransactionDto> getTransactionById(@PathVariable UUID id) {
    return ResponseEntity.ok(service.getTransactionById(id).toDto());
  }

  @PostMapping
  public ResponseEntity<TransactionDto> createTransaction(
      @RequestBody AddTransactionDto addTransactionDto) {
    TransactionDto transactionDto =
        service.createTransaction(new Transaction(addTransactionDto)).toDto();
    return ResponseEntity.created(URI.create(apiUri + "/" + transactionDto.id()))
        .body(transactionDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTransaction(@PathVariable UUID id) {
    service.deleteTransaction(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{eventID}")
  public ResponseEntity<TransactionDto> updateTransaction(
      @RequestBody TransactionDto transactionDto) {
    return ResponseEntity.ok(service.updateTransaction(new Transaction(transactionDto)).toDto());
  }
}

package se.saltcode.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.saltcode.model.transaction.Transaction;
import se.saltcode.model.transaction.TransactionDto;
import se.saltcode.service.TransactionService;

@RestController
@CrossOrigin
@RequestMapping("${api.base-path}${api.controllers.transactions}")
public class TransactionController {

  private final TransactionService service;

  public TransactionController(
      TransactionService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<List<TransactionDto>> getAllTransactions() {
    return ResponseEntity.ok(
        service.getAllTransactions().stream().map(Transaction::toDto).toList());
  }

  @GetMapping("/{id}")
  public ResponseEntity<TransactionDto> getTransactionById(@PathVariable UUID id) {
    return ResponseEntity.ok(service.getTransactionById(id).toDto());
  }
}

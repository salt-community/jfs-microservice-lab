package se.saltcode.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.saltcode.model.transaction.AddTransactionDTO;
import se.saltcode.model.transaction.Transaction;
import se.saltcode.model.transaction.TransactionDTO;
import se.saltcode.service.TransactionService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("${api.base-path}${api.controllers.transactions}")
public class TransactionController {

    private final TransactionService service;

    @Value("${api.base-path}${api.controllers.transactions}/")
    public String API_CONTEXT_ROOT;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        List<Transaction> transactions = service.getAllTransactions();
        return ResponseEntity.ok(transactions.stream().map(TransactionDTO::fromTransaction).toList());
    }

    @GetMapping("/{eventID}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable UUID eventID) {
        Transaction transaction = service.getTransactionById(eventID);
        return ResponseEntity.ok(TransactionDTO.fromTransaction(transaction));
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody AddTransactionDTO transactionDto) {
        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                transactionDto.orderId(),
                transactionDto.eventType(),
                transactionDto.status(),
                transactionDto.payload()
        );

        Transaction createdTransaction = service.createTransaction(transaction);
        TransactionDTO dto = TransactionDTO.fromTransaction(createdTransaction);
        return ResponseEntity.created(URI.create(API_CONTEXT_ROOT + createdTransaction.getEventID())).body(dto);
    }

    @PutMapping("/{eventID}")
    public ResponseEntity<TransactionDTO> updateTransaction(
            @PathVariable UUID eventID, @RequestBody AddTransactionDTO transactionDto) {

        Transaction updatedTransaction = service.updateTransaction(
                eventID,
                transactionDto.orderId(),
                transactionDto.eventType(),
                transactionDto.status(),
                transactionDto.payload()
        );

        return ResponseEntity.ok(TransactionDTO.fromTransaction(updatedTransaction));
    }

    @DeleteMapping("/{eventID}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID eventID) {
        service.deleteTransaction(eventID);
        return ResponseEntity.noContent().build();
    }
}

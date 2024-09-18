package se.saltcode.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;
import se.saltcode.model.enums.Event;
import se.saltcode.model.transaction.Transaction;
import se.saltcode.repository.TransactionRepository;

@Service
public class TransactionService {

  private final TransactionRepository repository;

  public TransactionService(TransactionRepository repository) {
    this.repository = repository;
  }

  public List<Transaction> getAllTransactions() {
    return repository.findAll();
  }

  public Transaction getTransactionById(UUID eventID) {
    return repository
        .findById(eventID)
        .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
  }

  public Transaction createTransaction(Transaction transaction) {
    return repository.save(transaction);
  }

  public Transaction updateTransaction(UUID eventID,   Event eventType, UUID orderId, UUID inventoryId, int change) {
    Transaction transaction = getTransactionById(eventID);
    transaction.setEventType(eventType);
    transaction.setOrderId(orderId);
    transaction.setInventoryId(inventoryId);
    transaction.setChange(change);
    return repository.save(transaction);
  }

  public void deleteTransaction(UUID eventID) {
    repository.deleteById(eventID);
  }
}

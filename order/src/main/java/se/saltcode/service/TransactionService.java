package se.saltcode.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import se.saltcode.model.enums.Event;
import se.saltcode.model.transaction.Transaction;
import se.saltcode.repository.TransactionDbRepository;

@Service
public class TransactionService {

  private final TransactionDbRepository transactionService;

  public TransactionService(TransactionDbRepository transactionService) {
    this.transactionService = transactionService;
  }

  public List<Transaction> getAllTransactions() {
    return transactionService.findAll();
  }

  public Transaction getTransactionById(UUID eventID) {
    return transactionService
        .findById(eventID)
        .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
  }

  public Transaction createTransaction(Transaction transaction) {
    return transactionService.save(transaction);
  }

  public Transaction updateTransaction(UUID eventID,   Event eventType, UUID orderId, UUID inventoryId, int change) {
    Transaction transaction = getTransactionById(eventID);
    transaction.setEventType(eventType);
    transaction.setOrderId(orderId);
    transaction.setInventoryId(inventoryId);
    transaction.setChange(change);
    return transactionService.save(transaction);
  }

  public void deleteTransaction(UUID eventID) {
    transactionService.deleteById(eventID);
  }
}

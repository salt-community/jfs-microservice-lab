package se.saltcode.service;

import org.springframework.stereotype.Service;
import se.saltcode.model.enums.Event;
import se.saltcode.model.enums.Status;
import se.saltcode.model.transaction.Transaction;
import se.saltcode.repository.TransactionRepository;

import java.util.List;
import java.util.UUID;

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
        return repository.findById(eventID)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
    }

    public Transaction createTransaction(Transaction transaction) {
        return repository.save(transaction);
    }

    public Transaction updateTransaction(UUID eventID, UUID orderId, Event eventType, Status status, String payload) {
        Transaction transaction = getTransactionById(eventID);
        transaction.setOrderId(orderId);
        transaction.setEventType(eventType);
        transaction.setStatus(status);
        transaction.setPayload(payload);
        return repository.save(transaction);
    }

    public void deleteTransaction(UUID eventID) {
        repository.deleteById(eventID);
    }
}

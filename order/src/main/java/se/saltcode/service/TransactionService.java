package se.saltcode.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import se.saltcode.exception.NoSuchTransactionException;
import se.saltcode.model.transaction.Transaction;
import se.saltcode.repository.ITransactionRepository;

@Service
public class TransactionService {

  private final ITransactionRepository transactionRepository;

  public TransactionService(ITransactionRepository transactionService) {
    this.transactionRepository = transactionService;
  }

  public List<Transaction> getAllTransactions() {
    return transactionRepository.findAll();
  }

  public Transaction getTransactionById(UUID eventID) {
    return transactionRepository.findById(eventID).orElseThrow(NoSuchTransactionException::new);
  }
}

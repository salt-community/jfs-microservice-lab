package se.saltcode.repository;

import org.springframework.data.repository.ListCrudRepository;
import se.saltcode.model.transaction.Transaction;

import java.util.UUID;

public interface TransactionDbRepository extends ListCrudRepository<Transaction, UUID> {
}

package se.saltcode.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import se.saltcode.model.transaction.Transaction;
import java.util.UUID;

@Repository
public interface TransactionDbRepository extends ListCrudRepository<Transaction, UUID> {}

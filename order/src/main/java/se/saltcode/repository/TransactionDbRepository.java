package se.saltcode.repository;

import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import se.saltcode.model.transaction.Transaction;

@Repository
public interface TransactionDbRepository extends ListCrudRepository<Transaction, UUID> {}

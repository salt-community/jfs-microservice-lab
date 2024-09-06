package se.saltcode.order.model;

import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface TransactionDbRepository extends ListCrudRepository<Transaction, UUID> {
}

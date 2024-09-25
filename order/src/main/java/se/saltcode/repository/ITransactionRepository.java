package se.saltcode.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.saltcode.model.transaction.Transaction;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, UUID> {}

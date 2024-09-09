package se.saltcode.model.transaction;

import org.springframework.stereotype.Repository;
import se.saltcode.repository.TransactionDbRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TransactionRepository {

    private final TransactionDbRepository repo;

    public TransactionRepository(TransactionDbRepository repo) {
        this.repo = repo;
    }

    public List<Transaction> findAll() {
        return repo.findAll();
    }

    public Optional<Transaction> findById(UUID eventID) {
        return repo.findById(eventID);
    }

    public Transaction save(Transaction transaction) {
        return repo.save(transaction);
    }

    public void deleteById(UUID eventID) {
        repo.deleteById(eventID);
    }
}

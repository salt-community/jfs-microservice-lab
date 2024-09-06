package se.saltcode.repository;

import org.springframework.stereotype.Repository;
import se.saltcode.order.model.Transaction;
import se.saltcode.order.model.TransactionDbRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderRepositoryDb {
    private final OrderRepository orderRepository;


    public OrderRepositoryDb(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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

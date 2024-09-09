package se.saltcode.repository;

import org.springframework.stereotype.Repository;
import se.saltcode.error.NoSuchOrderException;
import se.saltcode.model.order.Order;

import java.util.List;
import java.util.UUID;

@Repository
public class OrderRepository {

    private final IOrderRepository IOrderRepository;

    public OrderRepository(IOrderRepository IOrderRepository) {
        this.IOrderRepository = IOrderRepository;
    }

    public List<Order> getOrders() {
        return IOrderRepository.findAll();
    }

    public Order getOrder(UUID id) {
        return IOrderRepository.findById(id).orElseThrow(NoSuchOrderException::new);
    }

    public UUID createOrder(Order order) {
        return IOrderRepository.save(order).getId();
    }

    public void deleteOrder(UUID id) {

        if(!IOrderRepository.existsById(id)){
            throw new NoSuchOrderException();
        }
        IOrderRepository.deleteById(id);
    }

    public Order updateOrder(Order order) {
        if(!IOrderRepository.existsById(order.getId())){
            throw new NoSuchOrderException();
        }
        return IOrderRepository.save(order);
    }
}

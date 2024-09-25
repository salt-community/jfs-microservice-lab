package se.saltcode.inventory.service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import se.saltcode.inventory.exception.NoSuchOrderCacheException;
import se.saltcode.inventory.model.cache.OrderCache;
import se.saltcode.inventory.repository.IOrderCacheRepository;

@Service
public class OrderCacheService {

  private final IOrderCacheRepository orderCacheRepository;

  public OrderCacheService(IOrderCacheRepository orderCacheRepository) {
    this.orderCacheRepository = orderCacheRepository;
  }

  public List<OrderCache> getAllOrderCacheItems() {
    return orderCacheRepository.findAll();
  }

  public OrderCache getOrderCacheById(UUID id) {
    return orderCacheRepository.findById(id).orElseThrow(NoSuchOrderCacheException::new);
  }

  public OrderCache createOrderCacheItem(OrderCache orderCache) {
    return orderCacheRepository.save(orderCache);
  }

  public void deleteOrderCacheById(UUID id) {
    if (!orderCacheRepository.existsById(id)) {
      throw new NoSuchOrderCacheException();
    }
    orderCacheRepository.deleteById(id);
  }

  @Scheduled(fixedRate = 6000000)
  public void clearCacheItems() {
    orderCacheRepository.findAll().stream()
        .filter(
            e -> Duration.between(e.getCreatedAt(), ZonedDateTime.now()).get(ChronoUnit.HOURS) > 48)
        .forEach(e -> orderCacheRepository.deleteById(e.getId()));
  }
}

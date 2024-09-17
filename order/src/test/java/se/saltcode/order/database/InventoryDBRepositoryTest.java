package se.saltcode.order.database;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.saltcode.repository.IOrderRepository;

@SpringBootTest
class InventoryDBRepositoryTest {

  @Autowired private IOrderRepository repository;
}

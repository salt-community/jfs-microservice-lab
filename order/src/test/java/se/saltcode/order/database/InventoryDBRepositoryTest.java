package se.saltcode.order.database;

import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.saltcode.repository.IOrderRepository;

@EnableRuleMigrationSupport
@SpringBootTest
class InventoryDBRepositoryTest {

  @Autowired private IOrderRepository repository;
}

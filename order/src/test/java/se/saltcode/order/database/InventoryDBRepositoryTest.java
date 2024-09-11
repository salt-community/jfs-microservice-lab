package se.saltcode.order.database;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import se.saltcode.model.order.Orders;
import se.saltcode.repository.IOrderRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InventoryDBRepositoryTest {

    @Autowired
    private IOrderRepository repository;

    // Test to check if database creates an item
    @Test
    @Transactional
    @Rollback(value = true) // Used to ensure that test data is rolled back after testing is complete
    public void shouldCreateOneItem() {

        // Arrange
        Orders entity = new Orders();
        entity.setQuantity(1234);

        // Act
        repository.save(entity);

        // Assert
        assertThat(repository.findAll()).hasSize(1);
    }

    // Test to check if repository can find an item by its id
    @Test
    @Transactional
    @Rollback(value = true)
    public void shouldFindItemById() {

        // Arrange
        Orders entity = new Orders();
        entity.setId(UUID.randomUUID());
        entity.setInventoryId(UUID.randomUUID());
        entity.setCustomerId(UUID.randomUUID());
        entity.setQuantity(1234);
        entity.setTotalCost(12345D);

        // Act
        Orders savedEntity = repository.save(entity);
        UUID id = savedEntity.getId();

        // Assert
        Orders foundEntity = repository.findById(id).orElse(null);
        assertNotNull(foundEntity);
        assertThat(foundEntity.getQuantity()).isEqualTo(1234);
    }

    // Test to check if repository can update an existing items properties
    @Test
    @Transactional
    @Rollback(value = true)
    public void shouldUpdateItemQuantity() {

        // Arrange
        Orders entity = new Orders();
        entity.setId(UUID.randomUUID());
        entity.setInventoryId(UUID.randomUUID());
        entity.setCustomerId(UUID.randomUUID());
        entity.setQuantity(1234);
        entity.setTotalCost(12345D);
        Orders savedEntity1 = repository.save(entity);

        // Act
        Orders savedEntity2 = repository.findById(savedEntity1.getId()).orElse(null);
        savedEntity2.setQuantity(500); // Update the quantity
        repository.save(savedEntity2);

        // Assert
        Orders updatedEntity = repository.findById(savedEntity1.getId()).orElse(null);
        assertNotNull(updatedEntity);
        assertThat(updatedEntity.getQuantity()).isEqualTo(500);
    }

    // Test to check if repository can delete an item
    @Test
    @Transactional
    @Rollback(value = true)
    public void shouldDeleteItem() {

        // Arrange
        Orders entity = new Orders();
        entity.setId(UUID.randomUUID());
        entity.setInventoryId(UUID.randomUUID());
        entity.setCustomerId(UUID.randomUUID());
        entity.setQuantity(1234);
        entity.setTotalCost(12345D);
        Orders savedEntity = repository.save(entity);

        // Act
        repository.deleteById(savedEntity.getId());

        // Assert
        assertThat(repository.findAll()).isEmpty();
    }

    // Test to check if the repository can get all items in the database
    @Test
    @Transactional
    @Rollback(value = true)
    public void shouldFindAllItems() {

        // Arrange
        Orders entity1 = new Orders();
        entity1.setId(UUID.randomUUID());
        entity1.setInventoryId(UUID.randomUUID());
        entity1.setCustomerId(UUID.randomUUID());
        entity1.setQuantity(1234);
        entity1.setTotalCost(12345D);

        Orders entity2 = new Orders();
        entity2.setId(UUID.randomUUID());
        entity2.setInventoryId(UUID.randomUUID());
        entity2.setCustomerId(UUID.randomUUID());
        entity2.setQuantity(1234);
        entity2.setTotalCost(12345D);

        repository.save(entity1);
        repository.save(entity2);

        // Act
        List<Orders> items = repository.findAll();

        // Assert
        assertThat(items).hasSize(2);
    }

}
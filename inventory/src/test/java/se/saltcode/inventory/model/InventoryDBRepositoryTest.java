package se.saltcode.inventory.model;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class InventoryDBRepositoryTest {

  @Autowired private InventoryDBRepository repository;

  // Test to check if database creates an item
  @Test
  @Transactional
  @Rollback(value = true) // Used to ensure that test data is rolled back after testing is complete
  public void shouldCreateOneItem() {

    // Arrange
    Inventory entity = new Inventory();
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
    Inventory entity = new Inventory();
    entity.setProduct("Product 1");
    entity.setQuantity(100);
    entity.setReservedQuantity(10);

    // Act
    Inventory savedEntity = repository.save(entity);
    UUID id = savedEntity.getId();

    // Assert
    Inventory foundEntity = repository.findById(id).orElse(null);
    assertNotNull(foundEntity);
    assertThat(foundEntity.getProduct()).isEqualTo("Product 1");
  }

  // Test to check if repository can update an existing items properties
  @Test
  @Transactional
  @Rollback(value = true)
  public void shouldUpdateItemQuantity() {

    // Arrange
    Inventory entity = new Inventory();
    entity.setProduct("Product 2");
    entity.setQuantity(200);
    repository.save(entity);

    // Act
    Inventory savedEntity = repository.findAll().get(0);
    savedEntity.setQuantity(500); // Update the quantity
    repository.save(savedEntity);

    // Assert
    Inventory updatedEntity = repository.findById(savedEntity.getId()).orElse(null);
    assertNotNull(updatedEntity);
    assertThat(updatedEntity.getQuantity()).isEqualTo(500);
  }

  // Test to check if repository can delete an item
  @Test
  @Transactional
  @Rollback(value = true)
  public void shouldDeleteItem() {

    // Arrange
    Inventory entity = new Inventory();
    entity.setProduct("Product 3");
    entity.setQuantity(300);
    Inventory savedEntity = repository.save(entity);

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
    Inventory entity1 = new Inventory();
    entity1.setProduct("Product 4");
    entity1.setQuantity(400);
    Inventory entity2 = new Inventory();
    entity2.setProduct("Product 5");
    entity2.setQuantity(500);

    repository.save(entity1);
    repository.save(entity2);

    // Act
    List<Inventory> items = repository.findAll();

    // Assert
    assertThat(items).hasSize(2);
  }

  // Test to check if repository can find an item by its name
  @Test
  @Transactional
  @Rollback(value = true)
  public void shouldFindByProductName() {
    // Arrange
    Inventory entity = new Inventory();
    entity.setProduct("Unique Product");
    entity.setQuantity(600);
    repository.save(entity);

    // Act
    List<Inventory> foundItems = repository.findByProduct("Unique Product");

    // Assert
    assertThat(foundItems).hasSize(1);
    assertThat(foundItems.get(0).getQuantity()).isEqualTo(600);
  }
}

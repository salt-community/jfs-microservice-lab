package se.saltcode.inventory.testTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import se.saltcode.inventory.model.Inventory;
import se.saltcode.inventory.model.InventoryDBRepository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * The purpose of this test class is to test the in memory database, which itself is used for
 * testing purposes. This might seem redundant. Which it is.
 */

@SpringBootTest
public class H2DatabaseTest {

    @Autowired
    private InventoryDBRepository repository;

    @Test
    @Transactional
    @Rollback(value = true) // Used to ensure that test data is rolled back after testing is complete
    public void testH2Database() {

        // Arrange
        Inventory entity = new Inventory();
        entity.setQuantity(1234);

        // Act
        repository.save(entity);

        // Assert
        assertThat(repository.findAll()).hasSize(1);
    }

}

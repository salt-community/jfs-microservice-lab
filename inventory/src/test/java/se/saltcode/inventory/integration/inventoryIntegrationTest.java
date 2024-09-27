package se.saltcode.inventory.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import se.saltcode.inventory.model.inventory.AddInventoryDto;
import se.saltcode.inventory.model.inventory.Inventory;
import se.saltcode.inventory.model.inventory.InventoryDto;
import se.saltcode.inventory.repository.IInventoryRepository;
import se.saltcode.inventory.repository.IOrderCacheRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class inventoryIntegrationTest {

    @Value("${server.port}")
    private int configPort;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    private IInventoryRepository inventoryRepository;

    @Autowired
    private IOrderCacheRepository orderCacheRepository;

    @Value("${this.base-uri}${api.base-path}${api.controllers.inventory}")
    private String apiUri;

    @BeforeEach
    public void setup() {
        inventoryRepository.deleteAll();
        orderCacheRepository.deleteAll();
    }

    @Test
    public void portShouldUseIntegrationProperties() {
        assertThat(configPort).isEqualTo(5000);
    }

    @Test
    public void shouldBeAbleToCreateNewInventory(){
        AddInventoryDto newInventory = new AddInventoryDto("rope",100);
        //InventoryDto createdInventory = restTemplate.postForObject("http://localhost:" + configPort + "/api/inventory", newInventory,InventoryDto.class);

        // Assert

        ResponseEntity<InventoryDto> exchange = restTemplate.exchange(apiUri, HttpMethod.POST, new HttpEntity<>(newInventory), InventoryDto.class);

        // Assert
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(exchange.getHeaders().get("location").get(0)).isEqualTo(apiUri+"/"+exchange.getBody().id());
        assertThat(exchange.getBody().quantity()).isEqualTo(100);
        assertThat(exchange.getBody().product()).isEqualTo("rope");
        assertThat(exchange.getBody().reservedQuantity()).isEqualTo(0);


        Inventory inventory = inventoryRepository.findById(exchange.getBody().id()).orElse(null);

        assert inventory != null;
        assertThat(inventory.getQuantity()).isEqualTo(100);
        assertThat(inventory.getProduct()).isEqualTo("rope");
        assertThat(inventory.getReservedQuantity()).isEqualTo(0);

    }

    @Test
    public void shouldThrowErrorWhenCreatingInventoryWithNullProduct(){

    }

    @Test
    public void shouldThrowErrorWhenCreatingInventoryWithNullQuantity(){

    }

    @Test
    public void shouldThrowErrorWhenCreatingInventoryWithNegativeQuantity(){

    }


    @Test
    public void shouldBeAbleToGetAllInventoryItems(){

    }

    @Test
    public void shouldBeAbleToGetInventoryItemById(){

    }

    @Test
    public void shouldThrowErrorWhenGettingInventoryThatDoesNotExist(){

    }

    @Test
    public void shouldThrowErrorWhenDeletingInventoryThatDoesNotExist(){

    }

    @Test
    public void shouldDeleteItem(){

    }

    @Test
    public void ShouldUpdateInventoryItem(){

    }

    @Test
    public void shouldThrowErrorWhenUpdatingInventoryWithNullProduct(){

    }

    @Test
    public void shouldThrowErrorWhenUpdatingInventoryWithNullQuantity(){

    }

    @Test
    public void shouldThrowErrorWhenUpdatingInventoryWithNegativeQuantity(){

    }

    @Test
    public void shouldThrowErrorWhenUpdatingInventoryThatDoesNotExist(){

    }

    @Test
    public void shouldReturnUpdateResultForSuccessInUpdateQuantityOfInventory(){

    }
    @Test
    public void shouldReturnUpdateResultForDuplicateMessageInUpdateQuantityOfInventory(){

    }

    @Test
    public void shouldReturnUpdateResultForNoSuchInventoryInUpdateQuantityOfInventory(){

    }
    @Test
    public void shouldReturnUpdateResultForInsuficientQuantityInUpdateQuantityOfInventory(){

    }


}

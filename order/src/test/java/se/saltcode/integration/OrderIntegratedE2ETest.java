package se.saltcode.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import se.saltcode.model.order.AddOrderDto;
import se.saltcode.model.order.OrderDto;
import se.saltcode.repository.IOrderRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderIntegratedE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${local.server.port}")
    private int configPort;

    @Autowired
    private IOrderRepository repo;

    @AfterEach
    public void cleanUp() {
        try {
            repo.deleteAll();
        } catch (Exception e) {
            // Log or handle cleanup exceptions if necessary
            System.err.println("Error during cleanup: " + e.getMessage());
        }
    }

    @Test
    public void shouldReturnOrderWhenGetWithId() {
        String url = "http://localhost:" + configPort + "/api/order";
        AddOrderDto addOrderDto = new AddOrderDto(UUID.randomUUID(), 1, 10);

        try {
            // Create order
            ResponseEntity<OrderDto> exchange = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(addOrderDto), OrderDto.class);
            assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            OrderDto responseBody = exchange.getBody();
            assertThat(responseBody).isNotNull();
            assertThat(responseBody.id()).isNotNull();

            // Retrieve order by ID
            UUID id = responseBody.id();
            OrderDto order = restTemplate.getForObject(url + "/" + id, OrderDto.class);
            assertThat(order).isNotNull();
            assertThat(order.id()).isEqualTo(id);

        } catch (RestClientException e) {
            // Log the response for debugging
            System.err.println("Error during REST call: " + e.getMessage());
        }
    }

    @Test
    public void shouldAddOrderToRepositoryAfterItIsCreated() {
        String url = "http://localhost:" + configPort + "/api/order";

        try {
            // Get the initial list of orders
            OrderDto[] orderArray = restTemplate.getForObject(url, OrderDto[].class);
            int numberOfOrdersInList = orderArray != null ? orderArray.length : 0;

            // Create a new order
            AddOrderDto addOrderDto = new AddOrderDto(UUID.randomUUID(), 1, 10);
            ResponseEntity<OrderDto> exchange = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(addOrderDto), OrderDto.class);
            assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(exchange.getBody()).isNotNull();

            // Get the updated list of orders
            OrderDto[] newOrderArray = restTemplate.getForObject(url, OrderDto[].class);
            int newNumberOfOrdersInList = newOrderArray != null ? newOrderArray.length : 0;

            assertThat(newNumberOfOrdersInList).isEqualTo(numberOfOrdersInList + 1);

        } catch (RestClientException e) {
            // Log the response for debugging
            System.err.println("Error during REST call: " + e.getMessage());
        }
    }
}

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
import se.saltcode.model.order.AddOrderDto;
import se.saltcode.model.order.Order;
import se.saltcode.model.order.OrderDto;
import se.saltcode.repository.IOrderRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class OrderIntegratedE2Etest {

    private final String UUID_STRING = "00000000-0000-0000-0000-000000000001";

    @Autowired
    protected TestRestTemplate restTemplate;
    @Value("${server.port}")
    private int configPort;
    @Autowired
    private IOrderRepository repo;



    @AfterEach
    public void cleanUp() {
        // we are adding jokes to the repo in the tests, we need to remove them after each test, so we have a known state
        repo.deleteAll();
    }

    @Test
    public void portShouldUseIntegrationProperties() {
        assertThat(configPort).isEqualTo(8080);
    }


    @Test
    public void shouldReturnNoJokesWhenStartingClean() throws Exception {
        // Arrange

        // Act

        List<OrderDto> orderList = restTemplate.getForObject("http://localhost:" + configPort + "/api/order", List.class);
        // Assert
        int numberOfOrdersInList = orderList.size();

        AddOrderDto addOrderDto = new AddOrderDto(UUID.randomUUID(), 1, 10);
        String url = "http://localhost:" + configPort + "/api/order";

        ResponseEntity<AddOrderDto> exchange = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<AddOrderDto>(addOrderDto), AddOrderDto.class);

        // Assert
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        List<OrderDto> newOrderList = restTemplate.getForObject("http://localhost:" + configPort + "/api/order", List.class);

        assertThat(newOrderList.size()).isEqualTo(numberOfOrdersInList + 1); // checks that the joke is saved in the DB
    }   
}

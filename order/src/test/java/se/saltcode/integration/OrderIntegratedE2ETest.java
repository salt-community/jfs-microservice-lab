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
import se.saltcode.model.order.OrderDto;
import se.saltcode.repository.IOrderRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class OrderIntegratedE2ETest {

    @Autowired
    protected TestRestTemplate restTemplate;
    @Value("${server.port}")
    private int configPort;
    @Autowired
    private IOrderRepository repo;



    @AfterEach
    public void cleanUp() {
        repo.deleteAll();
    }

    @Test
    public void portShouldUse8080() {
        assertThat(configPort).isEqualTo(8080);
    }

    @Test
    public void shouldReturnOrderWhenGetWithId() {
        String url = "http://localhost:" + configPort + "/api/order";
        AddOrderDto addOrderDto = new AddOrderDto(UUID.randomUUID(), 1, 10);
        ResponseEntity<OrderDto> exchange = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<AddOrderDto>(addOrderDto), OrderDto.class);
        UUID id = exchange.getBody().id();
        OrderDto order = restTemplate.getForObject(url + "/" + id, OrderDto.class);
        assertThat(order).isNotNull();
    }

    @Test
    public void shouldAddOrderToRepositoryAfterItIsCreated() throws Exception {
        String url = "http://localhost:" + configPort + "/api/order";

        List<OrderDto> orderList = restTemplate.getForObject(url, List.class);
        int numberOfOrdersInList = orderList.size();

        AddOrderDto addOrderDto = new AddOrderDto(UUID.randomUUID(), 1, 10);
        ResponseEntity<AddOrderDto> exchange = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<AddOrderDto>(addOrderDto), AddOrderDto.class);
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        List<OrderDto> newOrderList = restTemplate.getForObject(url, List.class);
        int newNumberOfOrdersInList = newOrderList.size();

        assertThat(newNumberOfOrdersInList).isEqualTo(numberOfOrdersInList + 1); // checks that the joke is saved in the DB
    }
}

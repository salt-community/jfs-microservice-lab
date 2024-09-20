package se.saltcode.components;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import se.saltcode.model.order.AddOrderDTO;
import se.saltcode.model.order.Orders;
import se.saltcode.repository.ITransactionRepository;
import se.saltcode.repository.IOrderRepository;
import se.saltcode.repository.TransactionRepository;
import se.saltcode.service.OrderService;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MessageRelayTest {

    private static MockWebServer mockBackEnd;


    @Autowired
    private ITransactionRepository transactionRepository;

    @Autowired
    private IOrderRepository orderRepository;

    private MessageRelay messageRelay;
    private OrderService orderService;



    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s", mockBackEnd.getPort());
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();

         messageRelay = new MessageRelay(transactionRepository, orderRepository, webClient);
         orderService = new OrderService(orderRepository, messageRelay, transactionRepository);

    }

    @Test
    void sendUnfinishedMessages_WhenServiceIsUp_ShouldDeleteTransaction() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200 OK")
                .addHeader("Content-Type", "application/json"));

        AddOrderDTO addOrderDTO = new AddOrderDTO(UUID.randomUUID(), UUID.randomUUID(), 10, 10);
        Orders orders = new Orders(addOrderDTO);
        orderService.createOrder(orders);


        assertEquals(0, transactionRepository.findAll().size());
    }

    @Test
    void sendUnfinishedMessages_WhenServiceIsDown_ShouldNotDeleteTransaction() throws Exception {
        mockBackEnd.enqueue(new MockResponse().setHttp2ErrorCode(500));
        AddOrderDTO addOrderDTO = new AddOrderDTO(UUID.randomUUID(), UUID.randomUUID(), 10, 10);
        Orders orders = new Orders(addOrderDTO);
        orderService.createOrder(orders);

        assertEquals(1, transactionRepository.findAll().size());
    }
}

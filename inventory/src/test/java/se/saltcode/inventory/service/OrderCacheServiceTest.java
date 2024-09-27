package se.saltcode.inventory.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.saltcode.inventory.exception.NoSuchOrderCacheException;
import se.saltcode.inventory.model.cache.OrderCache;
import se.saltcode.inventory.repository.IOrderCacheRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderCacheServiceTest {

    @Mock
    private IOrderCacheRepository orderCacheRepository;

    @InjectMocks
    private OrderCacheService orderCacheService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void shouldReturnAllOrderCacheItems() {
        // Arrange
        OrderCache orderCache = new OrderCache();
        when(orderCacheRepository.findAll()).thenReturn(List.of(orderCache));

        // Act
        List<OrderCache> result = orderCacheService.getAllOrderCacheItems();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderCacheRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnOrderCacheById_whenIdExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        OrderCache orderCache = new OrderCache();
        when(orderCacheRepository.findById(id)).thenReturn(Optional.of(orderCache));

        // Act
        OrderCache result = orderCacheService.getOrderCacheById(id);

        // Assert
        assertNotNull(result);
        verify(orderCacheRepository, times(1)).findById(id);
    }

    @Test
    void shouldThrowException_whenOrderCacheByIdNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(orderCacheRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchOrderCacheException.class, () -> orderCacheService.getOrderCacheById(id));
        verify(orderCacheRepository, times(1)).findById(id);
    }

    @Test
    void shouldCreateOrderCacheItem() {
        // Arrange
        OrderCache orderCache = new OrderCache();
        when(orderCacheRepository.save(orderCache)).thenReturn(orderCache);

        // Act
        OrderCache result = orderCacheService.createOrderCacheItem(orderCache);

        // Assert
        assertNotNull(result);
        verify(orderCacheRepository, times(1)).save(orderCache);
    }

    @Test
    void shouldDeleteOrderCacheById_whenIdExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(orderCacheRepository.existsById(id)).thenReturn(true);

        // Act
        orderCacheService.deleteOrderCacheById(id);

        // Assert
        verify(orderCacheRepository, times(1)).deleteById(id);
    }

    @Test
    void shouldThrowException_whenDeleteOrderCacheByIdNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(orderCacheRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchOrderCacheException.class, () -> orderCacheService.deleteOrderCacheById(id));
        verify(orderCacheRepository, times(0)).deleteById(id);
    }
    @Test
    void shouldClearCacheItemsOlderThan48Hours() {
        // Arrange
        UUID id = UUID.randomUUID();
        OrderCache oldCache = new OrderCache();
        oldCache.setId(id);
        oldCache.setCreatedAt(ZonedDateTime.now().minusDays(3)); // Older than 48 hours

        OrderCache newCache = new OrderCache();
        newCache.setId(UUID.randomUUID());
        newCache.setCreatedAt(ZonedDateTime.now().minusHours(10)); // Newer than 48 hours

        when(orderCacheRepository.findAll()).thenReturn(List.of(oldCache, newCache));

        // Act
        orderCacheService.clearCacheItems();

        // Assert
        verify(orderCacheRepository, times(1)).deleteById(id);
        verify(orderCacheRepository, times(0)).deleteById(newCache.getId());
    }
}
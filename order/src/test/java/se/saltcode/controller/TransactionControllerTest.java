package se.saltcode.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.saltcode.model.enums.Event;
import se.saltcode.model.transaction.Transaction;
import se.saltcode.service.TransactionService;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private TransactionService transactionService;

  private UUID eventID;
  private Transaction transaction;

  @BeforeEach
  void setUp() {
    eventID = UUID.randomUUID();
    UUID inventoryId = UUID.randomUUID();
    transaction = new Transaction( Event.PURCHASE, 5, null);
    transaction.setId(eventID);
  }

  @Test
  void shouldGetAllTransactions() throws Exception {
    when(transactionService.getAllTransactions())
        .thenReturn(Collections.singletonList(transaction));

    mockMvc
        .perform(get("/api/transaction").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].eventType").value("PURCHASE"));
  }

  @Test
  void shouldGetTransactionById() throws Exception {

    when(transactionService.getTransactionById(eventID)).thenReturn(transaction);
    mockMvc
        .perform(get("/api/transaction/{eventID}", eventID).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(eventID.toString()))
        .andExpect(jsonPath("$.eventType").value("PURCHASE"));
  }
}

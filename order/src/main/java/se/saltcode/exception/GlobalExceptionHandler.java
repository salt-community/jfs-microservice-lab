package se.saltcode.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({NoSuchTransactionException.class})
  public ResponseEntity<String> NoSuchTransaction(NoSuchTransactionException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({NoSuchOrderException.class})
  public ResponseEntity<String> NoSuchOrder(NoSuchOrderException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<String> defaultErrorHandler(Exception ex) {
    return ResponseEntity.internalServerError().body(ex.getMessage());
  }
}

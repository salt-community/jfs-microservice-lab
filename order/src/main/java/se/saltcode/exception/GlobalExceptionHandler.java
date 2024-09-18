package se.saltcode.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({NoSuchOrderException.class})
  public ResponseEntity<String> NoSuchOrderException(NoSuchOrderException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({InsufficientInventoryException.class})
  public ResponseEntity<String> InsufficientInventoryException(InsufficientInventoryException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<String> defaultErrorHandler(Exception ex) {
    return ResponseEntity.internalServerError().body(ex.getMessage());
  }
}

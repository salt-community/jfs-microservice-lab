package se.saltcode.exception;

public class InsufficientInventoryException extends IllegalArgumentException {
  public InsufficientInventoryException() {
    super("There is not enough inventory for this order");
  }
}

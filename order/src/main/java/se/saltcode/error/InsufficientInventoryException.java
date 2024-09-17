package se.saltcode.error;

public class InsufficientInventoryException extends IllegalArgumentException {
  public InsufficientInventoryException() {
    super("There is not enough inventory for this order");
  }
}

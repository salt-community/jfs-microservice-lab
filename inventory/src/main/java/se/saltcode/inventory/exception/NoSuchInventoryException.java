package se.saltcode.inventory.exception;

import java.util.NoSuchElementException;

public class NoSuchInventoryException extends NoSuchElementException {
  public NoSuchInventoryException() {
    super("no inventory with matching id found");
  }
}

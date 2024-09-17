package se.saltcode.error;

import java.util.NoSuchElementException;

public class NoSuchOrderException extends NoSuchElementException {
  public NoSuchOrderException() {
    super("no Order with matching id found");
  }
}

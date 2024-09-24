package se.saltcode.inventory.exception;

import java.util.NoSuchElementException;

public class NoSuchOrderCacheException extends NoSuchElementException {
  public NoSuchOrderCacheException() {
    super("no orderCache with matching id found");
  }
}

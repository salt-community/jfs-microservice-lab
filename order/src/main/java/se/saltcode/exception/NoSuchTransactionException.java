package se.saltcode.exception;

import java.util.NoSuchElementException;

public class NoSuchTransactionException extends NoSuchElementException {
  public NoSuchTransactionException() {
    super("no transaction with matching id found");
  }
}

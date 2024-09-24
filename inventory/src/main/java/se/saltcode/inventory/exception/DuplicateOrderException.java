package se.saltcode.inventory.exception;

public class DuplicateOrderException extends IllegalArgumentException{
    public DuplicateOrderException() {
        super("Duplicate order");
    }
}

package ru.practicum.shareit.item.exception;

public class ItemOwnerMismatchException extends RuntimeException {
    public ItemOwnerMismatchException(String message) {
        super(message);
    }
}

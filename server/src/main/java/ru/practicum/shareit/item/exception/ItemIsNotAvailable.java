package ru.practicum.shareit.item.exception;

public class ItemIsNotAvailable extends RuntimeException {
    public ItemIsNotAvailable(String message) {
        super(message);
    }
}

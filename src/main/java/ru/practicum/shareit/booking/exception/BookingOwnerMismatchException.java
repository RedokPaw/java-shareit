package ru.practicum.shareit.booking.exception;

public class BookingOwnerMismatchException extends RuntimeException {
    public BookingOwnerMismatchException(String message) {
        super(message);
    }
}

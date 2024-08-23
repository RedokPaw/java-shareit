package ru.practicum.shareit.item.exception;

public class CommentForbiddenBooking extends RuntimeException {
    public CommentForbiddenBooking(String message) {
        super(message);
    }
}

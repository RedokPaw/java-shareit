package ru.practicum.shareit.item.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

@RestControllerAdvice
public class ItemErrorHandler {
    @ExceptionHandler({ValidationException.class, ItemValidationException.class, ItemNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleValidationException(final RuntimeException e) {
        return e.getMessage();
    }

    @ExceptionHandler({ItemOwnerMismatchException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleItemOwnerMismatchException(final ItemOwnerMismatchException e) {
        return e.getMessage();
    }

}

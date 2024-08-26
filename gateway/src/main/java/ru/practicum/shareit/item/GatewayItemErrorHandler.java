package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.error.ErrorResponse;
import ru.practicum.shareit.item.exception.ItemValidationException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GatewayItemErrorHandler {
    @ExceptionHandler({ValidationException.class, ItemValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final RuntimeException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }
}

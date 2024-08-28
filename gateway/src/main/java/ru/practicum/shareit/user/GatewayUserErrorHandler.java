package ru.practicum.shareit.user;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.error.ErrorResponse;
import ru.practicum.shareit.user.exception.UserValidationException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GatewayUserErrorHandler {
    @ExceptionHandler({ValidationException.class, UserValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.error("Validation exception, check data");
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }
}

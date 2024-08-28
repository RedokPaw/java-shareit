package ru.practicum.shareit.booking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exception.BookingTimeValidationException;

@RestControllerAdvice
public class GatewayBookingErrorHandler {

    @ExceptionHandler(BookingTimeValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleTimeValidationException(BookingTimeValidationException e) {
        return e.getMessage();
    }

}

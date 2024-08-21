package ru.practicum.shareit.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BookingErrorHandler {

    @ExceptionHandler(BookingTimeValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleTimeValidationException(BookingTimeValidationException e) {
        return e.getMessage();
    }

    @ExceptionHandler(BookingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleBookingNotFoundException(BookingNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(BookingOwnerMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleOwnerMismatchException(BookingOwnerMismatchException e) {
        return new ErrorResponse(e.getMessage());
    }
}

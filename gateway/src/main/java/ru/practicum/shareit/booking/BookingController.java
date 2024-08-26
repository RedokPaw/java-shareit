package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.exception.BookingTimeValidationException;

import java.time.LocalDateTime;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}", stateParam, userId);
        return bookingClient.getBookings(userId, state);
    }

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestBody @Valid BookingDto bookingDto) {
        log.info("Creating booking {}, userId={}", bookingDto, userId);
        validateBookingTime(bookingDto.getStart(), bookingDto.getEnd(), LocalDateTime.now());
        return bookingClient.createBooking(userId, bookingDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> bookingOwnerChangeStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                           @PathVariable Long bookingId,
                                                           @RequestParam(value = "approved") Boolean approved) {
        log.info("GateWay approve booking from user: {} booking id: {} approve status: {}",
                userId, bookingId, approved);
        return bookingClient.approveBooking(userId, bookingId, approved);
    }

    @GetMapping(value = "/owner")
    public ResponseEntity<Object> getAllBookingsByItemOwner(@RequestHeader("X-Sharer-User-Id") long userid,
                                                            @RequestParam(required = false,
                                                                    defaultValue = "ALL") BookingState state) {
        log.info("Gateway request for getting all bookings by item owner with id: {} ", userid);
        return bookingClient.getAllBookingsByOwner(userid, state);
    }

    private void validateBookingTime(LocalDateTime start, LocalDateTime end, LocalDateTime now) {
        if (end.isBefore(now)) {
            throw new BookingTimeValidationException("end time cannot be in past");
        }
        if (end.isEqual(start)) {
            throw new BookingTimeValidationException("end time must not equal to start time");
        }
        if (start.isBefore(now)) {
            throw new BookingTimeValidationException("start time cannot be in past");
        }
    }

}

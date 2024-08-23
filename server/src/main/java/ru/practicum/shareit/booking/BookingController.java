package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestBody BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") int ownerId) {
        log.info("request for create booking with booking: {}", bookingDto);
        return bookingService.createBooking(bookingDto, ownerId);
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                     @PathVariable int bookingId,
                                     @RequestParam(value = "approved") boolean isApproved) {
        log.info("request for approve: {} booking with id: {} from ownerId: {}", isApproved, bookingId, ownerId);
        return bookingService.approveBooking(bookingId, ownerId, isApproved);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                 @PathVariable int bookingId) {
        log.info("Request for getting booking with id: {} ", bookingId);
        return bookingService.getBooking(bookingId, ownerId);
    }

    @GetMapping
    public List<BookingDto> getAllBookings(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                           @RequestParam(required = false, defaultValue = "ALL") BookingState state) {
        log.info("Request for getting all bookings with state: {} ", state);
        return bookingService.getAllBookings(ownerId, state);
    }

    @GetMapping(value = "/owner")
    public List<BookingDto> getAllBookingsByItemOwner(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                                      @RequestParam(required = false,
                                                              defaultValue = "ALL") BookingState state) {
        log.info("Request for getting all bookings by item owner with id: {} ", ownerId);
        return bookingService.getAllBookingsByItemOwner(ownerId, state);
    }

}

package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto createBooking(BookingDto bookingDto, int ownerId);

    BookingDto approveBooking(int bookingId, int ownerId, boolean isApproved);

    BookingDto getBooking(int bookingId, int ownerId);

    List<BookingDto> getAllBookings(int ownerId, BookingState bookingState);

    List<BookingDto> getAllBookingsByItemOwner(int ownerId, BookingState bookingState);
}

package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.BookingOwnerMismatchException;
import ru.practicum.shareit.booking.exception.BookingTimeValidationException;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.ItemIsNotAvailable;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto createBooking(BookingDto bookingDto, int ownerId) {
        LocalDateTime now = LocalDateTime.now();
        User booker = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("User for booking does not exist"));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("Item for booking does not exist"));

        if (!item.getAvailable()) {
            throw new ItemIsNotAvailable("Cannot book item: item is not available");
        }

        LocalDateTime end = bookingDto.getEnd()
                .orElseThrow(() -> new BookingTimeValidationException("end time is missing"));
        LocalDateTime start = bookingDto.getStart()
                .orElseThrow(() -> new BookingTimeValidationException("start time is missing"));
        validateBookingTime(start, end, now);


        Booking booking = BookingMapper.toBooking(bookingDto, booker, item);
        booking.setStatus(BookingStatus.WAITING);
        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto approveBooking(int bookingId, int ownerId, boolean isApproved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking for approve not found"));
        if (booking.getItem().getOwner().getId() != ownerId) {
            throw new BookingOwnerMismatchException("Requester owner id mismatching with booker owner id");
        }
        booking.setStatus(isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBooking(int bookingId, int ownerId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking for approve not found"));
        if (booking.getItem().getOwner().getId() != ownerId && booking.getBooker().getId() != ownerId) {
            throw new BookingOwnerMismatchException("Mismatch with owner or booker id");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookings(int bookerId, BookingState bookingState) {
        userRepository.findById(bookerId)
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));

        LocalDateTime currentTime = LocalDateTime.now();

        List<Booking> bookings = switch (bookingState) {
            case CURRENT -> bookingRepository.findCurrentBookingsByBookerId(bookerId, currentTime);
            case PAST -> bookingRepository.findPastBookingsByBookerId(bookerId, currentTime);
            case FUTURE -> bookingRepository.findFutureBookingsByBookerId(bookerId, currentTime);
            case WAITING -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING);
            case REJECTED ->
                    bookingRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.REJECTED);
            default -> bookingRepository.findByBookerIdOrderByStartDesc(bookerId);
        };

        return bookings.stream().map(BookingMapper::toBookingDto).toList();
    }

    @Override
    public List<BookingDto> getAllBookingsByItemOwner(int ownerId, BookingState bookingState) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));

        LocalDateTime currentTime = LocalDateTime.now();

        List<Booking> bookings = switch (bookingState) {
            case CURRENT -> bookingRepository.findCurrentBookingsByItemOwner(ownerId, currentTime);
            case PAST -> bookingRepository.findPastBookingsByItemOwner(ownerId, currentTime);
            case FUTURE -> bookingRepository.findFutureBookingsByItemOwner(ownerId, currentTime);
            case WAITING ->
                    bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING);
            case REJECTED ->
                    bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED);
            default -> bookingRepository.findByItem_Owner_IdOrderByStartDesc(ownerId);
        };

        return bookings.stream().map(BookingMapper::toBookingDto).toList();
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

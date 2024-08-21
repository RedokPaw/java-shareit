package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(booking.getItem().getId());
        itemDto.setName(Optional.ofNullable(booking.getItem().getName()));
        UserDto booker = new UserDto();
        booker.setId(booking.getBooker().getId());
        booker.setName(Optional.ofNullable(booking.getBooker().getName()));
        return BookingDto.builder()
                .id(booking.getId())
                .start(Optional.ofNullable(booking.getStart()))
                .end(Optional.ofNullable(booking.getEnd()))
                .item(itemDto)
                .booker(booker)
                .status(booking.getStatus())
                .build();
    }

    public static Booking toBooking(BookingDto bookingDto, User booker, Item item) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        if (bookingDto.getStart().isPresent()) {
            booking.setStart(bookingDto.getStart().get());
        }
        if (bookingDto.getEnd().isPresent()) {
            booking.setEnd(bookingDto.getEnd().get());
        }
        booking.setStatus(bookingDto.getStatus());
        booking.setBooker(booker);
        booking.setItem(item);
        return booking;
    }
}

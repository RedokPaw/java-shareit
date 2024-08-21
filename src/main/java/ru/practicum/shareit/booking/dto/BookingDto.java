package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
public class BookingDto {
    private int id;
    private Optional<LocalDateTime> start;
    private Optional<LocalDateTime> end;
    private Integer itemId;
    private ItemDto item;
    private UserDto booker;
    private BookingStatus status;
}

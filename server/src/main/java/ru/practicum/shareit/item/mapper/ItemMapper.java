package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemAnswerDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public class ItemMapper {
    public static ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(Optional.ofNullable(item.getName()))
                .description(Optional.ofNullable(item.getDescription()))
                .available(Optional.ofNullable(item.getAvailable()))
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        if (itemDto.getName().isPresent()) {
            item.setName(itemDto.getName().get());
        }
        if (itemDto.getDescription().isPresent()) {
            item.setDescription(itemDto.getDescription().get());
        }
        if (itemDto.getAvailable().isPresent()) {
            item.setAvailable(itemDto.getAvailable().get());
        }
        return item;
    }

    public static ItemDtoWithBookingsAndComments toItemDtoWithBookingsAndComments(Item item,
                                                                                  List<CommentDto> comments,
                                                                                  BookingDto lastBooking,
                                                                                  BookingDto nextBooking) {
        return ItemDtoWithBookingsAndComments.builder()
                .id(item.getId())
                .name(item.getName())
                .available(item.getAvailable())
                .description(item.getDescription())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .build();

    }

    public static ItemAnswerDto toItemAnswerDto(Item item) {
        return ItemAnswerDto.builder()
                .id(item.getId())
                .name(item.getName())
                .requesterId(item.getRequest().getId())
                .build();

    }
}

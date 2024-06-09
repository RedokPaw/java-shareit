package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

public class ItemMapper {
    public static ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(Optional.ofNullable(item.getName()))
                .description(Optional.ofNullable(item.getDescription()))
                .available(Optional.ofNullable(item.getAvailable()))
                .ownerId(Optional.ofNullable(item.getOwnerId()))
                .request(Optional.ofNullable(item.getRequest()))
                .build();
    }
}

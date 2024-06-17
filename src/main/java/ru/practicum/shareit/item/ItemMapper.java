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
        if (itemDto.getOwnerId().isPresent()) {
            item.setOwnerId(itemDto.getOwnerId().get());
        }
        if (itemDto.getRequest().isPresent()) {
            item.setRequest(itemDto.getRequest().get());
        }
        return item;
    }
}

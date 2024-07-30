package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto getItem(int id);

    ItemDto createItem(int ownerId, ItemDto itemDto);

    ItemDto updateItem(int itemId, ItemDto itemDto, int ownerId);

    ItemDto deleteItem(int id);

    List<ItemDto> getAllItems();

    List<ItemDto> getAllItemsWithOwnerId(int ownerId);

    List<ItemDto> findItemByText(String text);
}

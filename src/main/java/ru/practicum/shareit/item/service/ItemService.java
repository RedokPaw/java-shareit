package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item getItem(int id);

    Item createItem(int ownerId, Item item);

    Item updateItem(int itemId, ItemDto itemDto, int ownerId);

    Item deleteItem(int id);

    List<Item> getAllItems();

    List<Item> getAllItemsWithOwnerId(int ownerId);

    List<Item> findItemByText(String text);
}

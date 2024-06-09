package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {
    Item getItem(int id);

    Item createItem(Item item);

    Item updateItem(int itemId, ItemDto itemDto, int ownerId);

    Item deleteItem(int id);

    List<Item> findItemByText(String text);

    List<Item> getAllItems();

    List<Item> getAllItemsWithOwnerId(int ownerId);
}

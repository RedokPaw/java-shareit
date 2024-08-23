package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;

import java.util.List;

public interface ItemService {
    ItemDtoWithBookingsAndComments getItem(int id);

    ItemDto createItem(int ownerId, ItemDto itemDto);

    ItemDto updateItem(int itemId, ItemDto itemDto, int ownerId);

    void deleteItem(int id);

    List<ItemDto> getAllItems();

    List<ItemDtoWithBookingsAndComments> getAllItemsWithOwnerId(int ownerId);

    List<ItemDtoWithBookingsAndComments> findItemByDescription(String text);

    CommentDto createComment(CommentDto commentDto, int itemId, int ownerId);

}

package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;

import java.util.List;

import static java.lang.String.format;

//TODO Logs

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    ItemDao itemDao;
    UserDao userDao;

    @Autowired
    public ItemServiceImpl(ItemDao itemDao, UserDao userDao) {
        this.itemDao = itemDao;
        this.userDao = userDao;
    }

    @Override
    public Item getItem(int id) {
        return itemDao.getItem(id);
    }

    @Override
    public Item createItem(int ownerId, Item item) {
        validateItemOwner(ownerId);
        item.setOwnerId(ownerId);
        return itemDao.createItem(item);
    }

    @Override
    public Item updateItem(int itemId, ItemDto itemDto, int ownerId) {
        return itemDao.updateItem(itemId, itemDto, ownerId);
    }

    @Override
    public Item deleteItem(int id) {
        return itemDao.deleteItem(id);
    }

    @Override
    public List<Item> getAllItems() {
        return itemDao.getAllItems();
    }

    @Override
    public List<Item> getAllItemsWithOwnerId(int ownerId) {
        return itemDao.getAllItemsWithOwnerId(ownerId);
    }

    @Override
    public List<Item> findItemByText(String text) {
        return itemDao.findItemByText(text);
    }

    private void validateItemOwner(int ownerId) {
        if (userDao.getUser(ownerId) == null) {
            throw new ItemValidationException(format("Owner with id: %s does not exist", ownerId));
        }
    }
}

package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemOwnerMismatchException;
import ru.practicum.shareit.item.exception.ItemValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

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
    public ItemDto getItem(int id) {
        return ItemMapper.toDto(itemDao.getItem(id));
    }

    @Override
    public ItemDto createItem(int ownerId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        validateItemOwner(ownerId);
        item.setOwnerId(ownerId);
        return ItemMapper.toDto(itemDao.createItem(item));
    }

    @Override
    public ItemDto updateItem(int itemId, ItemDto itemDto, int ownerId) {
        Item oldItem = itemDao.getItem(itemId);
        if (itemId == 0 || oldItem == null) {
            throw new ItemNotFoundException("Item not found");
        }
        if (ownerId != oldItem.getOwnerId()) {
            throw new ItemOwnerMismatchException("Owner id mismatch");
        }
        if (itemDto.getName().isPresent()) {
            oldItem.setName(itemDto.getName().get());
        }
        if (itemDto.getDescription().isPresent()) {
            oldItem.setDescription(itemDto.getDescription().get());
        }
        if (itemDto.getAvailable().isPresent()) {
            oldItem.setAvailable(itemDto.getAvailable().get());
        }
        if (itemDto.getOwnerId().isPresent()) {
            oldItem.setOwnerId(itemDto.getOwnerId().get());
        }
        if (itemDto.getRequest().isPresent()) {
            oldItem.setRequest(itemDto.getRequest().get());
        }
        return ItemMapper.toDto(itemDao.updateItem(oldItem));
    }

    @Override
    public ItemDto deleteItem(int id) {
        return ItemMapper.toDto(itemDao.deleteItem(id));
    }

    @Override
    public List<ItemDto> getAllItems() {
        return itemDao.getAllItems().stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getAllItemsWithOwnerId(int ownerId) {
        return itemDao.getAllItemsWithOwnerId(ownerId).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findItemByText(String text) {
        return itemDao.findItemByText(text).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    private void validateItemOwner(int ownerId) {
        if (userDao.getUser(ownerId) == null) {
            throw new ItemValidationException(format("Owner with id: %s does not exist", ownerId));
        }
    }
}

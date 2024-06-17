package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Repository
@Slf4j
public class InMemoryItemStorage implements ItemDao {

    private final HashMap<Integer, Item> items = new HashMap<>();

    private Integer id = 1;

    @Override
    public Item getItem(int id) {
        Item item = items.get(id);
        if (item == null) {
            throw new ItemNotFoundException(format("item with id '%d' not found", id));
        }
        return item;
    }

    @Override
    public Item createItem(Item item) {
        item.setId(generateId());
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public Item deleteItem(int id) {
        return items.remove(id);
    }

    @Override
    public List<Item> getAllItems() {
        return new ArrayList<>(items.values());
    }

    @Override
    public List<Item> getAllItemsWithOwnerId(int ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId() == ownerId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findItemByText(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable())
                .collect(Collectors.toList());
    }

    private int generateId() {
        return this.id++;
    }

}

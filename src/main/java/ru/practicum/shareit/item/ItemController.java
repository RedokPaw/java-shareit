package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequestMapping("/items")
public class ItemController {

    ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") int ownerId, @Valid @RequestBody Item item) {
        log.info("Creating new item: {}", item);
        return ItemMapper.toDto(itemService.createItem(ownerId, item));
    }

    @PatchMapping(value = "/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") int ownerId, @RequestBody ItemDto itemDto, @PathVariable int id) {
        log.info("Updating item: {}", itemDto);
        return ItemMapper.toDto(itemService.updateItem(id, itemDto, ownerId));
    }

    @GetMapping(value = "/{id}")
    public ItemDto getItem(@PathVariable int id) {
        log.info("Retrieving item: {}", id);
        return ItemMapper.toDto(itemService.getItem(id));
    }

    @GetMapping
    public List<Item> getAllItems(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        log.info("Retrieving all items for ownerId: {}", ownerId);
        return itemService.getAllItemsWithOwnerId(ownerId);
    }

    @GetMapping(value = "/search")
    public List<Item> findItemByText(@RequestParam("text") String text) {
        log.info("Retrieving all items for text: {}", text);
        return itemService.findItemByText(text);
    }

}

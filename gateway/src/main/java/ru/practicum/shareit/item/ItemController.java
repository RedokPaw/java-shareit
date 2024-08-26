package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemValidationException;

import java.util.Collections;

import static java.lang.String.format;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/items")
@Slf4j
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestBody @Valid ItemDto itemDto) {
        log.info("[Gateway] request item create from user with id: = {}, item: {}", userId, itemDto.getName());
        if (itemDto.getName() == null ||itemDto.getName().isBlank() || itemDto.getDescription() == null ||
                itemDto.getDescription().isBlank()) {
            throw new ItemValidationException(format("Item name: %s or description %s is missing",
                    itemDto.getName(), itemDto.getDescription()));
        }
        return itemClient.createItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable("itemId") Long itemId,
                                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("[Gateway] user with id: id={} retrieved item with id = {}", userId, itemId);
        return itemClient.getItemById(itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable("itemId") Long itemId,
                                             @RequestBody ItemDto itemDto) {
        log.info("[Gateway] user with id={} updated item with id = {}", userId, itemId);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId) {

        log.info("[Gateway] request for getting all items from user: {}", userId);
        return itemClient.getAllItemsByOwnerId(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam("text") String text) {
        log.info("[Gateway] search items for user with id = {} text: {}", userId, text);
        return itemClient.searchItem(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createItemComment(@RequestBody final CommentDto commentShortDto,
                                                    @PathVariable final Long itemId,
                                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("[Gateway] User with id = {} added comment for item with id = {}", userId, itemId);
        return itemClient.addNewComment(commentShortDto, itemId, userId);
    }
}

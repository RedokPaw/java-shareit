package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
public class ItemDto {
    private int id;
    private Optional<String> name;
    private Optional<String> description;
    private Optional<Boolean> available;
    private Optional<Integer> ownerId;
    private Optional<String> request;
}

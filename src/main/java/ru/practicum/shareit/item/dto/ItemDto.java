package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
public class ItemDto {
    private int id;
    private Optional<@NotBlank String> name;
    private Optional<@NotBlank String> description;
    private Optional<@NotNull Boolean> available;
    private Optional<Integer> ownerId;
    private Optional<String> request;
}

package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
public class ItemDto {
    private int id;
    private Optional<@NotBlank String> name;
    private Optional<@NotBlank String> description;
    private Optional<@NotNull Boolean> available;
    private int requestId;

    public ItemDto() {
        name = Optional.empty();
        description = Optional.empty();
        available = Optional.empty();
    }
}

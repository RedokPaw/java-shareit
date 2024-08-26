package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    private UserRequestDto requester;

    private LocalDateTime created;

    private List<ItemDto> items;
}
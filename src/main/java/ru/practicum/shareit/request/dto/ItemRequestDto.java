package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

@Data
@Builder
@AllArgsConstructor
public class ItemRequestDto {
    private int id;
    private String description;
    private User requester;
}

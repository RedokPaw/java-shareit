package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserRequestDto {

    private int id;

    private String name;

    @Email
    private String email;

    public UserRequestDto() {
        name = "";
        email = "";
    }
}
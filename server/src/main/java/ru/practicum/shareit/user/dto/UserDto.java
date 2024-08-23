package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
@Builder
public class UserDto {

    private int id;

    private Optional<String> name;

    private Optional<@NotBlank @Email String> email;

    public UserDto() {
        name = Optional.empty();
        email = Optional.empty();
    }
}

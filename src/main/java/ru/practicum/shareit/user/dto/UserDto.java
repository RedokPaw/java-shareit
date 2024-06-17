package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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

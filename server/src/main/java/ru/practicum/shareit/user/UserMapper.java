package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(Optional.ofNullable(user.getName()))
                .email(Optional.ofNullable(user.getEmail()))
                .build();
    }

    public static User toUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        if (userDto.getEmail().isPresent()) {
            user.setEmail(userDto.getEmail().get());
        }
        if (userDto.getName().isPresent()) {
            user.setName(userDto.getName().get());
        }
        return user;
    }
}

package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getUser(int id);

    UserDto createUser(UserDto userDto);

    UserDto updateUser(int userId, UserDto userDto);

    UserDto deleteUser(int id);

    List<UserDto> getAllUsers();
}

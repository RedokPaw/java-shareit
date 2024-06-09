package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User getUser(int id);

    User createUser(User user);

    User updateUser(int userId, UserDto userDto);

    User deleteUser(int id);

    List<User> getAllUsers();
}

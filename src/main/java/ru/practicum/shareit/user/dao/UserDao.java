package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDao {
    User getUser(int id);

    User createUser(User user);

    User updateUser(int userId, User user);

    User deleteUser(int id);

    List<User> getAllUsers();
}

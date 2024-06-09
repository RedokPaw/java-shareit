package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.String.format;

@Repository
public class InMemoryUserStorage implements UserDao {

    private final HashMap<Integer, User> users = new HashMap<>();

    private Integer id = 1;

    @Override
    public User getUser(int id) {
        return users.get(id);
    }

    @Override
    public User createUser(User user) {
        validateEmail(user.getEmail());
        user.setId(generateId());
        users.put(user.getId(), user);
        return users.get(id);
    }

    @Override
    public User updateUser(int userId, UserDto userDto) {
        User oldUser = users.get(userId);
        if (userDto.getEmail().isPresent()) {
            String newEmail = userDto.getEmail().get();
            if (!oldUser.getEmail().equals(newEmail)) {
                validateEmail(newEmail);
            }
            oldUser.setEmail(userDto.getEmail().get());
        }
        if (userDto.getName().isPresent()) {
            oldUser.setName(userDto.getName().get());
        }
        return users.get(userId);
    }

    @Override
    public User deleteUser(int id) {
        return users.remove(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private int generateId() {
        return this.id++;
    }

    private void validateEmail(String email) {
        boolean isEmailAlreadyExists = users.values().stream()
                .anyMatch(user1 -> user1.getEmail().equals(email));
        if (isEmailAlreadyExists) {
            throw new UserValidationException(format("User with email %s already exists", email));
        }
    }
}

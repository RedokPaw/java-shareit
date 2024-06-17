package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.exception.UserValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class UserServiceImpl implements UserService {

    UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDto getUser(int id) {
        return UserMapper.toUserDto(userDao.getUser(id));
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        return UserMapper.toUserDto(userDao.createUser(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto updateUser(int userId, UserDto userDto) {
        User oldUser = userDao.getUser(userId);
        if (oldUser == null) {
            throw new UserNotFoundException(format("User with id %s not found", userId));
        }
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
        return UserMapper.toUserDto(userDao.updateUser(userId, oldUser));
    }

    @Override
    public UserDto deleteUser(int id) {
        return UserMapper.toUserDto(userDao.deleteUser(id));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userDao.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    private void validateEmail(String email) {
        boolean isEmailAlreadyExists = userDao.getAllUsers().stream()
                .anyMatch(user1 -> user1.getEmail().equals(email));
        if (isEmailAlreadyExists) {
            throw new UserValidationException(format("User with email %s already exists", email));
        }
    }
}

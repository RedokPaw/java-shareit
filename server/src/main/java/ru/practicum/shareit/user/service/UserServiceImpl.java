package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto getUser(int id) {
        return UserMapper.toUserDto(userRepository.getReferenceById(id));
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto updateUser(int userId, UserDto userDto) {
        User oldUser = userRepository.getReferenceById(userId);
        if (userDto.getEmail().isPresent() && !userDto.getEmail().get().isBlank()) {
            oldUser.setEmail(userDto.getEmail().get());
        }
        if (userDto.getName().isPresent() && !userDto.getName().get().isBlank()) {
            oldUser.setName(userDto.getName().get());
        }
        return UserMapper.toUserDto(userRepository.save(oldUser));
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

}

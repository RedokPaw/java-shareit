package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody User user) {
        log.info("Create user: {}", user);
        return UserMapper.toUserDto(userService.createUser(user));
    }

    @GetMapping(value = "/{userId}")
    public UserDto getUser(@PathVariable int userId) {
        log.info("Get user: {}", userId);
        return UserMapper.toUserDto(userService.getUser(userId));
    }

    @PatchMapping(value = "/{userId}")
    public UserDto updateUser(@PathVariable int userId, @RequestBody User user) {
        log.info("Update user: {}", user);
        return UserMapper.toUserDto(userService.updateUser(userId, UserMapper.toUserDto(user)));
    }

    @DeleteMapping(value = "/{userId}")
    public UserDto deleteUser(@Valid @PathVariable int userId) {
        log.info("Delete user: {}", userId);
        return UserMapper.toUserDto(userService.deleteUser(userId));
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

}

package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.exception.UserValidationException;

import static java.lang.String.format;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Gateway get all users request");
        return userClient.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        log.info("Gateway get user by id: {}", userId);
        return userClient.getUserById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        log.info("Gateway request for create user: {}", userRequestDto);
        if (userRequestDto.getName() == null || userRequestDto.getName().isBlank() || userRequestDto.getEmail() == null ||
                userRequestDto.getEmail().isBlank()) {
            throw new UserValidationException(format("user name: %s or user email: %s is empty",
                    userRequestDto.getName(), userRequestDto.getEmail()));
        }
        return userClient.createUser(userRequestDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@RequestBody UserRequestDto userRequestDto,
                                             @PathVariable Long userId) {
        log.info("Gateway request for update user with id: {}", userId);
        return userClient.updateUser(userRequestDto, userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        log.info("Gateway delete request user with id: {}", userId);
        return userClient.deleteUser(userId);
    }
}

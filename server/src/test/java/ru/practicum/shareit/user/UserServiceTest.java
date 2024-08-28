package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private User user;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(0)
                .name("name")
                .email("email@tut.ya")
                .build();
        userDto = UserDto.builder()
                .id(user.getId())
                .name(Optional.of(user.getName()))
                .email(Optional.of(user.getEmail()))
                .build();
    }


    @Test
    public void getUserShouldReturnUser() {
        when(userRepository.getReferenceById(anyInt())).thenReturn(user);

        UserDto result = userService.getUser(anyInt());
        Assertions.assertEquals(userDto, result);
    }

    @Test
    public void createUserShouldReturnNewUser() {
        when(userRepository.save(any())).thenReturn(user);

        UserDto result = userService.createUser(userDto);
        Assertions.assertEquals(userDto, result);
    }

    @Test
    public void updateUserShouldReturnUpdatedUser() {
        when(userRepository.getReferenceById(anyInt())).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);

        UserDto result = userService.updateUser(1, userDto);
        Assertions.assertEquals(userDto, result);
    }

    @Test
    public void deleteUserShouldRunWithoutExceptions() {
        Assertions.assertDoesNotThrow(() -> userService.deleteUser(anyInt()));
    }

    @Test
    public void getAllUsersShouldReturnListOfUserDto() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> result = userService.getAllUsers();

        Assertions.assertFalse(result.isEmpty());
    }
}

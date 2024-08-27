package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureDataJpa
public class UserControllerTest {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userDto = UserDto.builder()
                .id(0)
                .name(Optional.of("name"))
                .email(Optional.of("email@tut.ya"))
                .build();
    }

    @Test
    public void createUserShouldReturnNewUser() throws Exception {
        when(userService.createUser(any())).thenReturn(userDto);
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(userDto.getName().get()), String.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail().get()), String.class));

    }

    @Test
    public void getUserShouldReturnUser() throws Exception {
        when(userService.getUser(anyInt())).thenReturn(userDto);
        mvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(userDto.getName().get()), String.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail().get()), String.class));

    }

    @Test
    public void updateUserShouldReturnUser() throws Exception {
        when(userService.updateUser(anyInt(), any())).thenReturn(userDto);
        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(userDto.getName().get()), String.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail().get()), String.class));

    }

    @Test
    public void deleteUserShouldReturnStatusOk() throws Exception {
        when(userService.updateUser(anyInt(), any())).thenReturn(userDto);
        mvc.perform(delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllUsersShouldReturnUser() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(userDto));
        mvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(userDto.getId()), Integer.class))
                .andExpect(jsonPath("$.[0].name", is(userDto.getName().get()), String.class))
                .andExpect(jsonPath("$.[0].email", is(userDto.getEmail().get()), String.class));

    }
}

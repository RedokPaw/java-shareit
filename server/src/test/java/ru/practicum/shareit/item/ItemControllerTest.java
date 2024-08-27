package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureDataJpa
public class ItemControllerTest {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemService itemService;

    private ItemDto itemDto;

    private ItemDtoWithBookingsAndComments itemDtoWithBookingsAndComments;

    @BeforeEach
    void setUp() {
        itemDto = ItemDto.builder()
                .id(0)
                .name(Optional.of("item"))
                .description(Optional.of("description"))
                .available(Optional.of(true))
                .build();
        itemDtoWithBookingsAndComments = ItemDtoWithBookingsAndComments.builder()
                .id(0)
                .name("item")
                .description("description")
                .available(true)
                .build();
    }

    @Test
    public void createItemShouldReturnItemDtoAndStatusOk() throws Exception {
        Mockito.when(itemService.createItem(anyInt(), any())).thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName().get()), String.class));
    }

    @Test
    public void updateItemShouldReturnItemDtoAndStatusOk() throws Exception {
        Mockito.when(itemService.updateItem(anyInt(), any(), anyInt())).thenReturn(itemDto);

        mvc.perform(patch("/items/3")
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName().get()), String.class));
    }

    @Test
    public void getItemShouldReturnItemDtoAndStatusOk() throws Exception {
        Mockito.when(itemService.getItem(anyInt())).thenReturn(itemDtoWithBookingsAndComments);

        mvc.perform(get("/items/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoWithBookingsAndComments.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemDtoWithBookingsAndComments.getName()), String.class));
    }

    @Test
    public void getAllItemShouldReturnItemDtoListAndStatusOk() throws Exception {
        Mockito.when(itemService.getAllItemsWithOwnerId(anyInt())).thenReturn(List.of(itemDtoWithBookingsAndComments));

        mvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemDtoWithBookingsAndComments.getId()), Integer.class))
                .andExpect(jsonPath("$.[0].name", is(itemDtoWithBookingsAndComments.getName()), String.class));
    }

    @Test
    public void findItemsByTextShouldReturnItemDtoListAndStatusOk() throws Exception {
        Mockito.when(itemService.findItemByDescription(anyString())).thenReturn(List.of(itemDtoWithBookingsAndComments));

        mvc.perform(get("/items/search")
                        .param("text", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemDtoWithBookingsAndComments.getId()), Integer.class))
                .andExpect(jsonPath("$.[0].name", is(itemDtoWithBookingsAndComments.getName()), String.class));
    }
}

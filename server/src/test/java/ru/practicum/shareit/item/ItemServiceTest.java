package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;
import ru.practicum.shareit.item.exception.CommentForbiddenBooking;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemOwnerMismatchException;
import ru.practicum.shareit.item.exception.ItemValidationException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @InjectMocks
    private ItemServiceImpl itemService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;

    private User user;
    private Item item;
    private ItemDto itemDto;
    private ItemDtoWithBookingsAndComments itemDtoWithBookingsAndComments;
    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(2)
                .name("Owner")
                .email("owner@example.com")
                .build();
        item = Item.builder()
                .id(1)
                .name("item")
                .description("dec")
                .available(true)
                .owner(user)
                .build();
        itemDto = ItemDto.builder()
                .id(item.getId())
                .name(Optional.of(item.getName()))
                .description(Optional.of(item.getDescription()))
                .available(Optional.of(true))
                .build();
        itemDtoWithBookingsAndComments = ItemDtoWithBookingsAndComments.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .comments(List.of())
                .available(true)
                .build();
        comment = Comment.builder()
                .id(0)
                .item(item)
                .text("comment")
                .author(user)
                .build();
        commentDto = CommentMapper.toCommentDto(comment);
    }

    @Test
    public void getItemShouldReturnItemDto() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));

        ItemDtoWithBookingsAndComments result = itemService.getItem(anyInt());

        assertEquals(itemDtoWithBookingsAndComments, result);
    }

    @Test
    public void getItemShouldThrowWhenItemNotFound() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemService.getItem(anyInt()));
    }

    @Test
    public void createItemShouldReturnItemDto() {
        when(itemRepository.save(any())).thenReturn(item);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        ItemDto result = itemService.createItem(user.getId(), itemDto);

        assertEquals(itemDto, result);
    }

    @Test
    public void createItemShouldThrowWhenOwnerDoesNotExist() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(ItemValidationException.class, () -> itemService.createItem(1, itemDto));
    }

    @Test
    public void updateItemShouldReturnItemDto() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).thenReturn(item);
        ItemDto result = itemService.updateItem(item.getId(), itemDto, user.getId());

        assertEquals(itemDto, result);
    }

    @Test
    public void updateItemShouldThrowWhenOwnerIdMismatching() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));

        assertThrows(ItemOwnerMismatchException.class,
                () -> itemService.updateItem(item.getId(), itemDto, user.getId() + 5));
    }

    @Test
    public void updateItemShouldThrowWhenItemIdIsZero() {

        assertThrows(ItemNotFoundException.class,
                () -> itemService.updateItem(0, itemDto, user.getId() + 5));
    }

    @Test
    public void deleteItemShouldRunWithoutExceptions() {
        assertDoesNotThrow(() -> itemService.deleteItem(anyInt()));
    }

    @Test
    public void getALlItemsShouldReturnListOfItemDto() {
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<ItemDto> result = itemService.getAllItems();

        assertNotNull(result);
    }

    @Test
    public void getAllItemsWithOwnerIdShouldReturnListOfItemDto() {
        when(itemRepository.findAllByOwner_Id(anyInt())).thenReturn(List.of(item));

        List<ItemDtoWithBookingsAndComments> result = itemService.getAllItemsWithOwnerId(anyInt());

        assertNotNull(result);
    }

    @Test
    public void findItemByDescriptionShouldReturnListOfItemDto() {
        when(itemRepository.findAllByDescriptionContaining(anyString())).thenReturn(List.of(item));

        List<ItemDtoWithBookingsAndComments> result = itemService.findItemByDescription(anyString());

        assertNotNull(result);
    }

    @Test
    public void createCommentShouldReturnNewComment() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findPastBookingsByBookerIdAndItem(anyInt(), anyInt(), any()))
                .thenReturn(List.of(new Booking()));
        CommentDto result = itemService.createComment(commentDto, item.getId(), user.getId());

        assertEquals(commentDto, result);
    }

    @Test
    public void createCommentShouldThrowWhenBookingIsMissing() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findPastBookingsByBookerIdAndItem(anyInt(), anyInt(), any()))
                .thenReturn(List.of());
        assertThrows(CommentForbiddenBooking.class, () -> itemService.createComment(commentDto, item.getId(), user.getId()));
    }


}

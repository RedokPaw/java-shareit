package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;

    private User booker;
    private User owner;
    private Item item;
    private BookingDto bookingRequestDto;
    private Booking booking;
    private BookingDto bookingResponseDto;


    @BeforeEach
    void setUp() {
        booker = User.builder().id(1).name("Booker").email("booker@example.com").build();
        owner = User.builder().id(2).name("Owner").email("owner@example.com").build();
        item = Item.builder().id(1).name("Item").description("Description").available(true).owner(owner).build();
        bookingRequestDto = BookingDto.builder()
                .start(Optional.of(LocalDateTime.now().plusDays(1)))
                .end(Optional.of(LocalDateTime.now().plusDays(2)))
                .itemId(item.getId())
                .build();
        booking = Booking.builder()
                .id(1)
                .start(bookingRequestDto.getStart().get())
                .end(bookingRequestDto.getEnd().get())
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();
        bookingResponseDto = BookingDto.builder()
                .id(booking.getId())
                .start(Optional.of(LocalDateTime.now().plusDays(1)))
                .end(Optional.of(LocalDateTime.now().plusDays(2)))
                .item(ItemMapper.toDto(item)).booker(UserMapper.toUserDto(booker))
                .status(BookingStatus.WAITING)
                .build();
    }


    @Test
    public void createBookingShouldReturnNewBooking() {

    }

}

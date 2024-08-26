package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

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

    private User user;
    private Item item;
    private BookingDto bookingRequestDto;
    private Booking booking;
    private BookingDto bookingResultDto;


    @BeforeEach
    void setUp() {
        user = User.builder().id(2).name("Owner").email("owner@example.com").build();
        item = Item.builder().id(1).name("Item").description("description").available(true).owner(user).build();
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
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();
        bookingResultDto = BookingDto.builder()
                .id(1)
                .start(Optional.of(LocalDateTime.now().plusDays(1)))
                .end(Optional.of(LocalDateTime.now().plusDays(2)))
                .item(ItemMapper.toDto(item))
                .booker(UserMapper.toUserDto(user))
                .status(BookingStatus.WAITING)
                .build();
    }


    @Test
    public void createBookingShouldReturnNewBooking() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        BookingDto result = bookingService.createBooking(bookingRequestDto, anyInt());

        assertEquals(bookingResultDto.getStatus(), result.getStatus());
        assertEquals(bookingResultDto.getStart().get(), result.getStart().get());
        assertEquals(bookingResultDto.getEnd().get(), result.getEnd().get());
    }

    @Test
    public void approveBookingShouldReturnApprovedBookingWhenIsApprovedTrue() {
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.of(booking));
        BookingDto result = bookingService.approveBooking(1, 2, true);

        assertEquals(result.getStatus(), BookingStatus.APPROVED);
    }
    @Test
    public void approveBookingShouldReturnRejectedBookingWhenIsApprovedFalse() {
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.of(booking));
        BookingDto result = bookingService.approveBooking(1, 2, false);

        assertEquals(result.getStatus(), BookingStatus.REJECTED);
    }
    @Test
    public void getBookingShouldReturnBooking() {
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.of(booking));
        BookingDto result = bookingService.getBooking(1, 2);

        assertEquals(bookingResultDto.getId(), result.getId());
        assertEquals(bookingResultDto.getStatus(), result.getStatus());
        assertEquals(bookingResultDto.getStart().get(), result.getStart().get());
        assertEquals(bookingResultDto.getEnd().get(), result.getEnd().get());
    }

    @Test
    public void getAllBookingsShouldReturnListOfBookingsDto() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findCurrentBookingsByBookerId(anyInt(), any())).thenReturn(List.of(booking));
        List<BookingDto> result = bookingService.getAllBookings(1, BookingState.CURRENT);

        assertNotNull(result);

    }
    @Test
    public void getAllBookingsByItemOwnerShouldReturnListOfBookingsDto() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findCurrentBookingsByItemOwner(anyInt(), any())).thenReturn(List.of(booking));
        List<BookingDto> result = bookingService.getAllBookingsByItemOwner(1, BookingState.CURRENT);

        assertNotNull(result);
    }



}

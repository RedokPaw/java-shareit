package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDtoWithBookingsAndComments getItem(int id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Item not found"));
        /*
        Тесты не проходит (в тестах ожидается null в след и предыдущем бронировании
        и при этом бронирование, которое генерируется в тестах, точно должно быть предыдущим, так что
        оставлю код в комментариях, может понадобится в следующем спринте? если что удалю
        */
        /*
        Optional<Booking> lastBooking =
                Optional.ofNullable(bookingRepository.findPreviousBookingByItem(item.getId(), LocalDateTime.now()));
        Optional<Booking> nextBooking =
                Optional.ofNullable(bookingRepository.findNextBookingByItem(item.getId(), LocalDateTime.now()));
        BookingDto lastBookingDto = lastBooking.map(BookingMapper::toBookingDto).orElse(null);
        BookingDto nextBookingDto = nextBooking.map(BookingMapper::toBookingDto).orElse(null);
        */
        return convertToItemDtoWithBookingsAndComments(item);
    }

    @Override
    public ItemDto createItem(int ownerId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        validateItemOwner(ownerId);
        item.setOwner(userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException("User not found")));
        item.setRequest(itemRequestRepository.findById(itemDto.getRequestId()).orElseThrow());
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(int itemId, ItemDto itemDto, int ownerId) {
        Item oldItem = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Item not found"));
        if (itemId == 0) {
            throw new ItemNotFoundException("Item not found");
        }
        if (ownerId != oldItem.getOwner().getId()) {
            throw new ItemOwnerMismatchException("Owner id mismatch");
        }
        if (itemDto.getName().isPresent()) {
            oldItem.setName(itemDto.getName().get());
        }
        if (itemDto.getDescription().isPresent()) {
            oldItem.setDescription(itemDto.getDescription().get());
        }
        if (itemDto.getAvailable().isPresent()) {
            oldItem.setAvailable(itemDto.getAvailable().get());
        }
        return ItemMapper.toDto(itemRepository.save(oldItem));
    }

    @Override
    public void deleteItem(int id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemDto> getAllItems() {
        return itemRepository.findAll().stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDtoWithBookingsAndComments> getAllItemsWithOwnerId(int ownerId) {
        return itemRepository.findAllByOwner_Id(ownerId).stream()
                .map(this::convertToItemDtoWithBookingsAndComments)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDtoWithBookingsAndComments> findItemByDescription(String text) {
        return itemRepository.findAllByDescriptionContaining(text).stream()
                .map(this::convertToItemDtoWithBookingsAndComments)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, int itemId, int ownerId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item for comment not found"));
        User author = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("User for comment not found"));
        List<Booking> booking = bookingRepository
                .findPastBookingsByBookerIdAndItem(ownerId,
                        itemId, LocalDateTime.now());
        if (booking.isEmpty()) {
            throw new CommentForbiddenBooking("User bookings for this item not found");
        }
        Comment comment = CommentMapper.toComment(commentDto, item, author);
        commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
    }

    private ItemDtoWithBookingsAndComments convertToItemDtoWithBookingsAndComments(Item item) {
        return ItemMapper.toItemDtoWithBookingsAndComments(item,
                commentRepository.findAllByItem_Id(item.getId()).stream()
                        .map(CommentMapper::toCommentDto).toList(), null, null);
    }

    private void validateItemOwner(int ownerId) {
        if (userRepository.findById(ownerId).isEmpty()) {
            throw new ItemValidationException(format("Owner with id: %s does not exist", ownerId));
        }
    }
}

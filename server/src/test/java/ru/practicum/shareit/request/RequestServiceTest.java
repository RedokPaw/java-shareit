package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithAnswer;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private ItemRepository itemRepository;

    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;
    private ItemRequestDtoWithAnswer itemRequestDtoWithAnswer;

    @BeforeEach
    void setUp() {
        itemRequest = ItemRequest.builder()
                .id(0)
                .description("desc")
                .requester(new User())
                .build();
        itemRequestDto = ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requester(itemRequest.getRequester())
                .build();
        itemRequestDtoWithAnswer = ItemRequestDtoWithAnswer.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
    }

    @Test
    public void createRequestItemShouldReturnNewRequest() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(itemRequest.getRequester()));
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);

        ItemRequestDto result = itemRequestService.createRequestItem(itemRequestDto, 0);
        assertEquals(itemRequestDto.getId(), result.getId());
        assertEquals(itemRequestDto.getDescription(), result.getDescription());
        assertEquals(itemRequestDto.getRequester(), result.getRequester());
    }

    @Test
    public void getRequestByRequestIdShouldReturnRequest() {
        Item itemForAnswer = new Item();
        itemForAnswer.setId(1);
        itemForAnswer.setRequest(itemRequest);

        when(itemRequestRepository.findById(anyInt())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequest_Id(anyInt())).thenReturn(List.of(itemForAnswer));

        ItemRequestDtoWithAnswer result = itemRequestService.getRequestByRequestId(anyInt());
        assertEquals(itemRequestDto.getId(), result.getId());
        assertEquals(itemRequestDto.getDescription(), result.getDescription());
        assertEquals(ItemMapper.toItemAnswerDto(itemForAnswer), result.getItems().getFirst());
    }

    @Test
    public void getAllRequestsByRequestIdShouldReturnRequest() {
        Item itemForAnswer = new Item();
        itemForAnswer.setId(1);
        itemForAnswer.setRequest(itemRequest);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(itemRequest.getRequester()));
        when(itemRequestRepository.getAllByRequester_Id(anyInt())).thenReturn(List.of(itemRequest));
        when(itemRepository.findAllByRequest_IdIn(any())).thenReturn(List.of(itemForAnswer));

        List<ItemRequestDtoWithAnswer> result = itemRequestService.getAllRequestsByRequesterId(anyInt());
        assertEquals(itemRequestDto.getId(), result.getFirst().getId());
        assertEquals(itemRequestDto.getDescription(), result.getFirst().getDescription());
        assertEquals(ItemMapper.toItemAnswerDto(itemForAnswer), result.getFirst().getItems().getFirst());
    }

    @Test
    public void getAllRequestsShouldReturnList() {
        when(itemRequestRepository.getAllByRequesterIdNot(anyInt())).thenReturn(List.of(itemRequest));

        List<ItemRequestDto> result = itemRequestService.getAllRequests(anyInt());

        assertEquals(itemRequestDto.getId(), result.getFirst().getId());
        assertEquals(itemRequestDto.getDescription(), result.getFirst().getDescription());
    }
}

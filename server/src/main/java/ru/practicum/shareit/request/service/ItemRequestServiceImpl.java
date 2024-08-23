package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemAnswerDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithAnswer;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto createRequestItem(ItemRequestDto itemRequestDto, int userId) {
        User user = getUserById(userId);

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, user);
        itemRequest.setCreated(LocalDateTime.now());
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public ItemRequestDtoWithAnswer getRequestByRequestId(int requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow();

        return ItemRequestMapper.toItemRequestDtoWithAnswer(itemRequest,
                getAnswersForRequest(itemRequest.getId()));
    }

    @Override
    public List<ItemRequestDtoWithAnswer> getAllRequestsByRequesterId(int userId) {
        getUserById(userId);

        return itemRequestRepository.getAllByRequester_Id(userId).stream()
                .map(itemRequest -> ItemRequestMapper
                        .toItemRequestDtoWithAnswer(itemRequest, getAnswersForRequest(itemRequest.getId())))
                .toList();
    }

    @Override
    public List<ItemRequestDto> getAllRequests(int userId) {
        return itemRequestRepository.getAllByRequesterIdNot(userId).stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();
    }

    private User getUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: + " + userId));
    }

    private List<ItemAnswerDto> getAnswersForRequest(int requestId) {
        return itemRepository.findAllByRequest_Id(requestId).stream()
                .map(ItemMapper::toItemAnswerDto)
                .toList();
    }
}

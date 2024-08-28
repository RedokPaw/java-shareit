package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemAnswerDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithAnswer;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("itemRequest not found with id: " + requestId));

        return ItemRequestMapper.toItemRequestDtoWithAnswer(itemRequest,
                getAnswersForRequest(itemRequest.getId()));
    }

    @Override
    @Cacheable(value = "requestWithAnswers", key = "#userId")
    public List<ItemRequestDtoWithAnswer> getAllRequestsByRequesterId(int userId) {
        getUserById(userId);
        List<ItemRequestDtoWithAnswer> itemRequestsWithAnswers = new ArrayList<>();
        List<ItemRequest> itemRequests = itemRequestRepository.getAllByRequester_Id(userId);
        List<Item> answersList = itemRepository.findAllByRequest_IdIn(itemRequests.stream()
                .map(ItemRequest::getId)
                .toList());
        itemRequests
                .forEach(request -> itemRequestsWithAnswers.add(ItemRequestMapper
                        .toItemRequestDtoWithAnswer(request, answersList
                                .stream()
                                .filter(answer -> answer.getRequest().getId() == request.getId())
                                .map(ItemMapper::toItemAnswerDto)
                                .toList())));

        return itemRequestsWithAnswers;
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

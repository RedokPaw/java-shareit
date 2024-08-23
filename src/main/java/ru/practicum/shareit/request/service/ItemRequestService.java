package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithAnswer;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createRequestItem(ItemRequestDto itemRequestDto, int userId);

    ItemRequestDtoWithAnswer getRequestByRequestId(int userId);

    List<ItemRequestDtoWithAnswer> getAllRequestsByRequesterId(int userId);

    List<ItemRequestDto> getAllRequests(int userId);
}

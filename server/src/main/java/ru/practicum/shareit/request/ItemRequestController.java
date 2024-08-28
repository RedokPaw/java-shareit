package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithAnswer;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestBody ItemRequestDto itemRequestDto,
                                            @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("request for request create: {}", itemRequestDto);
        return itemRequestService.createRequestItem(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDtoWithAnswer> getItemRequestsForUserWithAnswers(
            @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("request to get user request with answers for userid: {}", userId);
        return itemRequestService.getAllRequestsByRequesterId(userId);
    }

    @GetMapping(value = "/{requestId}")
    public ItemRequestDtoWithAnswer getRequestWithAnswerById(@PathVariable int requestId) {
        log.info("get request with anser by id: {}", requestId);
        return itemRequestService.getRequestByRequestId(requestId);
    }

    @GetMapping(value = "/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("get all request for user: {}", userId);
        return itemRequestService.getAllRequests(userId);
    }

}

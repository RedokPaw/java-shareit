package ru.practicum.shareit.error;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private LocalDateTime errorTime;
}

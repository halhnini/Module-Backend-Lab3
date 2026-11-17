package com.example.productapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(
        Long id,

        LocalDateTime orderDate,

        @NotEmpty(message = "La commande doit contenir au moins un article")
        List<@Valid OrderItemDto> items
) {
}

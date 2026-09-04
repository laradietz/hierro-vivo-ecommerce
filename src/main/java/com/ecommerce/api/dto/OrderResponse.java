package com.ecommerce.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
    UUID id,
    String status,
    BigDecimal totalAmount,
    String notes,
    LocalDate estimatedReadyDate,
    List<OrderItemResponse> items,
    LocalDateTime createdAt
) {}

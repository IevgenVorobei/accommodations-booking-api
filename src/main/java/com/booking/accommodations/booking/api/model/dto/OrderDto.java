package com.booking.accommodations.booking.api.model.dto;

import com.booking.accommodations.booking.api.model.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderDto(UUID orderId, LocalDate checkIn, LocalDate checkOut, BigDecimal totalPrice, OrderStatus status) {

}

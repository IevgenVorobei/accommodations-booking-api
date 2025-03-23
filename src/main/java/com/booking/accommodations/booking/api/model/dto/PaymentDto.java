package com.booking.accommodations.booking.api.model.dto;

import com.booking.accommodations.booking.api.model.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentDto(BigDecimal amount, String currency, PaymentStatus paymentStatus) {
}

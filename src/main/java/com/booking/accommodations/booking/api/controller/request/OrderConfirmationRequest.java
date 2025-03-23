package com.booking.accommodations.booking.api.controller.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderConfirmationRequest(@NotNull BigDecimal paymentAmount, @NotNull String currency) {
}

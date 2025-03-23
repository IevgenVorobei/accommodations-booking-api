package com.booking.accommodations.booking.api.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PriceDto(BigDecimal pricePerNight, BigDecimal totalUnitPrice, BigDecimal systemCommission, BigDecimal totalPriceWithCommission) {
}

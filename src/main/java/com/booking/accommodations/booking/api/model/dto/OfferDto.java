package com.booking.accommodations.booking.api.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record OfferDto(UnitDto unit, LocalDate checkIn, LocalDate checkOut, PriceDto priceDetails) {
}

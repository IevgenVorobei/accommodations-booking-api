package com.booking.accommodations.booking.api.controller.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SearchRequest(@Size(min = 2, message = "Size of query 2 symbols at least") String query,
                            @NotNull @FutureOrPresent LocalDate checkIn,
                            @NotNull @Future LocalDate checkOut,
                            @Positive(message = "minPricePerNight should be positive value") BigDecimal minPricePerNight,
                            @Positive(message = "maxPricePerNight should be positive value") BigDecimal maxPricePerNight) {
}

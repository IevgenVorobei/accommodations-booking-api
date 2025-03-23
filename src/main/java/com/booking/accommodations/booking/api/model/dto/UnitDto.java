package com.booking.accommodations.booking.api.model.dto;


import com.booking.accommodations.booking.api.model.AccommodationType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UnitDto(@NotNull Integer numOfRooms,
                      @NotNull AccommodationType accommodationType,
                      @NotNull
                      Integer floor,
                      @NotNull
                      BigDecimal pricePerNight,
                      String description) {
}

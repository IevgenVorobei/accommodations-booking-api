package com.booking.accommodations.booking.api.controller.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequest {

    @NotNull
    private UUID unitId;
    @NotNull
    private LocalDate checkIn;
    @NotNull
    private LocalDate checkOut;
}

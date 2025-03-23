package com.booking.accommodations.booking.api.controller.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CancellationRequest(String reason) {
}

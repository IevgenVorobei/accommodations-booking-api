package com.booking.accommodations.booking.api.service;

import com.booking.accommodations.booking.api.controller.request.OrderConfirmationRequest;
import com.booking.accommodations.booking.api.model.dto.PaymentDto;

public interface PaymentService {

    PaymentDto createPayment(OrderConfirmationRequest confirmationRequest);
}

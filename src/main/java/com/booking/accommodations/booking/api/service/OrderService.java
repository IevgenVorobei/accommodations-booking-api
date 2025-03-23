package com.booking.accommodations.booking.api.service;

import com.booking.accommodations.booking.api.controller.request.OrderConfirmationRequest;
import com.booking.accommodations.booking.api.controller.request.OrderRequest;
import com.booking.accommodations.booking.api.model.dto.OrderDto;

import java.util.UUID;

public interface OrderService {

    OrderDto createOrder(OrderRequest orderRequest);

    OrderDto confirmOrder(UUID orderId, OrderConfirmationRequest confirmationRequest);

    OrderDto cancelOrder(UUID orderId, String reason);
}

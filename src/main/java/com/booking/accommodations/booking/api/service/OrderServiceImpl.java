package com.booking.accommodations.booking.api.service;

import com.booking.accommodations.booking.api.controller.request.OrderConfirmationRequest;
import com.booking.accommodations.booking.api.controller.request.OrderRequest;
import com.booking.accommodations.booking.api.exceptions.OrderConfirmationFailedException;
import com.booking.accommodations.booking.api.exceptions.ResourceNotFoundException;
import com.booking.accommodations.booking.api.model.Order;
import com.booking.accommodations.booking.api.model.OrderStatus;
import com.booking.accommodations.booking.api.model.PaymentStatus;
import com.booking.accommodations.booking.api.model.Unit;
import com.booking.accommodations.booking.api.model.dto.OrderDto;
import com.booking.accommodations.booking.api.model.dto.PaymentDto;
import com.booking.accommodations.booking.api.repository.OrderRepository;
import com.booking.accommodations.booking.api.tasks.ordercancellation.OrderCancellationTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Value("${unconfirmed.order.ttl.mins}")
    private Integer unconfirmedOrdersTtlMins;
    private UnitService unitService;
    private PaymentService paymentService;
    private OrderRepository orderRepository;

    public OrderServiceImpl(UnitService unitService, PaymentService paymentService, OrderRepository orderRepository) {
        this.unitService = unitService;
        this.paymentService = paymentService;
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderDto createOrder(OrderRequest orderRequest) {
        Unit unit = unitService.findById(orderRequest.getUnitId());
        Order order = Order.builder()
                           .unit(unit)
                           .checkInDate(orderRequest.getCheckIn())
                           .checkOutDate(orderRequest.getCheckOut())
                           .orderStatus(OrderStatus.CREATED)
                           .totalPrice(estimateTotalPrice(unit.getPricePerNight(), orderRequest.getCheckIn(), orderRequest.getCheckOut()))
                           .build();
        runCancelUnConfirmedOrderTask(order.getId());
        return toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto confirmOrder(UUID orderId, OrderConfirmationRequest confirmationRequest) {
        PaymentDto paymentDto = paymentService.createPayment(confirmationRequest);
        if (PaymentStatus.DECLINED.equals(paymentDto.paymentStatus())) {
            throw new OrderConfirmationFailedException(orderId.toString(), "Payment was declined");
        }

        Order orderToBeConfirmed = orderRepository.getReferenceById(orderId);

        if (OrderStatus.CANCELLED.equals(orderToBeConfirmed.getOrderStatus())) {
            throw new OrderConfirmationFailedException(orderId.toString(), "Cancelled order cannot be confirmed");
        }
        orderToBeConfirmed.setOrderStatus(OrderStatus.CONFIRMED);
        return toDto(orderRepository.save(orderToBeConfirmed));
    }

    @Override
    public OrderDto cancelOrder(UUID orderId, String reason) {
        Order orderToCancel = orderRepository.findById(orderId)
                                             .orElseThrow(() -> new ResourceNotFoundException("order", orderId.toString()));
        orderToCancel.setOrderStatus(OrderStatus.CANCELLED);
        orderToCancel.setCancellationReason(reason);
        return toDto(orderRepository.save(orderToCancel));
    }

    private void runCancelUnConfirmedOrderTask(UUID orderId) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        Runnable orderCancellationTask = new OrderCancellationTask(orderId, orderRepository);
        executorService.schedule(orderCancellationTask, unconfirmedOrdersTtlMins, TimeUnit.MINUTES);
        executorService.shutdown();
    }

    private BigDecimal estimateTotalPrice(BigDecimal pricePerNight, LocalDate checkIn, LocalDate checkOut) {
        long daysBetween = ChronoUnit.DAYS.between(checkIn, checkOut);
        return pricePerNight.multiply(BigDecimal.valueOf(daysBetween));
    }

    private OrderDto toDto(Order order) {
        return OrderDto.builder()
                       .orderId(order.getId())
                       .checkIn(order.getCheckInDate())
                       .checkOut(order.getCheckOutDate())
                       .totalPrice(order.getTotalPrice())
                       .status(order.getOrderStatus())
                       .build();
    }
}

package com.booking.accommodations.booking.api.tasks.ordercancellation;

import com.booking.accommodations.booking.api.model.Order;
import com.booking.accommodations.booking.api.model.OrderStatus;
import com.booking.accommodations.booking.api.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class OrderCancellationTask implements Runnable {

    private UUID orderIdToBeCancelled;
    private OrderRepository orderRepository;

    public OrderCancellationTask(UUID orderIdToBeCancelled, OrderRepository orderRepository) {
        this.orderIdToBeCancelled = orderIdToBeCancelled;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run() {
        Order orderToCancel = orderRepository.findById(orderIdToBeCancelled).orElseThrow(() -> new RuntimeException("Unit not found by id"));
        if (!OrderStatus.CONFIRMED.equals(orderToCancel.getOrderStatus())) {
            log.info("Cancelling unconfirmed Order with id: {}", orderToCancel.getId());
            orderToCancel.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(orderToCancel);
            log.info("Cancelled unconfirmed Order with id: {}", orderToCancel.getId());
        }
    }
}

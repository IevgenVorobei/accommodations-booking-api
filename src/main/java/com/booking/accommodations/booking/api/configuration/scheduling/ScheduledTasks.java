package com.booking.accommodations.booking.api.configuration.scheduling;

import com.booking.accommodations.booking.api.model.Order;
import com.booking.accommodations.booking.api.model.OrderStatus;
import com.booking.accommodations.booking.api.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class ScheduledTasks {

    @Value("${unconfirmed.order.ttl.mins}")
    private Long unconfirmedOrdersTtlMins;

    private OrderRepository orderRepository;

    public ScheduledTasks(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    public void unconfirmedOrdersCancellation() {
        log.info("UnconfirmedOrdersCancellation job started...");
        LocalDateTime orderExpiredTime = LocalDateTime.now().minusMinutes(unconfirmedOrdersTtlMins);
        List<Order> ordersToBeCancelled = orderRepository.findExpiredUnconfirmedOrders(orderExpiredTime);
        ordersToBeCancelled.forEach(order -> order.setOrderStatus(OrderStatus.CANCELLED));
        orderRepository.saveAll(ordersToBeCancelled);
        log.info("UnconfirmedOrdersCancellation job finished...");
    }
}

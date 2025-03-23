package com.booking.accommodations.booking.api.repository;

import com.booking.accommodations.booking.api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("select o from Order o where o.createdAt >= :expirationTime")
    List<Order> findExpiredUnconfirmedOrders(LocalDateTime expirationTime);
}

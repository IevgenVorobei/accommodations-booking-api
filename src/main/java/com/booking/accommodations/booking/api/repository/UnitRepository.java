package com.booking.accommodations.booking.api.repository;

import com.booking.accommodations.booking.api.model.Unit;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface UnitRepository extends JpaRepository<Unit, UUID> {

    @Query("select u from Unit u left join Order o on u.id=o.unit.id "
            + "where (concat(u.description, ' ' , u.accommodationType) like %:query% or :query is null) "
            + "and (u.pricePerNight >= :minPrice or :minPrice is null) "
            + "and (u.pricePerNight <= :maxPrice or :maxPrice is null) "
            + "and ((o.checkInDate < :checkIn or o.checkInDate is null) and (o.checkOutDate < :checkOut or o.checkOutDate is null)) "
            + "and (o.orderStatus != 'CONFIRMED' or o.orderStatus is null) "
            + "order by u.pricePerNight asc"
    )
    List<Unit> searchAvailableUnits(LocalDate checkIn, LocalDate checkOut, String query, BigDecimal maxPrice, BigDecimal minPrice, Pageable pageable);

    @Cacheable("amountOfUnitsAvailableForBookingCache")
    @Query("select count (u) from Unit u")
    Integer getAmountOfUnitsAvailableForBooking();
}

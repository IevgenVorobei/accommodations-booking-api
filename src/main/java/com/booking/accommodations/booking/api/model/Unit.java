package com.booking.accommodations.booking.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "units")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class Unit {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "num_of_rooms")
    private Integer numOfRooms;
    private Integer floor;
    @Column(name = "accommodation_type")
    @Enumerated(EnumType.STRING)
    private AccommodationType accommodationType;
    @Column(name = "price_per_night")
    private BigDecimal pricePerNight;
    private String description;

}

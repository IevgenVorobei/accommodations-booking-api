package com.booking.accommodations.booking.api.model;

import com.booking.accommodations.booking.api.model.audit.DateAudit;
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
@Table(name = "payments")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class Payment extends DateAudit {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column
    BigDecimal amount;
    @Column(name = "currency_code")
    private String currencyCode;
    @Column
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}

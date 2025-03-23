package com.booking.accommodations.booking.api.service;

import com.booking.accommodations.booking.api.controller.request.OrderConfirmationRequest;
import com.booking.accommodations.booking.api.model.Payment;
import com.booking.accommodations.booking.api.model.PaymentStatus;
import com.booking.accommodations.booking.api.model.dto.PaymentDto;
import com.booking.accommodations.booking.api.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    @Override
    public PaymentDto createPayment(OrderConfirmationRequest confirmationRequest) {
        return toDto(paymentRepository.save(Payment.builder()
                                                   .amount(confirmationRequest.paymentAmount())
                                                   .currencyCode(confirmationRequest.currency())
                                                   .status(PaymentStatus.ACCEPTED)
                                                   .build()));
    }

    private PaymentDto toDto(Payment payment) {
        return PaymentDto.builder().build();
    }
}

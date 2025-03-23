package com.booking.accommodations.booking.api.service;

import com.booking.accommodations.booking.api.controller.request.SearchRequest;
import com.booking.accommodations.booking.api.model.dto.OfferDto;
import com.booking.accommodations.booking.api.model.dto.PriceDto;
import com.booking.accommodations.booking.api.model.dto.UnitDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
public class OfferServiceImpl implements OfferService {

    @Value("${booking.system.commission.percents}")
    private Integer systemCommissionPercentage;
    private UnitService unitService;

    @Autowired
    public OfferServiceImpl(UnitService unitService) {
        this.unitService = unitService;
    }

    @Override
    public List<OfferDto> searchForOffers(SearchRequest searchRequest, int page, int pageSize) {
        return unitService.searchAvailableUnits(searchRequest, page, pageSize)
                          .stream()
                          .map(unit -> buildOfferDto(unit, searchRequest.checkIn(), searchRequest.checkOut()))
                          .toList();
    }


    private OfferDto buildOfferDto(UnitDto unitDto, LocalDate checkIn, LocalDate checkOut) {
        BigDecimal totalPriceExcluded = estimateTotalPrice(unitDto.pricePerNight(), checkIn, checkOut);
        BigDecimal systemCommission = estimateSystemCommission(totalPriceExcluded);
        return OfferDto.builder()
                       .unit(unitDto)
                       .checkIn(checkIn)
                       .checkOut(checkOut)
                       .priceDetails(PriceDto.builder()
                                             .pricePerNight(unitDto.pricePerNight())
                                             .totalUnitPrice(totalPriceExcluded)
                                             .systemCommission(estimateSystemCommission(totalPriceExcluded))
                                             .totalPriceWithCommission(totalPriceExcluded.add(systemCommission))
                                             .build())
                       .build();
    }

    private BigDecimal estimateTotalPrice(BigDecimal pricePerNight, LocalDate checkIn, LocalDate checkOut) {
        long daysBetween = ChronoUnit.DAYS.between(checkIn, checkOut);
        return pricePerNight.multiply(BigDecimal.valueOf(daysBetween));
    }

    private BigDecimal estimateSystemCommission(BigDecimal priceWithoutCommission) {
        return priceWithoutCommission.multiply(new BigDecimal(systemCommissionPercentage))
                                     .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }
}

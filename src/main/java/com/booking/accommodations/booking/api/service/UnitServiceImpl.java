package com.booking.accommodations.booking.api.service;

import com.booking.accommodations.booking.api.controller.request.SearchRequest;
import com.booking.accommodations.booking.api.exceptions.ResourceNotFoundException;
import com.booking.accommodations.booking.api.model.Unit;
import com.booking.accommodations.booking.api.model.dto.UnitDto;
import com.booking.accommodations.booking.api.repository.UnitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UnitServiceImpl implements UnitService {

    private UnitRepository unitRepository;

    public UnitServiceImpl(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @Override
    @CacheEvict(cacheNames = "amountOfUnitsAvailableForBookingCache", allEntries = true)
    public void addUnit(UnitDto unitDto) {
        saveUnits(List.of(Unit.builder()
                              .floor(unitDto.floor())
                              .numOfRooms(unitDto.numOfRooms())
                              .accommodationType(unitDto.accommodationType())
                              .description(unitDto.description())
                              .pricePerNight(unitDto.pricePerNight())
                              .build()));
    }

    @Override
    @CacheEvict(cacheNames = "amountOfUnitsAvailableForBookingCache", allEntries = true)
    public void addUnits(List<UnitDto> units) {
        saveUnits(units.stream().map(dto -> Unit.builder()
                                                .accommodationType(dto.accommodationType())
                                                .floor(dto.floor())
                                                .numOfRooms(dto.numOfRooms())
                                                .description(dto.description())
                                                .pricePerNight(dto.pricePerNight())
                                                .build()).toList());
    }

    @Override
    public Unit findById(UUID unitId) {
        return unitRepository.findById(unitId).orElseThrow(() -> new RuntimeException("Unit not found by id"));
    }

    @Override
    public UnitDto updateUnit(String unitId, UnitDto unitDto) {
        Optional<Unit> unitToBeUpdatedOpt = unitRepository.findById(UUID.fromString(unitId));
        if (unitToBeUpdatedOpt.isPresent()) {
            Unit unitToBeUpdated = unitToBeUpdatedOpt.get();
            Optional.ofNullable(unitDto.accommodationType()).ifPresent(unitToBeUpdated::setAccommodationType);
            Optional.ofNullable(unitDto.floor()).ifPresent(unitToBeUpdated::setFloor);
            Optional.ofNullable(unitDto.description()).ifPresent(unitToBeUpdated::setDescription);
            Optional.ofNullable(unitDto.numOfRooms()).ifPresent(unitToBeUpdated::setNumOfRooms);
            Optional.ofNullable(unitDto.pricePerNight()).ifPresent(unitToBeUpdated::setPricePerNight);

            return toDto(unitRepository.save(unitToBeUpdated));
        }
        throw new ResourceNotFoundException("Unit not Found", unitId);
    }

    @Override
    public List<UnitDto> searchAvailableUnits(SearchRequest searchRequest, int page, int pageSize) {
        return unitRepository.searchAvailableUnits(
                                     searchRequest.checkIn(),
                                     searchRequest.checkOut(),
                                     searchRequest.query(),
                                     searchRequest.maxPricePerNight(),
                                     searchRequest.minPricePerNight(),
                                     PageRequest.of(page, pageSize)
                             ).stream()
                             .map(this::toDto)
                             .collect(Collectors.toList());
    }

    @Override
    public Integer getAmountOfUnitsAvailableForBooking() {
        return unitRepository.getAmountOfUnitsAvailableForBooking();
    }

    private void saveUnits(List<Unit> units) {
        unitRepository.saveAll(units);
    }

    private UnitDto toDto(Unit unit) {
        return UnitDto.builder()
                      .accommodationType(unit.getAccommodationType())
                      .floor(unit.getFloor())
                      .numOfRooms(unit.getNumOfRooms())
                      .pricePerNight(unit.getPricePerNight())
                      .description(unit.getDescription())
                      .build();
    }
}

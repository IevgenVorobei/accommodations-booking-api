package com.booking.accommodations.booking.api.service;

import com.booking.accommodations.booking.api.controller.request.SearchRequest;
import com.booking.accommodations.booking.api.model.Unit;
import com.booking.accommodations.booking.api.model.dto.UnitDto;

import java.util.List;
import java.util.UUID;

public interface UnitService {

    List<UnitDto> addUnit(UnitDto unit);

    List<UnitDto> addUnits(List<UnitDto> units);

    Unit findById(UUID unitId);

    UnitDto updateUnit(String unitId, UnitDto unitDto);

    List<UnitDto> searchAvailableUnits(SearchRequest searchRequest, int page, int pageSize);

    Integer getAmountOfUnitsAvailableForBooking();

}

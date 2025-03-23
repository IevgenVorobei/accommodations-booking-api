package com.booking.accommodations.booking.api.service;

import com.booking.accommodations.booking.api.controller.request.SearchRequest;
import com.booking.accommodations.booking.api.model.dto.OfferDto;

import java.util.List;

public interface OfferService {

    List<OfferDto> searchForOffers(SearchRequest searchRequest, int page, int pageSize);
}

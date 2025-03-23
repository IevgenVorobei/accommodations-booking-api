package com.booking.accommodations.booking.api.controller;

import com.booking.accommodations.booking.api.controller.request.CancellationRequest;
import com.booking.accommodations.booking.api.controller.request.OrderConfirmationRequest;
import com.booking.accommodations.booking.api.controller.request.OrderRequest;
import com.booking.accommodations.booking.api.controller.request.SearchRequest;
import com.booking.accommodations.booking.api.model.dto.OfferDto;
import com.booking.accommodations.booking.api.model.dto.OrderDto;
import com.booking.accommodations.booking.api.model.dto.UnitDto;
import com.booking.accommodations.booking.api.service.OfferService;
import com.booking.accommodations.booking.api.service.OrderService;
import com.booking.accommodations.booking.api.service.UnitService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/v1")
@Tag(name = "Accommodation booking api")
@Validated
public class AccommodationController {

    private UnitService unitService;
    private OrderService orderService;
    private OfferService offerService;

    @Autowired
    public AccommodationController(UnitService unitService, OrderService orderService, OfferService offerService) {
        this.unitService = unitService;
        this.orderService = orderService;
        this.offerService = offerService;
    }

    @PostMapping(value = "/unit")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity addUnit(@Valid @RequestBody UnitDto unit) {
        unitService.addUnit(unit);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/units")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity addUnits(@RequestBody List<UnitDto> units) {
        unitService.addUnits(units);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping(value = "/unit/{unitId}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "basicAuth")
    public UnitDto updateUnit(@PathVariable String unitId, @RequestBody UnitDto unitDto) {
        return unitService.updateUnit(unitId, unitDto);
    }

    @GetMapping(value = "/offers")
    public List<OfferDto> search(@Valid SearchRequest searchRequest,
                                 @RequestParam(required = false, defaultValue = "0") int page,
                                 @RequestParam(required = false, defaultValue = "10")
                                 @Max(value = 1000, message = "Max page-size value is 1000") int pageSize) {
        return offerService.searchForOffers(searchRequest, page, pageSize);
    }

    @PostMapping(value = "/order")
    public OrderDto createOrder(@RequestBody @Valid OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @PostMapping(value = "/order/{orderId}/confirm")
    public OrderDto confirmOrder(@PathVariable UUID orderId, @RequestBody @Valid OrderConfirmationRequest confirmationRequest) {
        return orderService.confirmOrder(orderId, confirmationRequest);
    }

    @PostMapping("/order/{orderId}/cancel")
    public OrderDto cancelBooking(@PathVariable UUID orderId, @RequestBody CancellationRequest cancellationRequest) {
        return orderService.cancelOrder(orderId, cancellationRequest.reason());
    }

    @GetMapping(value = "/unit/statistics")
    public Integer getAmountOfUnitsAvailableForBooking() {
        return unitService.getAmountOfUnitsAvailableForBooking();
    }

}

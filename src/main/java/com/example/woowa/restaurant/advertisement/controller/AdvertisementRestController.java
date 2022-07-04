package com.example.woowa.restaurant.advertisement.controller;

import com.example.woowa.restaurant.advertisement.dto.request.AdvertisementCreateRequest;
import com.example.woowa.restaurant.advertisement.dto.request.AdvertisementUpdateRequest;
import com.example.woowa.restaurant.advertisement.dto.response.AdvertisementCreateResponse;
import com.example.woowa.restaurant.advertisement.dto.response.AdvertisementFindResponse;
import com.example.woowa.restaurant.advertisement.service.AdvertisementService;
import com.example.woowa.restaurant.restaurant.dto.response.RestaurantFindResponse;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/baemin/v1/advertisements")
@RestController
public class AdvertisementRestController {

    private final AdvertisementService advertisementService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdvertisementCreateResponse> createAdvertisement(@Valid @RequestBody
        final AdvertisementCreateRequest advertisementCreateRequest) {
        AdvertisementCreateResponse advertisement = advertisementService.createAdvertisement(
            advertisementCreateRequest);
        return new ResponseEntity<>(advertisement, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AdvertisementFindResponse>> findAllAdvertisements() {
        List<AdvertisementFindResponse> advertisements = advertisementService.findAdvertisements();
        return new ResponseEntity<>(advertisements, HttpStatus.OK);
    }

    @GetMapping(value = "/{advertisementId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdvertisementFindResponse> findAdvertisementById(@PathVariable final Long advertisementId) {
        AdvertisementFindResponse advertisement = advertisementService.findAdvertisementById(advertisementId);
        return new ResponseEntity<>(advertisement, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{advertisementId}/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RestaurantFindResponse>> findRestaurantsByAdvertisementId(@PathVariable final Long advertisementId) {
        List<RestaurantFindResponse> restaurants = advertisementService.findRestaurantsByAdvertisementId(advertisementId);
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @PutMapping(value = "/{advertisementId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateAdvertisementById(@PathVariable final Long advertisementId,
        @RequestBody @Valid final AdvertisementUpdateRequest advertisementUpdateRequest) {
        advertisementService.updateAdvertisementById(advertisementId, advertisementUpdateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{advertisementId}")
    public ResponseEntity<Void> deleteAdvertisementById(@PathVariable final Long advertisementId) {
        advertisementService.deleteAdvertisementById(advertisementId);
        return new ResponseEntity<>(HttpStatus.OK );
    }

    @PostMapping(value = "/{advertisementId}/restaurants/{restaurantId}")
    public ResponseEntity<Void> includeRestaurantInAdvertisement(@PathVariable final Long advertisementId,
        @PathVariable final Long restaurantId) {
        advertisementService.includeRestaurantInAdvertisement(advertisementId, restaurantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{advertisementId}/restaurants/{restaurantId}")
    public ResponseEntity<Void> excludeRestaurantOutOfAdvertisement(@PathVariable final Long advertisementId,
        @PathVariable final Long restaurantId) {
        advertisementService.excludeRestaurantOutOfAdvertisement(advertisementId, restaurantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

package com.example.woowa.restaurant.owner.controller;

import com.example.woowa.restaurant.owner.dto.request.OwnerCreateRequest;
import com.example.woowa.restaurant.owner.dto.request.OwnerUpdateRequest;
import com.example.woowa.restaurant.owner.dto.response.OwnerCreateResponse;
import com.example.woowa.restaurant.owner.dto.response.OwnerFindResponse;
import com.example.woowa.restaurant.owner.service.OwnerService;
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
@RequestMapping(value = "/baemin/v1/owners")
@RestController
public class OwnerRestController {

    private final OwnerService ownerService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OwnerCreateResponse> createOwner(final @Valid @RequestBody OwnerCreateRequest ownerCreateRequest) {
        OwnerCreateResponse owner = ownerService.createOwner(ownerCreateRequest);
        return new ResponseEntity<>(owner, HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OwnerFindResponse>> findAllOwners() {
        List<OwnerFindResponse> owners = ownerService.findOwners();
        return new ResponseEntity<>(owners, HttpStatus.OK);
    }

    @GetMapping(value = "/{ownerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OwnerFindResponse> findOwnerById(final @PathVariable Long ownerId) {
        OwnerFindResponse owner = ownerService.findOwnerById(ownerId);
        return new ResponseEntity<>(owner, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{ownerId}")
    public ResponseEntity<Void> deleteOwnerById(final @PathVariable Long ownerId) {
        ownerService.deleteOwnerById(ownerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{ownerId}")
    public ResponseEntity<Void> updateOwnerById(final @PathVariable Long ownerId,
        final @Valid @RequestBody OwnerUpdateRequest ownerUpdateRequest) {
        ownerService.updateOwnerById(ownerId, ownerUpdateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

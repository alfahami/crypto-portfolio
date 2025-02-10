package com.codelogium.portfolioservice.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codelogium.portfolioservice.entity.Holding;
import com.codelogium.portfolioservice.service.HoldingService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = "users/{userId}/portfolios/{portfolioId}/holdings", produces = MediaType.APPLICATION_JSON_VALUE)
public class HoldingController {
    
    private HoldingService holdingService;

    @PostMapping
    public ResponseEntity<Holding> createHolding(@RequestBody @Valid Holding holding) {
        return new ResponseEntity<>(holdingService.createHolding(holding), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Holding> updateHolding(@PathVariable Long id, @RequestBody @Valid Holding holding) {
        return new ResponseEntity<>(holdingService.updateHolding(id, holding), HttpStatus.OK);
    }

}

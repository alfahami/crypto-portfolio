package com.codelogium.portfolioservice.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codelogium.portfolioservice.entity.Holding;
import com.codelogium.portfolioservice.service.HoldingService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/portfolios/{portfolioId}/holdings")
public class HoldingController {
    
    private HoldingService holdingService;

    @PostMapping
    public ResponseEntity<Holding> addHolding(@PathVariable Holding holding) {
        return new ResponseEntity<>(holdingService.addHolding(holding), HttpStatus.valueOf(200));
    }

}

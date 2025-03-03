package com.codelogium.portfolioservice.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@AllArgsConstructor
@RequestMapping(value = "users/{userId}/portfolios/{portfolioId}/holdings", produces = MediaType.APPLICATION_JSON_VALUE)
public class HoldingController {

    private HoldingService holdingService;

    @PostMapping
    public ResponseEntity<Holding> createHolding(@PathVariable Long portfolioId, @PathVariable Long userId, @RequestBody @Valid Holding holding) {
        return new ResponseEntity<>(holdingService.createHolding(portfolioId, userId, holding), HttpStatus.CREATED);
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<Holding> retrieveHolding(@PathVariable String symbol, @PathVariable Long portfolioId, @PathVariable Long userId) {
        return new ResponseEntity<>(holdingService.retrieveHolding(symbol, portfolioId, userId), HttpStatus.OK);
    }

    @PatchMapping("/{symbol}")
    public ResponseEntity<Holding> updateHolding(@PathVariable String symbol, @PathVariable Long portfolioId, @PathVariable Long userId, @RequestBody @Valid Holding holding) {
        return new ResponseEntity<>(holdingService.updateHolding(symbol, portfolioId, userId, holding),HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Holding>> retrieveHoldingsByPortfolioId(@PathVariable Long userId, @PathVariable Long portfolioId) {
        return new ResponseEntity<>(holdingService.retrieveHoldingsByPortfolioId(portfolioId, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{symbol}")
    public ResponseEntity<HttpStatus> removeHolding(@PathVariable String symbol, @PathVariable Long portfolioId, @PathVariable Long userId) {
        holdingService.removeHolding(symbol, portfolioId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

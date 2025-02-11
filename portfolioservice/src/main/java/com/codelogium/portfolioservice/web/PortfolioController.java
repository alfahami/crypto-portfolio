package com.codelogium.portfolioservice.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codelogium.portfolioservice.entity.Portfolio;
import com.codelogium.portfolioservice.service.PortfolioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/users/{userId}/portfolios", produces = MediaType.APPLICATION_JSON_VALUE)
public class PortfolioController {

    private PortfolioService portfolioService;

    @PostMapping
    public ResponseEntity<Portfolio> createPortfolio(@PathVariable Long userId, @RequestBody @Valid Portfolio portfolio) {
        return new ResponseEntity<>(portfolioService.createPortfolio(userId, portfolio), HttpStatus.CREATED);
    }

    @GetMapping("/{portfolioId}")
    public ResponseEntity<Portfolio> retrievePortfolio(@PathVariable Long portfolioId, @PathVariable Long userId) {
        return new ResponseEntity<>(portfolioService.retrievePortfolio(portfolioId, userId), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Portfolio> updatePortfolio(@PathVariable Long portfolioId, @PathVariable Long userId, @RequestBody @Valid Portfolio portfolio) {
        ;
        return new ResponseEntity<>(portfolioService.updatePortfolio(portfolioId, userId, portfolio), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Portfolio>> retrievePorfoliosByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(portfolioService.retrievePortfoliosByUserId(userId), HttpStatus.OK);
    }
    // TODO: Ensure deletion is only with owned holdings
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> removePortfolio(@PathVariable Long userId, @PathVariable Long portfolioId) {
        portfolioService.removePortfolio(userId, portfolioId);;
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}

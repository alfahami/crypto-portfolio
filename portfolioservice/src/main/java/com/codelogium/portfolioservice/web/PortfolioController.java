package com.codelogium.portfolioservice.web;

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

@RestController
@AllArgsConstructor
@RequestMapping(value = "/users/{userId}/portfolios", produces = MediaType.APPLICATION_JSON_VALUE)
public class PortfolioController {

    private PortfolioService portfolioService;

    @PostMapping
    public ResponseEntity<Portfolio> createPortfolio(@PathVariable Long userId, @RequestBody @Valid Portfolio portfolio) {
        return new ResponseEntity<>(portfolioService.createPortfolio(userId, portfolio), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Portfolio> getPortfolio(@PathVariable Long id) {
        return new ResponseEntity<>(portfolioService.getPortfolio(id), HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> removePortfolio(@PathVariable Long id) {
        portfolioService.removePortfolio(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}

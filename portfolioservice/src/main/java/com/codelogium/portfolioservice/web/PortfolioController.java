package com.codelogium.portfolioservice.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codelogium.portfolioservice.entity.Portfolio;
import com.codelogium.portfolioservice.service.PortfolioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/users/{userId}/portfolios")
public class PortfolioController {

    private PortfolioService portfolioService;

    @PostMapping()
    public ResponseEntity<Portfolio> addPortfolio(@RequestBody @Valid Portfolio portfolio) {
        return new ResponseEntity<>(null);
    }



    
}

package com.codelogium.portfolioservice.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codelogium.portfolioservice.entity.Holding;
import com.codelogium.portfolioservice.entity.Portfolio;
import com.codelogium.portfolioservice.entity.User;
import com.codelogium.portfolioservice.service.HoldingService;
import com.codelogium.portfolioservice.service.PortfolioService;
import com.codelogium.portfolioservice.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class PortfolioController {

    private HoldingService holdingService;
    private PortfolioService portfolioService;
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<User> addUser(@RequestBody @Valid User user) {
        return new ResponseEntity<>(userService.addUser(user), HttpStatus.valueOf(200));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.valueOf(200));
    }

    @PostMapping("/{id}/portfolios/")
    public ResponseEntity<Portfolio> addPortfolio(@RequestBody @Valid Portfolio portfolio) {
        return new ResponseEntity<>(portfolioService.addPortfolio(portfolio), HttpStatus.valueOf(200));
    }

    @PostMapping("/portfolios/{id}/holding")
    public ResponseEntity<Holding> addHolding(@PathVariable Holding holding) {
        return new ResponseEntity<>(holdingService.addHolding(holding), HttpStatus.valueOf(200));
    }


    
}

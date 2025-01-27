package com.codelogium.exchangerateservice.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codelogium.exchangerateservice.mapper.CryptoResponseMapper;
import com.codelogium.exchangerateservice.service.ExchangeRateService;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@AllArgsConstructor
@RequestMapping("/exchange-rate")
public class ExchangeRateController {
    
    private ExchangeRateService exchangeRateService;

    @GetMapping("/latest")
    public Mono<ResponseEntity<String>> getRawData() {
        return exchangeRateService.getAllData();
    }

    @GetMapping()
    public Mono<CryptoResponseMapper> getMethodName(@RequestParam String symbol, @RequestParam String base) {
        return exchangeRateService.retrivePrice(symbol, base);
    }
    
}

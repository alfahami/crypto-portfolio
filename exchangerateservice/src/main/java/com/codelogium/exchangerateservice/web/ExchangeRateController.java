package com.codelogium.exchangerateservice.web;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codelogium.exchangerateservice.mapper.CryptoResponseMapper;
import com.codelogium.exchangerateservice.service.ExchangeRateService;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Validated // enables method-level validation
@RestController
@AllArgsConstructor
@RequestMapping(value = "/exchange-rate", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExchangeRateController {

    private ExchangeRateService exchangeRateService;

    @GetMapping("/latest")
    public Mono<ResponseEntity<String>> getRawData() {
        return exchangeRateService.getAllData();
    }

    // Request Params validation could be handled usign a record java class inside
    // dto
    @GetMapping
    public Mono<CryptoResponseMapper> retrievePrice(
            @RequestParam @NotBlank(message = "Symbol cannot be null or blank") String symbol,
            @RequestParam @NotBlank(message = "Base cannot be null or blank") String base) {
        return exchangeRateService.retrivePrice(symbol.toUpperCase(), base.toUpperCase());
    }
}

package com.codelogium.exchangerateservice.mapper;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CryptoResponseMapper {
    private String symbol;
    private String baseCurrency;
    private BigDecimal price;
}
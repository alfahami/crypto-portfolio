package com.codelogium.exchangerateservice.mapper;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoResponseMapper {
    private String symbol;
    private String base;
    private BigDecimal price;
}
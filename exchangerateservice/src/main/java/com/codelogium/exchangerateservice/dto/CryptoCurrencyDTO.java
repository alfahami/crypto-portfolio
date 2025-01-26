package com.codelogium.exchangerateservice.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CryptoCurrencyDTO {
    private String baseCurrency;
    private BigDecimal price;
    private String symbol;

}

package com.codelogium.exchangerateservice.mapper;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoResponseMapper {
    private String symbol;
    private String base;
    @JsonProperty("price")
    private BigDecimal price;
}
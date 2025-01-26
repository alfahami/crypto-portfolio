package com.codelogium.exchangerateservice.mapper;

import java.math.BigDecimal;

public class CryptoResponseMapper {
    private String baseCurrency;
    private BigDecimal price;
    private String symbol;


    public CryptoResponseMapper() {
    }

    public CryptoResponseMapper(String baseCurrency, BigDecimal price, String symbol) {
        this.baseCurrency = baseCurrency;
        this.price = price;
        this.symbol = symbol;
    }

    public String getBaseCurrency() {
        return this.baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public CryptoResponseMapper baseCurrency(String baseCurrency) {
        setBaseCurrency(baseCurrency);
        return this;
    }

    public CryptoResponseMapper price(BigDecimal price) {
        setPrice(price);
        return this;
    }

    public CryptoResponseMapper symbol(String symbol) {
        setSymbol(symbol);
        return this;
    }
}

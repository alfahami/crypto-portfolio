package com.codelogium.portfolioservice.entity;

import java.math.BigDecimal;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "holdings")
public class Holding {

    private Long id;
    private String symbol;
    private BigDecimal amount;
}

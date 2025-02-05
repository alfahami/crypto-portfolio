package com.codelogium.portfolioservice.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "portfolios")
public class Portfolio {
    private Long id;
    private String name;

    private User user;
    private List<Holding> holdings;

    //Add holding to the portfolio with initialization check
    public void addHolding(Holding holding) {
        if(this.holdings == null) {
            this.holdings = new ArrayList<>();
        }
        this.holdings.add(holding);
    }

}

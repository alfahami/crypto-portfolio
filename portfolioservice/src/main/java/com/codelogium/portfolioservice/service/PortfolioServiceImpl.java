package com.codelogium.portfolioservice.service;

import org.springframework.stereotype.Service;

import com.codelogium.portfolioservice.entity.Portfolio;
import com.codelogium.portfolioservice.respositry.PortfolioRespository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {
    
    private PortfolioRespository portfolioRespository;
    @Override
    public Portfolio addPortfolio(Portfolio portfolio) {
        return this.portfolioRespository.save(portfolio);
    }
}

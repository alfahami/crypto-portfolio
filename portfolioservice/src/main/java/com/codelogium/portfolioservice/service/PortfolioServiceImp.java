package com.codelogium.portfolioservice.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codelogium.portfolioservice.entity.Portfolio;
import com.codelogium.portfolioservice.exception.EntityNotFoundException;
import com.codelogium.portfolioservice.respositry.PortfolioRespository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PortfolioServiceImp implements PortfolioService {
    
    private PortfolioRespository portfolioRespository;
    @Override
    public Portfolio addPortfolio(Portfolio portfolio) {
        return this.portfolioRespository.save(portfolio);
    }

    public static Portfolio unwrapPortfolio(Long id, Optional<Portfolio> optPorfolio) {
        if(optPorfolio.isPresent()) return optPorfolio.get();
        else throw new EntityNotFoundException(id, Portfolio.class);
    } 
}

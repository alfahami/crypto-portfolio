package com.codelogium.portfolioservice.service;

import java.util.List;

import com.codelogium.portfolioservice.entity.Portfolio;

public interface PortfolioService {
    Portfolio createPortfolio(Long userId, Portfolio portfolio);
    Portfolio retrievePortfolio(Long portfolioId, Long userId);
    Portfolio updatePortfolio(Long portfolioId, Long userId, Portfolio newPortfolio);
    void removePortfolio(Long userId, Long portfolioId);
    List<Portfolio> retrievePortfoliosByUserId(Long userId);
} 


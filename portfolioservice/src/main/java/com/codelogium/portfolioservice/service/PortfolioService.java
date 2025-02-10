package com.codelogium.portfolioservice.service;

import com.codelogium.portfolioservice.entity.Portfolio;

public interface PortfolioService {
    Portfolio createPortfolio(Long userId, Portfolio portfolio);
    Portfolio getPortfolio(Long id);
    void removePortfolio(Long id);
} 


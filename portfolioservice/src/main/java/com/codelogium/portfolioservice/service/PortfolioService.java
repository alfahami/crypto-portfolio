package com.codelogium.portfolioservice.service;

import java.util.List;

import com.codelogium.portfolioservice.entity.Portfolio;

public interface PortfolioService {
    Portfolio createPortfolio(Long userId, Portfolio portfolio);
    Portfolio retrievePortfolio(Long id);
    Portfolio updatePortfolio(Long id, Portfolio newPortfolio);
    void removePortfolio(Long id);
    List<Portfolio> retrivePortfolioByUserId(Long id);
} 


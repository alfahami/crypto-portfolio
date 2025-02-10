package com.codelogium.portfolioservice.service;

import com.codelogium.portfolioservice.entity.Portfolio;

public interface PortfolioService {
    Portfolio createPortfolio(Long userId, Portfolio portfolio);
    Portfolio getPortfolio(Long id);
    Portfolio updatePortfolio(Long id, Portfolio newPortfolio);
    void removePortfolio(Long id);

} 


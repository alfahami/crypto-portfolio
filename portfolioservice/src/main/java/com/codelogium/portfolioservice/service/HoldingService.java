package com.codelogium.portfolioservice.service;

import java.util.List;

import com.codelogium.portfolioservice.entity.Holding;

public interface HoldingService {
    Holding createHolding(Long userId, Long portfolioId, Holding holding);
    Holding retrieveHolding(Long id);
    Holding updateHolding(Long userId, Long portfolioId, Long holdingId, Holding newHolding);
    void removeHolding(Long id);
    List<Holding> retrieveHoldingByPortfolioId(Long userId, Long portfolioId);
}

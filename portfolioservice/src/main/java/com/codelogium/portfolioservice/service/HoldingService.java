package com.codelogium.portfolioservice.service;

import java.util.List;

import com.codelogium.portfolioservice.entity.Holding;

public interface HoldingService {
    Holding createHolding(Long portfolioId, Long userId, Holding holding);

    Holding retrieveHolding(Long holdingId, Long portfolioId, Long userId);

    Holding updateHolding(Long holdingId, Long portfolioId, Long userId, Holding newHolding);

    void removeHolding(Long holodingId, Long portfolioId, Long userId);

    List<Holding> retrieveHoldingsByPortfolioId(Long portfolioId, Long userId);
}

package com.codelogium.portfolioservice.service;

import com.codelogium.portfolioservice.entity.Holding;

public interface HoldingService {
    Holding createHolding(Holding holding);
    Holding updateHolding(Long id, Holding newHolding);
    void removeHolding(Long id);
}

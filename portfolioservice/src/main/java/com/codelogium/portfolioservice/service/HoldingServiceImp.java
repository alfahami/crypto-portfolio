package com.codelogium.portfolioservice.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codelogium.portfolioservice.entity.Holding;
import com.codelogium.portfolioservice.respositry.HoldingRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HoldingServiceImp implements HoldingService {
    
    private HoldingRepository holdingRepository;

    @Override
    public Holding addHolding(Holding holding) {
        return this.holdingRepository.save(holding);
    }
}

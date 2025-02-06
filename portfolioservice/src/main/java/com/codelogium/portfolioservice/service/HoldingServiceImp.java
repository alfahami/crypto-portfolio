package com.codelogium.portfolioservice.service;

import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.stereotype.Service;

import com.codelogium.portfolioservice.entity.Holding;
import com.codelogium.portfolioservice.exception.EntityNotFoundException;
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

    public static Holding unwrapHolding(Long id, Optional<Holding> optHolding) {
        if(optHolding.isPresent()) return optHolding.get();
        else throw new EntityNotFoundException(id, Holding.class);
    }
}

package com.codelogium.portfolioservice.service;

import static com.codelogium.portfolioservice.util.EntityUtils.*;

import java.util.Optional;

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
    public Holding createHolding(Holding holding) {
        return this.holdingRepository.save(holding);
    }

    @Override
    public Holding updateHolding(Long id, Holding newHolding) {
        Holding existingHolding = unwrapHolding(id, holdingRepository.findById(id));
        newHolding.setId(existingHolding.getId());

        updateIfNotNull(existingHolding::setSymbol, newHolding.getSymbol());
        updateIfNotNull(existingHolding::setAmount, newHolding.getAmount());
        
        return holdingRepository.save(existingHolding);
    }

    @Override
    public void removeHolding(Long id) {
        Holding holding = unwrapHolding(id, holdingRepository.findById(id));
        holdingRepository.delete(holding);
    }

    public static Holding unwrapHolding(Long id, Optional<Holding> optHolding) {
        if(optHolding.isPresent()) return optHolding.get();
        else throw new EntityNotFoundException(id, Holding.class);
    }

}

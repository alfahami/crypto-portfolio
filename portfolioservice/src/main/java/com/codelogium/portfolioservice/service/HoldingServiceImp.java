package com.codelogium.portfolioservice.service;

import static com.codelogium.portfolioservice.util.EntityUtils.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codelogium.portfolioservice.entity.Holding;
import com.codelogium.portfolioservice.entity.Portfolio;
import com.codelogium.portfolioservice.exception.EntityNotFoundException;
import com.codelogium.portfolioservice.respositry.HoldingRepository;
import com.codelogium.portfolioservice.respositry.PortfolioRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HoldingServiceImp implements HoldingService {
    
    private HoldingRepository holdingRepository;
    private PortfolioRepository portfolioRepository;
    private PortfolioServiceImp portfolioServiceImp;

    @Override
    public Holding createHolding(Long portfolioId, Long userId , Holding holding) {

        portfolioServiceImp.validateUserExists(userId);
        // Checks and Retrieve the portfolio that belongs to the calling user
        Portfolio portfolio = PortfolioServiceImp.unwrapPortfolio(portfolioId, portfolioRepository.findByIdAndUserId(portfolioId, userId));
        
        // Setting the relation portfolio <- holding
        holding.setPortfolio(portfolio);
        return this.holdingRepository.save(holding);
    }

    @Override
    public Holding retrieveHolding(Long holdingId, Long portfolioId, Long userId) {

        // Relying on portfolio service to check for user as the uri could be tampered at this level
        portfolioServiceImp.validateUserExists(userId);

        PortfolioServiceImp.unwrapPortfolio(portfolioId, portfolioRepository.findByIdAndUserId(portfolioId, userId));

        return unwrapHolding(holdingId, holdingRepository.findByIdAndPortfolioIdAndPortfolioUserId(holdingId, portfolioId, userId));
    }

    @Transactional // commit changes correcly or roll back completely if failure
    @Override
    public Holding updateHolding(Long holdingId, Long portfolioId,Long userId, Holding newHolding) {

        portfolioServiceImp.validateUserExists(userId);
        
        PortfolioServiceImp.unwrapPortfolio(portfolioId, portfolioRepository.findByIdAndUserId(portfolioId, userId));

        // Checks if the holding exists
        // Ensure ownership of user -> portfolio -> holding
        Holding existingHolding = unwrapHolding(holdingId, holdingRepository.findByIdAndPortfolioIdAndPortfolioUserId(holdingId, portfolioId, userId));

        // Ignore request body ID, could be changed tho
        newHolding.setId(existingHolding.getId());

        updateIfNotNull(existingHolding::setSymbol, newHolding.getSymbol());
        updateIfNotNull(existingHolding::setAmount, newHolding.getAmount());
        
        return holdingRepository.save(existingHolding);
    }

    @Override
    public List<Holding> retrieveHoldingsByPortfolioId(Long portfolioId, Long userId) {

        // Relying on portfolio service to check for user as the uri could be tampered at this level
        portfolioServiceImp.validateUserExists(userId);

        // Ensure ownership of user -> portfolio
        PortfolioServiceImp.unwrapPortfolio(portfolioId, portfolioRepository.findByIdAndUserId(portfolioId, userId));

        return holdingRepository.findByPortfolioId(portfolioId);
    }

    @Override
    public void removeHolding(Long holdingId, Long portfolioId, Long userId) {
        // Relying on portfolio service to check for user as the uri could be tampered at this level
        portfolioServiceImp.validateUserExists(userId);

        PortfolioServiceImp.unwrapPortfolio(portfolioId, portfolioRepository.findByIdAndUserId(portfolioId, userId));

        Holding holding = unwrapHolding(portfolioId, holdingRepository.findByIdAndPortfolioIdAndPortfolioUserId(holdingId, portfolioId, userId));

        holdingRepository.delete(holding);
    }

    public static Holding unwrapHolding(Long holdingId, Optional<Holding> optHolding) {
        if(optHolding.isPresent()) return optHolding.get();
        else throw new EntityNotFoundException(holdingId, Holding.class);
    }
}

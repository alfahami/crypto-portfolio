package com.codelogium.portfolioservice.service;

import static com.codelogium.portfolioservice.util.EntityUtils.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codelogium.portfolioservice.entity.Holding;
import com.codelogium.portfolioservice.entity.Portfolio;
import com.codelogium.portfolioservice.entity.User;
import com.codelogium.portfolioservice.exception.EntityNotFoundException;
import com.codelogium.portfolioservice.exception.OwnershipException;
import com.codelogium.portfolioservice.respositry.HoldingRepository;
import com.codelogium.portfolioservice.respositry.PortfolioRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HoldingServiceImp implements HoldingService {
    
    private HoldingRepository holdingRepository;
    private PortfolioRepository portfolioRepository;

    @Override
    public Holding createHolding(Long userId, Long portfolioId, Holding holding) {
        // Check if user exists by trying to find any portfolio for this user
        validateUserExists(userId, portfolioRepository.existsById(userId));

        // Checks and Retrieve the portfolio that belongs to the calling user
        Portfolio portfolio = PortfolioServiceImp.validateAndGetPortfolio(portfolioRepository.findByIdAndUserId(portfolioId, userId));
        
        // Setting the relation portfolio <- holding
        holding.setPortfolio(portfolio);
        return this.holdingRepository.save(holding);
    }

    @Override
    public Holding retrieveHolding(Long id) {
        return unwrapHolding(id, holdingRepository.findById(id));
    }

    @Transactional // Ensure that changes are committed correcly or rolled back completely in case of failure
    @Override
    public Holding updateHolding(Long userId, Long portfolioId, Long holdingId, Holding newHolding) {

        validateUserExists(userId, portfolioRepository.existsById(userId));
        // Checks if the holding exists
        unwrapHolding(holdingId, holdingRepository.findById(holdingId));
        // Ensure ownership of user -> portfolio -> holding
        Holding existingHolding = validateAndGetHolding(holdingRepository.findByIdAndPortfolioIdAndPortfolioUserId(holdingId, portfolioId, userId));
        // Ignore request body ID, could be changed tho
        newHolding.setId(existingHolding.getId());

        updateIfNotNull(existingHolding::setSymbol, newHolding.getSymbol());
        updateIfNotNull(existingHolding::setAmount, newHolding.getAmount());
        
        return holdingRepository.save(existingHolding);
    }

    @Override
    public List<Holding> retrieveHoldingByPortfolioId(Long userId, Long portfolioId) {
        // check if user exists
        validateUserExists(userId, portfolioRepository.existsById(userId));
        // Ensure ownership of user -> portfolio
        PortfolioServiceImp.validateAndGetPortfolio(portfolioRepository.findByIdAndUserId(portfolioId, userId));

        return holdingRepository.findByPortfolioId(portfolioId);
    }

    @Override
    public void removeHolding(Long id) {
        Holding holding = unwrapHolding(id, holdingRepository.findById(id));
        holdingRepository.delete(holding);
    }

    // Ensure a holding exists before proceeding
    public static Holding validateAndGetHolding(Optional<Holding> optionalHolding) {
        return optionalHolding.orElseThrow(() -> new OwnershipException(Holding.class));
    }

    // Ensure that userId was not tempered at this level of the relation
    public static void validateUserExists(Long userId, boolean bool) {
        if(bool == false) throw new EntityNotFoundException(userId, User.class);
    }

    public static Holding unwrapHolding(Long id, Optional<Holding> optHolding) {
        if(optHolding.isPresent()) return optHolding.get();
        else throw new EntityNotFoundException(id, Holding.class);
    }
}

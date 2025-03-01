package com.codelogium.portfolioservice.service;

import static com.codelogium.portfolioservice.util.EntityUtils.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codelogium.portfolioservice.entity.Holding;
import com.codelogium.portfolioservice.entity.Portfolio;
import com.codelogium.portfolioservice.entity.User;
import com.codelogium.portfolioservice.exception.ResourceNotFoundException;
import com.codelogium.portfolioservice.respository.HoldingRepository;
import com.codelogium.portfolioservice.respository.PortfolioRepository;
import com.codelogium.portfolioservice.respository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HoldingServiceImp implements HoldingService {

    private HoldingRepository holdingRepository;
    private PortfolioRepository portfolioRepository;
    private UserRepository userRepository; // for valiadtion checks
    

    @Override
    public Holding createHolding(Long portfolioId, Long userId, Holding holding) {

        // Validata user, check and retrieves portfolio
        validateUserAndChecksOwnership(portfolioId, userId);
        
        Portfolio portfolio = portfolioRepository.findByIdAndUserId(portfolioId, userId).get();

        // Setting the relation portfolio <- holding
        holding.setPortfolio(portfolio);
        return this.holdingRepository.save(holding);
    }

    @Override
    public Holding retrieveHolding(Long holdingId, Long portfolioId, Long userId) {

        // Validata user, check portfolio ownership
        validateUserAndChecksOwnership(portfolioId, userId);

        return unwrapHolding(holdingId,
                holdingRepository.findByIdAndPortfolioIdAndPortfolioUserId(holdingId, portfolioId, userId));
    }

    @Transactional // commit changes correcly or roll back completely if failure
    @Override
    public Holding updateHolding(Long holdingId, Long portfolioId, Long userId, Holding newHolding) {

        // Validata user, check portfolio ownership
        validateUserAndChecksOwnership(portfolioId, userId);

        // Checks if the holding exists
        // Ensure ownership of user -> portfolio -> holding
        Holding existingHolding = unwrapHolding(holdingId,
                holdingRepository.findByIdAndPortfolioIdAndPortfolioUserId(holdingId, portfolioId, userId));

        // No need to as we're updating the retrieved object. Ignore request body ID, could be changed tho
        // newHolding.setId(existingHolding.getId());

        updateIfNotNull(existingHolding::setSymbol, newHolding.getSymbol());
        updateIfNotNull(existingHolding::setAmount, newHolding.getAmount());

        return holdingRepository.save(existingHolding);
    }

    @Override
    public List<Holding> retrieveHoldingsByPortfolioId(Long portfolioId, Long userId) {

        // Validata user, check user->portfolio ownership
        validateUserAndChecksOwnership(portfolioId, userId);

        List<Holding> holdings = holdingRepository.findByPortfolioId(portfolioId);

        if(holdings == null || holdings.size() == 0) throw new ResourceNotFoundException("No holding created yet.");

        return holdings;
    }

    @Override
    public void removeHolding(Long holdingId, Long portfolioId, Long userId) {
       
        // Validata user, check user->portfolio ownership
        validateUserAndChecksOwnership(portfolioId, userId);

        Holding holding = unwrapHolding(portfolioId,
                holdingRepository.findByIdAndPortfolioIdAndPortfolioUserId(holdingId, portfolioId, userId));

        holdingRepository.delete(holding);
    }

    private void validateUserAndChecksOwnership(Long portfolioId, Long userId) {
        
        if(!userRepository.existsById(userId)) throw new ResourceNotFoundException(userId, User.class);

        if(!portfolioRepository.existsByIdAndUserId(portfolioId, userId)) throw new ResourceNotFoundException(portfolioId, Portfolio.class);
    }

    public static Holding unwrapHolding(Long holdingId, Optional<Holding> optHolding) {
        if (optHolding.isPresent())
            return optHolding.get();
        else
            throw new ResourceNotFoundException(holdingId, Holding.class);
    }

}

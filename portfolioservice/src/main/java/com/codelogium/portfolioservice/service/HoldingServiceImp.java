package com.codelogium.portfolioservice.service;

import static com.codelogium.portfolioservice.util.EntityUtils.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codelogium.portfolioservice.entity.Holding;
import com.codelogium.portfolioservice.entity.Portfolio;
import com.codelogium.portfolioservice.exception.ResourceNotFoundException;
import com.codelogium.portfolioservice.respository.HoldingRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HoldingServiceImp implements HoldingService {

    private HoldingRepository holdingRepository;
    private ValidationAndAuthorizationService validationAndAuthorizationService;

    @Override
    public Holding createHolding(Long portfolioId, Long userId, Holding holding) {

        // Validata user, check and retrieves portfolio
        Portfolio portfolio = validationAndAuthorizationService.validateUserAndGetPortfolio(portfolioId, userId);        

        // Setting the relation portfolio <- holding
        holding.setPortfolio(portfolio);
        return this.holdingRepository.save(holding);
    }

    @Override
    public Holding retrieveHolding(Long holdingId, Long portfolioId, Long userId) {

        // Validata user, check portfolio ownership
        validationAndAuthorizationService.validateUserAndGetPortfolio(portfolioId, userId); 

        return unwrapHolding(holdingId,
                holdingRepository.findByIdAndPortfolioIdAndPortfolioUserId(holdingId, portfolioId, userId));
    }

    @Transactional // commit changes correcly or roll back completely if failure
    @Override
    public Holding updateHolding(Long holdingId, Long portfolioId, Long userId, Holding newHolding) {

        // Validata user, check portfolio ownership
        validationAndAuthorizationService.validateUserAndGetPortfolio(portfolioId, userId); 

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
        validationAndAuthorizationService.validateUserAndGetPortfolio(portfolioId, userId); 

        List<Holding> holdings = holdingRepository.findByPortfolioId(portfolioId);

        if(holdings == null || holdings.size() == 0) throw new ResourceNotFoundException("No holding created yet.");

        return holdings;
    }

    @Override
    public void removeHolding(Long holdingId, Long portfolioId, Long userId) {
       
        // Validata user, check user->portfolio ownership
       validationAndAuthorizationService.validateUserAndGetPortfolio(portfolioId, userId); 

        Holding holding = unwrapHolding(portfolioId,
                holdingRepository.findByIdAndPortfolioIdAndPortfolioUserId(holdingId, portfolioId, userId));

        holdingRepository.delete(holding);
    }

    public static Holding unwrapHolding(Long holdingId, Optional<Holding> optHolding) {
        if (optHolding.isPresent())
            return optHolding.get();
        else
            throw new ResourceNotFoundException(holdingId, Holding.class);
    }
}

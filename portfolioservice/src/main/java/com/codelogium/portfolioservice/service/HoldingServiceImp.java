package com.codelogium.portfolioservice.service;

import static com.codelogium.portfolioservice.util.EntityUtils.*;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
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
        // Ensure ownership of user -> portfolio -> holding
        Portfolio portfolio = portfolioRepository.findByIdAndUserId(portfolioId, userId).get();

        // Setting the relation portfolio <- holding
        holding.setPortfolio(portfolio);

        // handling unique constraint violation of symbok
        try {
            return holdingRepository.save(holding);
        } catch(DataIntegrityViolationException e) {
            throw new ResourceNotFoundException("You already have a " + holding.getSymbol() + " holding.");
        }
    }

    @Override
    public Holding retrieveHolding(String symbol, Long portfolioId, Long userId) {

        // Validata user, check portfolio ownership
        validateUserAndChecksOwnership(portfolioId, userId);

        // Ensure ownership of user -> portfolio -> holding
        Portfolio portfolio = PortfolioServiceImp.unwrapPortfolio(portfolioId,
                portfolioRepository.findByIdAndUserId(portfolioId, userId));

        return unwrapHoldingBySymbol(symbol, portfolio);
    }

    @Transactional // commit changes correcly or roll back completely if failure
    @Override
    public Holding updateHolding(String symbol, Long portfolioId, Long userId, Holding newHolding) {

        Portfolio portfolio = getValidatedPortfolio(portfolioId, userId);

        Holding existingHolding = unwrapHoldingBySymbol(symbol, portfolio);

        updateIfNotNull(existingHolding::setSymbol, newHolding.getSymbol());
        updateIfNotNull(existingHolding::setAmount, newHolding.getAmount());

        return holdingRepository.save(existingHolding);
    }

    @Override
    public List<Holding> retrieveHoldingsByPortfolioId(Long portfolioId, Long userId) {

        // Validata user, check user->portfolio ownership
        validateUserAndChecksOwnership(portfolioId, userId);

        List<Holding> holdings = holdingRepository.findByPortfolioId(portfolioId);

        if (holdings == null || holdings.size() == 0)
            throw new ResourceNotFoundException("No holding created yet.");

        return holdings;
    }

    @Transactional
    @Override
    public void removeHolding(String symbol, Long portfolioId, Long userId) {
        Portfolio portfolio = getValidatedPortfolio(portfolioId, userId);

        Holding holding = unwrapHoldingBySymbol(symbol, portfolio);
        portfolio.getHoldings().remove(holding); // triggers orphanRemoval automatically

        // holdingRepository.delete(holding); // no need to manually call this since orphanRemoval is set to true in the entity portfolio field
    }

    private Portfolio getValidatedPortfolio(Long portfolioId, Long userId) {
        // Validata user, check portfolio ownership
        validateUserAndChecksOwnership(portfolioId, userId);

        // Ensure ownership of user -> portfolio and return portfolio
        return PortfolioServiceImp.unwrapPortfolio(portfolioId,
                portfolioRepository.findByIdAndUserId(portfolioId, userId));
    }

    private void validateUserAndChecksOwnership(Long portfolioId, Long userId) {

        if (!userRepository.existsById(userId))
            throw new ResourceNotFoundException(userId, User.class);

        if (!portfolioRepository.existsByIdAndUserId(portfolioId, userId))
            throw new ResourceNotFoundException(portfolioId, Portfolio.class);
    }

    private Holding unwrapHoldingBySymbol(String symbol, Portfolio portfolio) {
        return portfolio.getHoldings().stream()
                .filter(holding -> holding.getSymbol().equals(symbol))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("The " + symbol + " holding is not found."));
    }
}

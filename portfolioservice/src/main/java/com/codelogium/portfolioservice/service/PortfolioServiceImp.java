package com.codelogium.portfolioservice.service;

import static com.codelogium.portfolioservice.util.EntityUtils.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codelogium.portfolioservice.entity.Portfolio;
import com.codelogium.portfolioservice.entity.User;
import com.codelogium.portfolioservice.exception.EntityNotFoundException;
import com.codelogium.portfolioservice.exception.OwnershipException;
import com.codelogium.portfolioservice.respositry.PortfolioRepository;
import com.codelogium.portfolioservice.respositry.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PortfolioServiceImp implements PortfolioService {
    
    private PortfolioRepository portfolioRepository;
    private UserRepository userRepository;

    @Override
    public Portfolio createPortfolio(Long userId, Portfolio portfolio) {

        User user = UserServiceImp.unwrapUser(userId, userRepository.findById(userId));
        portfolio.setUser(user);
        return portfolioRepository.save(portfolio);
    }

    @Override
    public Portfolio retrievePortfolio(Long portfolioId, Long userId) {
        UserServiceImp.unwrapUser(userId, userRepository.findById(userId));
        return unwrapPortfolio(portfolioId, portfolioRepository.findByIdAndUserId(portfolioId, userId));
    }
 
    @Transactional // Commit changes correctly or rollback if failure
    @Override
    public Portfolio updatePortfolio(Long portfolioId, Long userId, Portfolio newPortfolio) {

        Portfolio existingPortfolio = unwrapPortfolio(portfolioId, portfolioRepository.findByIdAndUserId(portfolioId, userId));
        newPortfolio.setId(existingPortfolio.getId()); // Ignore ID of request body
        updateIfNotNull(existingPortfolio::setName, newPortfolio.getName());

        return portfolioRepository.save(existingPortfolio);
    }

    @Override
    public List<Portfolio> retrievePortfoliosByUserId(Long userId) {
        UserServiceImp.unwrapUser(userId, userRepository.findById(userId));
        return portfolioRepository.findByUserId(userId);
    }

    @Override
    public void removePortfolio(Long portfolioId, Long userId) {
        Portfolio portfolio = unwrapPortfolio(portfolioId, portfolioRepository.findByIdAndUserId(portfolioId, userId));
        portfolioRepository.delete(portfolio);
    }

    // ensure portfolio exists and is owned by the calling entity
    public static Portfolio validateAndGetPortfolio(Optional<Portfolio> optionalPortfolio) {
        return optionalPortfolio.orElseThrow(() -> new OwnershipException(Portfolio.class));
    }

    public static Portfolio unwrapPortfolio(Long portfolioId, Optional<Portfolio> optPorfolio) {
        if(optPorfolio.isPresent()) return optPorfolio.get();
        else throw new EntityNotFoundException(portfolioId, Portfolio.class);
    } 
}

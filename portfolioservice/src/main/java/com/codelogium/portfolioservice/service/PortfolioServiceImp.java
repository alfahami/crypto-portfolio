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
    public Portfolio retrievePortfolio(Long id) {
        return unwrapPortfolio(id, portfolioRepository.findById(id));
    }

    @Transactional
    @Override
    public Portfolio updatePortfolio(Long id, Portfolio newPortfolio) {

        Portfolio existingPortfolio = unwrapPortfolio(id, portfolioRepository.findById(id));
        newPortfolio.setId(existingPortfolio.getId()); // Ignore ID of request body
        updateIfNotNull(existingPortfolio::setName, newPortfolio.getName());

        return portfolioRepository.save(existingPortfolio);
    }

    @Override
    public List<Portfolio> retrivePortfolioByUserId(Long id) {
        UserServiceImp.unwrapUser(id, userRepository.findById(id));
        return portfolioRepository.findByUserId(id);
    }

    @Override
    public void removePortfolio(Long id) {
        Portfolio portfolio = unwrapPortfolio(id, portfolioRepository.findById(id));
        portfolioRepository.delete(portfolio);
    }

    public static Portfolio validateAndReturnPortfolioOwnership(Optional<Portfolio> optionalPortfolio) {
        if(optionalPortfolio.isPresent()) return optionalPortfolio.get();
        else throw new OwnershipException(Portfolio.class);
    }

    public static Portfolio unwrapPortfolio(Long id, Optional<Portfolio> optPorfolio) {
        if(optPorfolio.isPresent()) return optPorfolio.get();
        else throw new EntityNotFoundException(id, Portfolio.class);
    } 
}

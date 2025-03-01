package com.codelogium.portfolioservice.service;

import org.apache.catalina.User;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.codelogium.portfolioservice.entity.Portfolio;
import com.codelogium.portfolioservice.exception.ResourceNotFoundException;
import com.codelogium.portfolioservice.respository.PortfolioRepository;
import com.codelogium.portfolioservice.respository.UserRepository;

import lombok.AllArgsConstructor;

/**
 * Service responsible for validating entity existence and authorization relationships.
 * 
 * This service explicitly uses singleton scope, ensuring a single instance is shared throughout the application. 
 * This is technically redundant since singleton is Spring's default scope, but explicitly declared here for clarity.
 */

@Service
@AllArgsConstructor
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ValidationAndAuthorizationService {
    
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;

    public Portfolio validateUserAndGetPortfolio(Long portfolioId, Long userId) {
        // First validate user exists
        validateUserExists(userId);

        // Get portfolio with ownership using the static unwrap
        return PortfolioServiceImp.unwrapPortfolio(portfolioId, portfolioRepository.findByIdAndUserId(portfolioId, userId));
    }

    public void validateUserExists(Long userId) {
        if(!userRepository.existsById(userId)) throw new ResourceNotFoundException(userId, User.class);
    }

}

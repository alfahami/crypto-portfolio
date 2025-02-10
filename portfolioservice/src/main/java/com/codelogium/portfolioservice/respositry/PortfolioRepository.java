package com.codelogium.portfolioservice.respositry;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelogium.portfolioservice.entity.Portfolio;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    // Fetches all portfolio that belongs to a user by user id
    List<Portfolio> findByUserId(Long userId);
    // Retrieves a portfolio by its ID, ensuring it belongs to the specified user.
    Optional<Portfolio> findByIdAndUserId(Long portfolioId, Long userId);
}

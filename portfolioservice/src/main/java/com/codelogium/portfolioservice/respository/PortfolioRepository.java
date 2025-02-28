package com.codelogium.portfolioservice.respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codelogium.portfolioservice.entity.Portfolio;
import com.codelogium.portfolioservice.entity.User;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    // Fetches all portfolio that belongs to a user by user id
    List<Portfolio> findByUserId(Long userId);

    @Query("SELECT p.user FROM Portfolio p WHERE p.id = :portfolioId")
    Optional<User> findUserByPortfolioId(@Param("portfolioId") Long portfolioId);

    // Retrieves a portfolio by its ID, ensuring it belongs to the specified user
    Optional<Portfolio> findByIdAndUserId(Long portfolioId, Long userId);
}

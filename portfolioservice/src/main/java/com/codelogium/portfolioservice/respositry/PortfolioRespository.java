package com.codelogium.portfolioservice.respositry;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelogium.portfolioservice.entity.Portfolio;

public interface PortfolioRespository extends JpaRepository<Portfolio, Long> {
    // Fetches all portfolio that belongs to a user by user id
    List<Portfolio> findByUserId(Long userId);
}

package com.codelogium.portfolioservice.respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelogium.portfolioservice.entity.Holding;

public interface HoldingRepository extends JpaRepository<Holding, Long> {

    // Fetched all holdings that belongs to a portfolio by portfolio id
    List<Holding> findByPortfolioId(Long portfolioId);

    /*
     * Retrieves a holding by its ID, ensuring it belogns to the specified portfolio
     * and user. Helps enforce ownership and prevents unauthorized access
     */
    Optional<Holding> findByIdAndPortfolioIdAndPortfolioUserId(Long holdingId, Long portfolioId, Long userId);
}

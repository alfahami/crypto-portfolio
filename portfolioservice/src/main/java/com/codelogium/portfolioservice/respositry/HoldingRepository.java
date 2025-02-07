package com.codelogium.portfolioservice.respositry;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelogium.portfolioservice.entity.Holding;

public interface HoldingRepository extends JpaRepository<Holding, Long> {
    
    // Fetched all holdings that belongs to a portfolio by portfolio id
    List<Holding> findByPortfolioId(Long id);
}

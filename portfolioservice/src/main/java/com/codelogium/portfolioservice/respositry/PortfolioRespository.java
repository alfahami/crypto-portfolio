package com.codelogium.portfolioservice.respositry;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelogium.portfolioservice.entity.Portfolio;


public interface PortfolioRespository extends JpaRepository<Long, Portfolio> {
    
}

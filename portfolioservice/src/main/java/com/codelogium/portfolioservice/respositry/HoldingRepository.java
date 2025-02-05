package com.codelogium.portfolioservice.respositry;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelogium.portfolioservice.entity.Holding;

public interface HoldingRepository extends JpaRepository<Holding, Long> {
    
}

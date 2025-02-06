package com.codelogium.portfolioservice.service;

import com.codelogium.portfolioservice.entity.User;

public interface UserService {
    User addUser(User newUser);
    User getUser(Long id);
    User addPortfolioToUser(Long userId, Long portfolioId);
} 
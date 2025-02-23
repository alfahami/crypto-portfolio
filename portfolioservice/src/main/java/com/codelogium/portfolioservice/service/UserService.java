package com.codelogium.portfolioservice.service;

import com.codelogium.portfolioservice.entity.User;

public interface UserService {
    User createUser(User newUser);

    User retrieveUser(Long id);

    User updateUser(Long userId, User newUser);

    void removeUser(Long id);
}
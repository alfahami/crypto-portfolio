package com.codelogium.portfolioservice.service;

import org.springframework.stereotype.Service;

import com.codelogium.portfolioservice.entity.User;
import com.codelogium.portfolioservice.respositry.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {
    
    private UserRepository userRepository;

    @Override
    public User addUser(User newUser) {
        return userRepository.save(newUser);
    }
    

}

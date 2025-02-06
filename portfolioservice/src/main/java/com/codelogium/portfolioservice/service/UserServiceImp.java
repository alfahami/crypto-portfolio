package com.codelogium.portfolioservice.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codelogium.portfolioservice.entity.User;
import com.codelogium.portfolioservice.exception.EntityNotFoundException;
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

    @Override
    public User addPortfolioToUser(Long userId, Long portfolioId) {
        // TODO Auto-generated method stub
        return null;
    }

    public static User unwrapUser(Long id, Optional<User> optionalUser) {
        if(optionalUser.isPresent()) return optionalUser.get();
        else throw new EntityNotFoundException(id, User.class);
    }
    

}

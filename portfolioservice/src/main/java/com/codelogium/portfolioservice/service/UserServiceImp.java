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
    public User createUser(User newUser) {
        return userRepository.save(newUser);
    }

    @Override
    public User getUser(Long id) {
        return unwrapUser(id, userRepository.findById(id));
    }

    @Override
    public User updateUser(Long id, User newUser) {
        //checks for not found entity
        unwrapUser(id, userRepository.findById(id));
        newUser.setId(id); // ignore ID request in the body as it might be intentionally changed
        return userRepository.save(newUser);
    }

    public static User unwrapUser(Long id, Optional<User> optionalUser) {
        if(optionalUser.isPresent()) return optionalUser.get();
        else throw new EntityNotFoundException(id, User.class);
    }
}

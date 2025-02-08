package com.codelogium.portfolioservice.service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codelogium.portfolioservice.entity.Portfolio;
import com.codelogium.portfolioservice.entity.User;
import com.codelogium.portfolioservice.exception.EntityNotFoundException;
import com.codelogium.portfolioservice.respositry.PortfolioRespository;
import com.codelogium.portfolioservice.respositry.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {
    
    private UserRepository userRepository;
    private PortfolioRespository portfolioRespository;

    @Override
    public User createUser(User newUser) {
        return userRepository.save(newUser);
    }

    @Override
    public User getUser(Long id) {
        return unwrapUser(id, userRepository.findById(id));
    }


    @Override
    public User addPortfolioToUser(Long userId, Long portfolioId) {
        
        User user = getUser(userId);
        Portfolio portfolio = PortfolioServiceImp.unwrapPortfolio(portfolioId, portfolioRespository.findById(portfolioId));

        user.getPortfolios().add(portfolio);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, Map<String, Object> updates) {
        User retrievedUser = unwrapUser(id, userRepository.findById(id));

        for(Map.Entry<String,Object> entry : updates.entrySet()) {
            switch (entry.getKey()) {
                case "fullName":
                    retrievedUser.setFullName((String) entry.getValue());
                    break;
                case "birthDate":
                    retrievedUser.setBirthDate((LocalDate) entry.getValue());
                case "profession":
                    retrievedUser.setProfession((String) entry.getValue());
                default:
                    break;
            }
        }
        return userRepository.save(retrievedUser);
    }

    public static User unwrapUser(Long id, Optional<User> optionalUser) {
        if(optionalUser.isPresent()) return optionalUser.get();
        else throw new EntityNotFoundException(id, User.class);
    }
}

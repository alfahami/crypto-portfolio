package com.codelogium.portfolioservice.mapper;

import org.springframework.stereotype.Component;

import com.codelogium.portfolioservice.dto.UserDto;
import com.codelogium.portfolioservice.entity.User;

@Component
public class UserMapper {
    
    public User toEntity(UserDto dto) {

        if(dto == null) return null;
        User user = new User();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setBirthDate(dto.getBirthDate());
        user.setProfession(dto.getProfession());
        return user;
    }

    public UserDto toDTO(User user) {
        
        if(user == null) return null;

        UserDto dto = new UserDto();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setProfession(user.getProfession());
        dto.setBirthDate(user.getBirthDate());

        return dto;
    }


    
}

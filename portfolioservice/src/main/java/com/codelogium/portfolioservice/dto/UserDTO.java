package com.codelogium.portfolioservice.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String firstname;
    private String lastname;
    private LocalDate birthDate;
    private String profession;
}

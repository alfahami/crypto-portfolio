package com.codelogium.portfolioservice.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    @NonNull
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NonNull
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @Past
    @NonNull
    private LocalDate birthDate;

    @NonNull
    @NotBlank(message = "Profession cannot be blank")
    private String profession;
}

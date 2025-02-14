package com.codelogium.portfolioservice.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name cannot be null blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be null or blank")
    private String lastName;

    @Past
    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;

    @NotBlank(message = "Profession cannot be null or blank")
    private String profession;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) 
    private List<Portfolio> portfolios = new ArrayList<>(); // One User -> Many Portfolios | prevents orphanRemoval errors
}

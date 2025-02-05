package com.codelogium.portfolioservice.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.annotation.Nonnull;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @NotBlank(message = "Full Name cannot be blank")
    private String fullName;
    @Nonnull
    private LocalDate birthDate;
    @Nonnull
    @NotBlank(message = "Profession cannot be blank")
    private String profession;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) 
    private Set<Portfolio> portfolios; // One User -> Many Portfolios
}

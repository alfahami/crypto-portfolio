package com.codelogium.portfolioservice.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "users")
public class User {

    private Long id;
    private String fullName;
    private LocalDate birthDate;
    private String profession;

    private Set<Portfolio> portfolios;

}

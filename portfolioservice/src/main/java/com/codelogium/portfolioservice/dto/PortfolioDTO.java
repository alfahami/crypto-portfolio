package com.codelogium.portfolioservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class PortfolioDTO {
    @NonNull
    private Long id;

    @NonNull
    @NotBlank(message = "Portfolio name cannot be blank")
    private String name;
}

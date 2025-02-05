package com.codelogium.portfolioservice.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "portfolios")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank(message = "portfolio name cannot be blank")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id") // creates a foreign key
    private User user; //each portfolio -> One User

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Holding> holdings;

    //Add holding to the portfolio with initialization check
    public void addHolding(Holding holding) {
        if(this.holdings == null) {
            this.holdings = new ArrayList<>();
        }
        this.holdings.add(holding);
    }

}

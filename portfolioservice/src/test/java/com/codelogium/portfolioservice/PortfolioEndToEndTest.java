package com.codelogium.portfolioservice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.codelogium.portfolioservice.entity.Holding;
import com.codelogium.portfolioservice.entity.Portfolio;
import com.codelogium.portfolioservice.entity.User;
import com.codelogium.portfolioservice.respositry.HoldingRepository;
import com.codelogium.portfolioservice.respositry.PortfolioRepository;
import com.codelogium.portfolioservice.respositry.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class PortfolioEndToEndTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private HoldingRepository holdingRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    private static final User USER_1 = new User(null, "ELogie", "Assomptions", LocalDate.parse("1993-09-23"), "Music Producer", null);
    private static final User USER_2 = new User(null, "Dakoine", "Toihir", LocalDate.parse("1995-10-25"), "Physist", null);
    private static final User USER_3 = new User(null, "Driss", "Boumlik", LocalDate.parse("1999-12-01"), "Porgrammer", null);

    private static final Portfolio PORTFOLIO_1 = new Portfolio(null, "Tech Stock Investment", null, null);
    private static final Portfolio PORTFOLIO_2 = new Portfolio(null, "Piriform Assets", null, null);
    private static final Portfolio PORTFOLIO_3 = new Portfolio(null, "Lodrige Caretaker Group", null, null);

    private final static Holding HOLDING_1 = new Holding(null, "BTC", BigDecimal.valueOf(213.45), null);
    private final static Holding HOLDING_2 = new Holding(null, "ETH", BigDecimal.valueOf(195.50), null);
    private final static Holding HOLDING_3 = new Holding(null, "ALL", BigDecimal.valueOf(515.60), null);

    @BeforeEach
    void setUp() {
        userRepository.saveAll(List.of(USER_1, USER_2, USER_3));
        portfolioRepository.saveAll(List.of(PORTFOLIO_1, PORTFOLIO_2, PORTFOLIO_3));
        holdingRepository.saveAll(List.of(HOLDING_1, HOLDING_2, HOLDING_3));
        
    }

    @AfterEach
    void clearDatabases() {
        userRepository.deleteAll();
        portfolioRepository.deleteAll();
    }

    @Test
    public void shouldPostPortfolioSuccessfully() throws Exception {
        User user = userRepository.findById(1L).get();

        String data = objectMapper.writeValueAsString(new Portfolio(null, "CodeLogium Investment", user, new ArrayList<>()));

        RequestBuilder request = MockMvcRequestBuilders.post("/users/1/portfolios").contentType(MediaType.APPLICATION_JSON_VALUE).content(data);

        mockMvc.perform(request).andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("CodeLogium Investment"));
    }

    @Test
    void shouldFailWhenCreatingPortfolio() throws Exception {
        User user = userRepository.findById(1L).get();

        String data = objectMapper.writeValueAsString(new Portfolio(null, "   ", user, null));

        RequestBuilder request = MockMvcRequestBuilders.post("/users/1/portfolios").contentType(MediaType.APPLICATION_JSON_VALUE).content(data);

        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetPortfolioSuccessfully() throws Exception {
        User user = userRepository.findById(1L).get();
        Portfolio portfolio = portfolioRepository.findById(2L).get();
        portfolio.setUser(user);
        portfolioRepository.save(portfolio);

        RequestBuilder request = MockMvcRequestBuilders.get("/users/1/portfolios/2");

        mockMvc.perform(request).andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Piriform Assets"))
        .andExpect(jsonPath("$.id").value(2L));
    }






}

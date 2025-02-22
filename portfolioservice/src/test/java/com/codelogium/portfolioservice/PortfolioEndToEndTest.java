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

    private User testUser;
    private Portfolio testPortfolio;
    private Holding testHolding1;
    private Holding testHolding2;


    @BeforeEach
    void setUp() {

        cleanupDatabases();
        
        testUser =userRepository.save(new User(null, "Driss", "Boumlik", LocalDate.parse("1999-12-01"), "Porgrammer", null));

        testPortfolio = portfolioRepository.save(new Portfolio(null, "Tech Stock Investment", null, null));

        testHolding1 = holdingRepository.save(new Holding(null, "BTC", BigDecimal.valueOf(213.45), null));

        testHolding2 = holdingRepository.save(new Holding(null, "ETH", BigDecimal.valueOf(195.50), null));
        
    }

    @AfterEach
    void cleanupDatabases() {
        userRepository.deleteAll();
        portfolioRepository.deleteAll();
    }

    @Test
    public void shouldPostPortfolioSuccessfully() throws Exception {
        User user = userRepository.findById(testUser.getId()).get();

        String data = objectMapper.writeValueAsString(
            new Portfolio(null, "CodeLogium Investment", user, new ArrayList<>()));

        RequestBuilder request = MockMvcRequestBuilders
                .post("/users/" + testUser.getId() + "/portfolios")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(data);

        mockMvc.perform(request)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("CodeLogium Investment"));
    }

    @Test
    void shouldFailWhenCreatingPortfolio() throws Exception {
        User user = userRepository.findById(testUser.getId()).get();

        String data = objectMapper.writeValueAsString(new Portfolio(null, "   ", user, null));

        RequestBuilder request = MockMvcRequestBuilders.post("/users/"+ testUser.getId() + " /portfolios").contentType(MediaType.APPLICATION_JSON_VALUE).content(data);

        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetPortfolioSuccessfully() throws Exception {
        addUserToPortfolio(testUser.getId(), testPortfolio.getId());

        RequestBuilder request = MockMvcRequestBuilders
            .get("/users/"+ testUser.getId() + "/portfolios/" + testPortfolio.getId());

        mockMvc.perform(request).andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Tech Stock Investment"))
        .andExpect(jsonPath("$.id").value(testPortfolio.getId()));
    }

    @Test
    void shouldUpdatePortfolioSuccessfully() throws Exception {

        Portfolio portfolio = addUserToPortfolio(testUser.getId(), testPortfolio.getId());

        portfolio.setId(3L); // intentionally tampering the id of the request body
        portfolio.setName("Crack Software Inc. Investment");
        String requestData = objectMapper.writeValueAsString(portfolio);

        RequestBuilder request = MockMvcRequestBuilders
                                .patch("/users/" + testUser.getId() +"/portfolios/" + testPortfolio.getId())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(requestData);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testPortfolio.getId()))
                .andExpect(jsonPath("$.name").value("Crack Software Inc. Investment"));
    }

    @Test
    void shouldFailWhenRetrievingPortfolio() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/users/portfolios/99");

        mockMvc.perform(request).andExpect(status().isNotFound());
    }

    @Test
    void shouldRemoveUserSuccessfully() throws Exception {

        addUserToPortfolio(testUser.getId(), testPortfolio.getId());
        
        RequestBuilder request = MockMvcRequestBuilders.delete("/users/" + testUser.getId() + "/portfolios/" + testPortfolio.getId());

        mockMvc.perform(request).andExpect(status().isNoContent());
    }

    @Test
    void shouldCalculateValuation() throws Exception {
        Portfolio portfolio = addUserToPortfolio(testUser.getId(), testPortfolio.getId());

        addHoldingsToPortfolio(portfolio.getId(), testHolding1.getId());
        addHoldingsToPortfolio(portfolio.getId(), testHolding2.getId());

        RequestBuilder request = MockMvcRequestBuilders.get("/users/" + testUser.getId() + "/portfolios/" + testPortfolio.getId() + "/valuation").queryParam("base", "EUR");

        mockMvc.perform(request).andExpect(status().isOk());
    }

    @Test
    void shouldGetAllPortfolios() throws Exception {
        addUserToPortfolio(testUser.getId(), testPortfolio.getId());
        
        RequestBuilder request = MockMvcRequestBuilders.get("/users/" + testUser.getId() + "/portfolios/all");

        mockMvc.perform(request).andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(1))
        .andExpect(jsonPath("$.[?(@.name == \"Tech Stock Investment\")]").exists());
    }

    private Portfolio addUserToPortfolio(Long userId, Long portfolioId) {

        User user = userRepository.findById(userId).orElseThrow();
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow();
        portfolio.setUser(user);
        return portfolioRepository.save(portfolio); 
    }

    private Portfolio addHoldingsToPortfolio(Long portfolioId, Long holdingId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow();
        Holding holding = holdingRepository.findById(holdingId).orElseThrow();

        portfolio.setHoldings(List.of(holding));
        return portfolioRepository.save(portfolio);
    }
}

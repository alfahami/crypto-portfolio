package com.codelogium.portfolioservice;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.codelogium.portfolioservice.entity.Holding;
import com.codelogium.portfolioservice.entity.Portfolio;
import com.codelogium.portfolioservice.entity.User;
import com.codelogium.portfolioservice.service.HoldingService;
import com.codelogium.portfolioservice.service.PortfolioService;
import com.codelogium.portfolioservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.mockwebserver.MockWebServer;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class PortfolioEndToEndTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private HoldingService holdingService;

    @Autowired
    private UserService userService;

    ObjectMapper objectMapper = new ObjectMapper();

    private User testUser;
    private Portfolio testPortfolio;

    private static MockWebServer mockWebServer;


    @BeforeEach
    void setUp() throws Exception {
        tearDown();

        mockWebServer.start(9090);
        // Inject MockWebServer URL into the service
        String baseUrl = mockWebServer.url("/").toString();
        System.setProperty("exchangerate.base.url", baseUrl);

        
        testUser = userService.createUser(new User(null, "Driss", "Boumlik", LocalDate.parse("1999-12-01"), "Porgrammer", null));

        // Seed databases
        testPortfolio = portfolioService.createPortfolio(testUser.getId(), new Portfolio(null, "Tech Stock Investment", null, null));

        holdingService.createHolding(testPortfolio.getId(), testUser.getId(), new Holding(null, "BTC", BigDecimal.valueOf(213.45), null));

        holdingService.createHolding(testPortfolio.getId(), testUser.getId(), new Holding(null, "ETH", BigDecimal.valueOf(195.50), null));
        
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void shouldPostPortfolioSuccessfully() throws Exception {
        String data = objectMapper.writeValueAsString(
            new Portfolio(null, "CodeLogium Investment", testUser, null));

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
        String data = objectMapper.writeValueAsString(new Portfolio(null, "   ", testUser, null));

        RequestBuilder request = MockMvcRequestBuilders.post("/users/"+ testUser.getId() + " /portfolios").contentType(MediaType.APPLICATION_JSON_VALUE).content(data);

        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetPortfolioSuccessfully() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
            .get("/users/"+ testUser.getId() + "/portfolios/" + testPortfolio.getId());

        mockMvc.perform(request).andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Tech Stock Investment"))
        .andExpect(jsonPath("$.id").value(testPortfolio.getId()));
    }

    @Test
    void shouldUpdatePortfolioSuccessfully() throws Exception {
        testPortfolio.setName("Crack Software Inc. Investment");
        String requestData = objectMapper.writeValueAsString(testPortfolio);

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
        RequestBuilder request = MockMvcRequestBuilders.delete("/users/" + testUser.getId() + "/portfolios/" + testPortfolio.getId());

        mockMvc.perform(request).andExpect(status().isNoContent());
    }

    @Test
    void shouldCalculateValuation() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/users/" + testUser.getId() + "/portfolios/" + testPortfolio.getId() + "/valuation").queryParam("base", "EUR");

        mockMvc.perform(request).andExpect(status().isOk());
    }

    @Test
    void shouldGetAllPortfolios() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/users/" + testUser.getId() + "/portfolios/all");

        mockMvc.perform(request).andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(1))
        .andExpect(jsonPath("$.[?(@.name == \"Tech Stock Investment\")]").exists());
    }
}

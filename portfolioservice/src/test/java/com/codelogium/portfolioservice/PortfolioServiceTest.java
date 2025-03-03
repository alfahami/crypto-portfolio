package com.codelogium.portfolioservice;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.codelogium.portfolioservice.entity.Holding;
import com.codelogium.portfolioservice.entity.Portfolio;
import com.codelogium.portfolioservice.entity.User;
import com.codelogium.portfolioservice.exception.ResourceNotFoundException;
import com.codelogium.portfolioservice.respository.PortfolioRepository;
import com.codelogium.portfolioservice.respository.UserRepository;
import com.codelogium.portfolioservice.service.PortfolioService;
import com.codelogium.portfolioservice.service.PortfolioServiceImp;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@ExtendWith(SpringExtension.class)
public class PortfolioServiceTest {

    // Initialze Mock web server
    private static MockWebServer mockWebServer;
    private PortfolioService portfolioService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PortfolioRepository portfolioRepository;

    @BeforeEach
    void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start(8585);

        WebClient mockedWebClient = WebClient.builder().baseUrl(mockWebServer.url("/").toString())
                .build();

        portfolioService = new PortfolioServiceImp(portfolioRepository, userRepository, mockedWebClient);

    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.close();
        mockWebServer.shutdown();
    }

    @Test
    void shouldGetValuationSuccessfully() {
        // Mock
        User testUser = new User(1L, "John", "Doe", LocalDate.parse("1999-09-24"), "Developer", null);

        Portfolio portfolio = new Portfolio(10L, "Tech Guru Investment", testUser, new ArrayList<>());

        Holding holding1 = new Holding(1L, "BTC", new BigDecimal("21.43"), portfolio);
        Holding holding2 = new Holding(2L, "ETH", new BigDecimal("45.12"), portfolio);

        portfolio.setHoldings(List.of(holding1, holding2));
        
        when(portfolioRepository.findUserByPortfolioId(10L)).thenReturn(Optional.of(testUser));
        when(portfolioRepository.findByIdAndUserId(10L, 1L)).thenReturn(Optional.of(portfolio));

        // Mock API for BTC
        mockWebServer.enqueue(
                new MockResponse().setResponseCode(HttpStatus.OK.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody("{\"price\": 1259.54}"));

        // Mock API for ETH
        mockWebServer.enqueue(
                new MockResponse().setResponseCode(HttpStatus.OK.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody("{\"price\": 895.35}"));

        // AcT
        BigDecimal result = portfolioService.valuation(10L, 1L, "EUR");

        // Assert
        assertEquals(new BigDecimal("67390.1342"), result);
    }

    @Test
    void shouldFailToGetValuationWhenPortfolioHasNoHoldings() {
        // Mock
        User testUser = new User(1L, "John", "Doe", LocalDate.parse("1999-09-24"), "Developer", null);

        Portfolio portfolio = new Portfolio(10L, "Tech Guru Investment", testUser, null);

        when(portfolioRepository.findUserByPortfolioId(10L)).thenReturn(Optional.of(testUser));
        when(portfolioRepository.findByIdAndUserId(10L, 1L)).thenReturn(Optional.of(portfolio));

        // Act
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            portfolioService.valuation(10L, 1L, "MAD");
        });

        // Assert
        assertEquals("Portfolio doesn't have holdings yet", exception.getMessage());
    }

    @Test
    void shouldCreatePortfolioSuccessfully() {
        // Mock
        User testUser = new User(1L, "John", "Doe", LocalDate.parse("1999-09-24"), "Developer", null);
        Portfolio portfolio = new Portfolio(1L, "Tech Guru Investment", testUser, new ArrayList<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(portfolioRepository.save(portfolio)).thenReturn(portfolio);
        // Act
        Portfolio createdPortfolio = portfolioService.createPortfolio(1L, portfolio);

        // Assert
        assertNotNull(createdPortfolio);
        assertEquals("Tech Guru Investment", createdPortfolio.getName());
        verify(portfolioRepository, times(1)).save(any(Portfolio.class));
    }

    @Test
    void shouldFailtToCreatePortfolioWhenUserNotFound() {

        Exception exception = assertThrows(RuntimeException.class, () -> {
            portfolioService.createPortfolio(999L, new Portfolio());
        });

        assertEquals("The user with the id 999 is not found.", exception.getMessage());
    }

    @Test
    void shouldRetrievePortfolioSuccessfully() {
        // Mock
        User testUser = new User(1L, "John", "Doe", LocalDate.parse("1999-09-24"), "Developer", null);
        Portfolio portfolio = new Portfolio(1L, "Tech Guru Investment", testUser, new ArrayList<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(portfolioRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of((portfolio)));

        // Act
        Portfolio result = portfolioService.retrievePortfolio(1L, 1L);

        // Assert
        assertEquals(result.getId(), portfolio.getId());
        assertEquals(result.getName(), portfolio.getName());
    }

    @Test
    void shouldFailToRetrievePortfolioWhenPortfolioNotExist() {
        // Mock
        User testUser = new User(1L, "John", "Doe", LocalDate.parse("1999-09-24"), "Developer", null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            portfolioService.retrievePortfolio(999L, 1L);
        });

        assertEquals("The portfolio with the id 999 is not found.", exception.getMessage());
    }

    @Test
    void shouldUpdatePortfolioSuccessfully() {
        // Mock
        User testUser = new User(1L, "John", "Doe", LocalDate.parse("1999-09-24"), "Developer", null);
        Portfolio portfolio = new Portfolio(1L, "Tech Guru Investment", testUser, new ArrayList<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(portfolioRepository.save(portfolio)).thenReturn(portfolio);
        when(portfolioRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(portfolio));

        Portfolio retrievedPortfolio = portfolioRepository.findByIdAndUserId(1L, 1L).get();

        retrievedPortfolio.setName("Medium Tech Stock");
        retrievedPortfolio.setId(999L); // tampering the ID in order to test if it gets ignored by the service

        when(portfolioRepository.save(any(Portfolio.class))).thenReturn(retrievedPortfolio);

        // Act
        Portfolio result = portfolioService.updatePortfolio(1L, 1L, retrievedPortfolio);

        // Assert
        assertEquals("Medium Tech Stock", result.getName());
    }
}

package com.codelogium.portfolioservice;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.codelogium.portfolioservice.entity.Portfolio;
import com.codelogium.portfolioservice.entity.User;
import com.codelogium.portfolioservice.respositry.PortfolioRepository;
import com.codelogium.portfolioservice.respositry.UserRepository;
import com.codelogium.portfolioservice.service.PortfolioService;
import com.codelogium.portfolioservice.service.PortfolioServiceImp;

import okhttp3.mockwebserver.MockWebServer;

@ExtendWith(SpringExtension.class)
public class PortfolioServiceTest {
    
    // Initialze Mock web server
    private static MockWebServer mockWebServer;
    private  PortfolioService portfolioservice;
    
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

        portfolioservice = new PortfolioServiceImp(portfolioRepository, userRepository, mockedWebClient);

    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.close();
        mockWebServer.shutdown();
    }

    @Test
    void shouldCreatePortfolioSuccessfully() {
        // Mock
        User testUser = new User(1L, "John", "Doe", LocalDate.parse("1999-09-24"), "Developer", null);
        Portfolio portfolio = new Portfolio(1L, "Tech Guru Investment", testUser, new ArrayList<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(portfolioRepository.save(portfolio)).thenReturn(portfolio);

        // Act
        Portfolio createdPortfolio = portfolioservice.createPortfolio(1L, portfolio);

        // Assert
        assertNotNull(createdPortfolio);
        assertEquals("Tech Guru Investment", createdPortfolio.getName());
        verify(portfolioRepository, times(1)).save(any(Portfolio.class));   
    }

    @Test
    void shouldFailtToCreatePortfolioWhenUserNotFound() {

        Exception exception = assertThrows(RuntimeException.class, () -> {
            portfolioservice.createPortfolio(999L, new Portfolio());
        });

        assertEquals("The user with the id 999 is not found.", exception.getMessage());
    }

    @Test
    void shouldRetrievePortfolioSuccessfully() {
        //Mock 
        User testUser = new User(1L, "John", "Doe", LocalDate.parse("1999-09-24"), "Developer", null);
        Portfolio portfolio = new Portfolio(1L, "Tech Guru Investment", testUser, new ArrayList<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(portfolioRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of((portfolio)));
        
        // Act
        Portfolio result = portfolioservice.retrievePortfolio(1L, 1L);

        //Assert
        assertEquals(result.getId(), portfolio.getId());
        assertEquals(result.getName(), portfolio.getName());
    }

    @Test
    void shouldFailToRetrievePortfolioWhenPortfolioNotExist() {
        //Mock 
        User testUser = new User(1L, "John", "Doe", LocalDate.parse("1999-09-24"), "Developer", null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            portfolioservice.retrievePortfolio(999L, 1L);
        });

        assertEquals("The portfolio with the id 999 is not found.", exception.getMessage());
    }
}

package com.codelogium.portfolioservice;

import java.time.LocalDate;
import java.util.ArrayList;

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

import com.codelogium.portfolioservice.entity.Portfolio;
import com.codelogium.portfolioservice.entity.User;
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

    ObjectMapper objectMapper = new ObjectMapper();

    private User[] users = new User[] {
        new User(null, "ELogie", "Assomptions", LocalDate.parse("1993-09-23"), "Music Producer", null),
        new User(null, "Dakoine", "Toihir", LocalDate.parse("1995-10-25"), "Physist", null),
        new User(null, "Driss", "Boumlik", LocalDate.parse("1999-12-01"), "Porgrammer", null)

    };

    @BeforeEach
    void setUpUsers() {
        for (int i = 0; i < users.length; i++) {
            userRepository.save(users[i]);
        }
    }

    @AfterEach
    void clearUsers() {
        userRepository.deleteAll();
        portfolioRepository.deleteAll();
    }

    @Test
    public void shouldPostPortfolioSuccessfully() throws Exception {
        User user = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("User not found"));

        String data = objectMapper.writeValueAsString(new Portfolio(null, "CodeLogium Investment", user, new ArrayList<>()));

        RequestBuilder request = MockMvcRequestBuilders.post("/users/1/portfolios").contentType(MediaType.APPLICATION_JSON_VALUE).content(data);

        mockMvc.perform(request).andExpect(status().isCreated());
        
    }
}

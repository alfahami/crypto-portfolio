package com.codelogium.portfolioservice;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.codelogium.portfolioservice.service.PortfolioServiceImp;
import com.codelogium.portfolioservice.web.PortfolioController;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {PortfolioController.class, PortfolioServiceImp.class})
@WebMvcTest
public class PortfolioEndToEndTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void postPrtfolio() {
        
    }



}

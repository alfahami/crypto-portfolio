package com.codelogium.portfolioservice.web;

import org.springframework.web.bind.annotation.RestController;

import com.codelogium.portfolioservice.service.HoldingService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class HoldingController {
    
    private HoldingService holdingService;

}

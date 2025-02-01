package com.codelogium.exchangerateservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExchangeRateEndToEndApiTest {

    @Autowired
    private WebTestClient webTestClient;

    /*
     * Validate that the endpoint /exchange-rate/latest is responding correctly with the correct HTTP status, content type, and expected JSON structure. 
     */
    @Test
    void getRawData_ShouldReturnSuccessfulResponse() {
        webTestClient
            .get()
            .uri("/exchange-rate/latest")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType("application/json; charset=utf-8")
            .expectBody()
            .jsonPath("$.data").exists();
    }

    /*
     * Validate that the endpoint /exchange-rate/latest with error code. 
     */
    @Test
    void getRawData_ShouldReturnErrorResponse() {
        webTestClient.get()
                    .uri("/exchange-rate/lastest")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().is4xxClientError()
                    .expectHeader().contentType("application/json");
    }

}

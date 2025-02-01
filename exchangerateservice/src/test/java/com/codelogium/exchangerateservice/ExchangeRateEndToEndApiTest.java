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
            .consumeWith(response ->  {
                System.out.println("Response Body " + new String(response.getResponseBody()));
            })
            .jsonPath("$.data").exists()
            .jsonPath("$.data[0].symbol").isEqualTo("BTC");
    }

    /*
     * Validate that the endpoint /exchange-rate/latest with error code. 
     */
    @Test
    void getLastPrice_ShouldReturnInternalErrorResponse() {
        webTestClient.get()
                    .uri("/exchange-rate?symbol=INVALID&base=ERROR")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().is5xxServerError()
                    .expectHeader().contentType("application/json");
    }

}

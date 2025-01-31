package com.codelogium.exchangerateservice;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.codelogium.exchangerateservice.service.ExchangeRateService;
import com.codelogium.exchangerateservice.service.ExchangeRateServiceImpl;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
public class ExchangeRateServiceTest {

  // Initialize MockWebServer, which will act as a mock server for the WebClient.
  private static MockWebServer mockWebServer;
  private static ExchangeRateService exchangeRateService;

  @BeforeAll
  static void setUp() throws Exception {
    mockWebServer = new MockWebServer();
    mockWebServer.start(9090);

    // Create a WebClient instance with the mock server's base URL, this simulates the actual WebClient in exchangerate service.
    WebClient mockedWebClient = WebClient.builder().baseUrl(mockWebServer.url("/").toString()).build();

    // Retrieve the base URL for logging and validation purposes.
    String baseUrl = mockWebServer.url("/").toString();
    System.out.println("Base Url :" + baseUrl);

    // Initialize the ExchangeRateService with the mocked WebClient.
    exchangeRateService = new ExchangeRateServiceImpl(mockedWebClient);
  }

  @AfterAll
  static void tearDown() throws IOException {
    // Close the mock server after the tests are done to free up resources.
    mockWebServer.close();
  }

  @Test
  public void getAllDataSuccessTest() {
    // Define the mocked response that will be returned by the mock server.
    String mockResponse = getMockedResponse();

    // Enqueue a mock response to be returned when the WebClient performs the GET request.
    mockWebServer.enqueue(
        new MockResponse().setResponseCode(HttpStatus.OK.value()) // Set the response code to 200 OK
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // Set the content type header to application/json
            .setBody(getMockedResponse())); // Set the response body to the mock data

    // Call the method that will interact with the WebClient (and the mock server).
    Mono<ResponseEntity<String>> result = exchangeRateService.getAllData();

    // Verify that the result matches the expected response. StepVerifier is used for reactive streams.
    StepVerifier.create(result)
        .expectNextMatches(responseData -> responseData.getBody().contains(mockResponse));

  }

  static String getMockedResponse() {
    return """

                            {
                  "status": {
                    "timestamp": "2025-01-28T12:00:00.000Z",
                    "error_code": 0,
                    "error_message": null,
                    "elapsed": 10,
                    "credit_count": 1
                  },
                  "data": [
                    {
                      "id": 1,
                      "name": "Bitcoin",
                      "symbol": "BTC",
                      "slug": "bitcoin",
                      "num_market_pairs": 10000,
                      "date_added": "2013-04-28T00:00:00.000Z",
                      "max_supply": 21000000,
                      "circulating_supply": 19500000,
                      "total_supply": 21000000,
                      "platform": null,
                      "cmc_rank": 1,
                      "last_updated": "2025-01-28T12:00:00.000Z",
                      "quote": {
                        "USD": {
                          "price": 42000.00,
                          "volume_24h": 35000000000,
                          "percent_change_1h": -0.5,
                          "percent_change_24h": 2.3,
                          "percent_change_7d": 5.6,
                          "market_cap": 800000000000,
                          "last_updated": "2025-01-28T12:00:00.000Z"
                        }
                      }
                    },
                    {
                      "id": 2,
                      "name": "Ethereum",
                      "symbol": "ETH",
                      "slug": "ethereum",
                      "num_market_pairs": 8000,
                      "date_added": "2015-08-07T00:00:00.000Z",
                      "max_supply": null,
                      "circulating_supply": 120000000,
                      "total_supply": 120000000,
                      "platform": null,
                      "cmc_rank": 2,
                      "last_updated": "2025-01-28T12:00:00.000Z",
                      "quote": {
                        "USD": {
                          "price": 2700.00,
                          "volume_24h": 20000000000,
                          "percent_change_1h": 0.2,
                          "percent_change_24h": 1.8,
                          "percent_change_7d": 4.2,
                          "market_cap": 324000000000,
                          "last_updated": "2025-01-28T12:00:00.000Z"
                        }
                      }
                    }
                  ]
                }

        """;
  }

}

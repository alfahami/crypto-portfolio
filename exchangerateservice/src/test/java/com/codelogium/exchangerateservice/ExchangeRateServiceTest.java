package com.codelogium.exchangerateservice;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.codelogium.exchangerateservice.exception.ClientException;
import com.codelogium.exchangerateservice.mapper.CryptoResponseMapper;
import com.codelogium.exchangerateservice.service.ExchangeRateService;
import com.codelogium.exchangerateservice.service.ExchangeRateServiceImpl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
public class ExchangeRateServiceTest {

  // Initialize MockWebServer, which will act as a mock server for the WebClient.
  private static MockWebServer mockWebServer;
  private static ExchangeRateService exchangeRateService;

  @BeforeEach
  void setUp() throws Exception {
    mockWebServer = new MockWebServer();
    mockWebServer.start(9090);

    // Create a WebClient that simulates the actual WebClient in exchangerate service.
    WebClient mockedWebClient = WebClient.builder()
        .codecs(configurer -> configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder()))
        .baseUrl(mockWebServer.url("/").toString())
        .build();

    // Initialize the ExchangeRateService with the mocked WebClient.
    exchangeRateService = new ExchangeRateServiceImpl(mockedWebClient);
  }

  @AfterEach
  void tearDown() throws IOException {
    // Close the mock server after the tests are done to free up resources.
    mockWebServer.close();
  }

  @Test
  void getPriceSuccessTest() throws Exception {
    String base = "MAD";
    String symbol = "BTC";
    String mockedResponse = """
                          {
            "data": {
                "BTC": {
                    "quote": {
                        "MAD": {
                            "price": 1029310.6450322965
                        }
                    }
                }
            }
        }
                """;
    //Mock
    mockWebServer.enqueue(
        new MockResponse().setResponseCode(HttpStatus.OK.value())
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(mockedResponse));

    // Act
    Mono<CryptoResponseMapper> result = exchangeRateService.retrivePrice(symbol, base);

    // Verify
    StepVerifier.create(result)
        .expectNextMatches(response -> {
          return response.getBase().equals(base) &&
              response.getSymbol().equals(symbol) &&
              response.getPrice().compareTo(new BigDecimal("1029310.6450322965")) == 0;
        })
        .verifyComplete();
    //Assert
    RecordedRequest recordedRequest = mockWebServer.takeRequest();
    assertEquals("GET", recordedRequest.getMethod());
  }

  @Test
  void getPriceErrorTest() {
    String base = "MAD";
    String symbol = "BTC";
    mockWebServer.enqueue(
        new MockResponse().setResponseCode(429)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("{\"status\":{\"error_message\":\"You've exceeded your API Key's daily rate limit.\"}}")
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

    Mono<CryptoResponseMapper> result = exchangeRateService.retrivePrice(symbol, base);

    // Checking the error code using expectErrorMatches
    StepVerifier.create(result)
        .expectErrorMatches(throwable -> throwable instanceof ClientException &&
            ((ClientException) throwable).getStatus().is4xxClientError())
        .verify();
  }
  

  @Test
  void getAllDataSuccessTest() {
    // Define the mocked response that will be returned by the mock server.
    String mockResponse = getMockedResponse();

    // Enqueue a mock response to be returned when the WebClient performs the GET request.
    mockWebServer.enqueue(
        new MockResponse().setResponseCode(HttpStatus.OK.value())
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(getMockedResponse())); // Set the response body to the mock data

    // Call the method that will interact with the WebClient (and the mock server).
    Mono<ResponseEntity<String>> result = exchangeRateService.getAllData();

    // Verify that the result matches the expected response. StepVerifier is used for reactive streams.
    StepVerifier.create(result)
        .expectNextMatches(responseData -> responseData.getBody().contains(mockResponse)).verifyComplete();

  }

  @Test
  void getAllDataErrorTest() {

    /*
     * Test of 500 INTERNAL SERVER ERROR | 4xx client Error can be test by setting
     * the response to a client Error(ex: 404) and in stepVerfier : is4xxServerError
     * 
     */
    mockWebServer.enqueue(
        new MockResponse().setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("{\"status\":{\"error_message\":\"Internal Server Error\"}}")
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

    Mono<ResponseEntity<String>> result = exchangeRateService.getAllData();

    // Checking the error code using expectErrorMatches
    StepVerifier.create(result)
        .expectErrorMatches(throwable -> throwable instanceof ClientException &&
            ((ClientException) throwable).getStatus().is5xxServerError())
        .verify();
  }

  @Test
  void getAllDataErrorApiSubTest() {
    // Mimicking CMC API behavior for an expired API key
    mockWebServer.enqueue(
        new MockResponse().setResponseCode(402)
            .setBody("{\"status\":{\"error_message\":\"Your API Key's subscription plan has expired.\"}}")
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

    Mono<ResponseEntity<String>> result = exchangeRateService.getAllData();
    // Done in the sake of learning, checking the returned message that cmc defined themselves
    StepVerifier.create(result)
        .expectErrorMatches(throwable -> {
          if (throwable instanceof ClientException) {
            ClientException exception = (ClientException) throwable;
            return exception.getMessage().contains("Your API Key's subscription plan has expired.")
                && exception.getStatus().is4xxClientError();
          }
          return false;
        });
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

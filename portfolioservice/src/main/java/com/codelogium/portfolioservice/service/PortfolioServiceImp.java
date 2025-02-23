package com.codelogium.portfolioservice.service;

import static com.codelogium.portfolioservice.util.EntityUtils.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.codelogium.portfolioservice.entity.Holding;
import com.codelogium.portfolioservice.entity.Portfolio;
import com.codelogium.portfolioservice.entity.User;
import com.codelogium.portfolioservice.exception.ExchangeRateException;
import com.codelogium.portfolioservice.exception.ResourceNotFoundException;
import com.codelogium.portfolioservice.respository.PortfolioRepository;
import com.codelogium.portfolioservice.respository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class PortfolioServiceImp implements PortfolioService {

    private PortfolioRepository portfolioRepository;
    private UserRepository userRepository;
    private final WebClient webClient;

    @Override
    public Portfolio createPortfolio(Long userId, Portfolio portfolio) {

        User user = UserServiceImp.unwrapUser(userId, userRepository.findById(userId));
        portfolio.setUser(user);
        return portfolioRepository.save(portfolio);
    }

    @Override
    public Portfolio retrievePortfolio(Long portfolioId, Long userId) {
        UserServiceImp.unwrapUser(userId, userRepository.findById(userId));

        return unwrapPortfolio(portfolioId, portfolioRepository.findByIdAndUserId(portfolioId, userId));
    }

    // Implement a partial update as portfolio can have multiple fields
    @Transactional // Commit changes correctly or rollback if failure
    @Override
    public Portfolio updatePortfolio(Long portfolioId, Long userId, Portfolio newPortfolio) {

        UserServiceImp.unwrapUser(userId, userRepository.findById(userId));

        Portfolio existingPortfolio = unwrapPortfolio(portfolioId,
                portfolioRepository.findByIdAndUserId(portfolioId, userId));
        newPortfolio.setId(existingPortfolio.getId()); // Ignore ID of request body
        updateIfNotNull(existingPortfolio::setName, newPortfolio.getName());

        return portfolioRepository.save(existingPortfolio);
    }

    @Override
    public List<Portfolio> retrievePortfoliosByUserId(Long userId) {
        UserServiceImp.unwrapUser(userId, userRepository.findById(userId));
        return portfolioRepository.findByUserId(userId);
    }

    @Override
    public void removePortfolio(Long portfolioId, Long userId) {

        UserServiceImp.unwrapUser(userId, userRepository.findById(userId));

        Portfolio portfolio = unwrapPortfolio(portfolioId, portfolioRepository.findByIdAndUserId(portfolioId, userId));
        portfolioRepository.delete(portfolio);
    }

    @Override
    public BigDecimal valuation(Long userId, Long portfolioId, String base) {
        // check if the user exists, uri param was not tempered
        validateUserExists(userId);

        Portfolio portfolio = unwrapPortfolio(portfolioId, portfolioRepository.findByIdAndUserId(portfolioId, userId));

        // initialize the result to accumulate the total valuation
        BigDecimal result = BigDecimal.ZERO;
        if (portfolio.getHoldings() != null) {
            for (Holding holding : portfolio.getHoldings()) {
                result = result.add(holding.getAmount().multiply(getCurrentPrice(holding.getSymbol(), base)));
            }
            return result;
        } else
            throw new ResourceNotFoundException("Portfolio doesn't have holdings yet");
    }

    // Retrieves the current price of crypto using exchangerateservice local api
    public BigDecimal getCurrentPrice(String symbol, String base) {
        try {
            JsonNode data = webClient.get().uri(uriBuilder -> uriBuilder.path("/exchange-rate")
                    .queryParam("symbol", symbol)
                    .queryParam("base", base)
                    .build())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError,
                            clientResponse -> Mono.error(new ExchangeRateException("Failed to fetch exchange rate")))
                    .bodyToMono(JsonNode.class)
                    .block(); // Bolocking for simplicity (avoid in reactive flows)

            String price = data.get("price").toString();

            if (data == null || price == null) {
                throw new ExchangeRateException("Invalid Exchange rate response");
            }

            return new BigDecimal(price);
            // WebClient-Specific Exceptions (API response errors)
        } catch (WebClientResponseException ex) {
            throw new ExchangeRateException("Exchange rate service error: " + ex.getMessage(), ex);
            // Catch All Other Exceptions (e.g., NullPointerException,
            // IllegalStateException)
        } catch (Exception ex) {
            throw new ExchangeRateException("Unexpected error fetching exchange rate", ex);
        }
    }

    // Other relation down level might need to check for user existance
    public void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId))
            throw new ResourceNotFoundException(userId, User.class);
    }

    public static Portfolio unwrapPortfolio(Long portfolioId, Optional<Portfolio> optPorfolio) {
        if (optPorfolio.isPresent())
            return optPorfolio.get();
        else
            throw new ResourceNotFoundException(portfolioId, Portfolio.class);
    }
}

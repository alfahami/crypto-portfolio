# Crypto Portfolio API  

Built with **Spring Boot** and **WebClient** for seamless integration with external APIs.  

## Overview  
The **Crypto Portfolio API** allows users to manage portfolios of cryptocurrency holdings. Users can create multiple portfolios, add holdings to each portfolio, and track their valuations in different currencies. 

The solution integrates with an exchange rate service running on port `8801` and provides portfolio management endpoints on port `8080`.

The API consists of two main services:  

- **Portfolio Service** – Manages users, portfolios, and holdings.  
- **Exchange Rate Service** – Provides cryptocurrency exchange rates using [CoinMarketCap](https://coinmarketcap.com/api/documentation/v1/#section/Quick-Start-Guide).  

### ⚠ Architecture Note  
While the application follows a modular approach and runs services on separate ports, it does not fully implement a **microservices architecture** due to limited experience in that area.  

---

## Base URLs  
- **Portfolio Service**: `http://localhost:8080`  
- **Exchange Rate Service**: `http://localhost:8081`  

---

## Authentication  
This API does not currently implement authentication. 
In a production environment, appropriate authentication mechanisms (e.g., JWT, OAuth2) should be integrated.  

---

## Development Challenge  
Building this application involved:

#### ExchangerateService
- Exploring **reactive programming** concepts using `WebClient` to call an external API.  
- Writing **unit tests** and **integration tests** using `WebTestClient`.  
- Learning how to **simulate or integrate** external APIs (this implementation uses **CoinMarketCap**, a real crypto API). 

Here's an updated version with the addition of controller advice and error handling:

### **PortfolioService**
- **Exploring JPA & Hibernate** for efficient data persistence and entity relationships.  
- **Designing RESTful APIs** with proper CRUD operations for managing portfolios.  
- **Implementing transaction management** to ensure atomic updates.  
- **Integrating with ExchangeRateService** to retrieve real-time crypto prices.  
- **Writing unit and integration tests** using `JUnit`, `Mockito`, and `Spring Boot Test`.  
- **Implementing global exception handling** using `@ControllerAdvice` to catch and handle errors gracefully.  
- **Custom error responses** for better clarity and user experience when exceptions occur in the API.
---

## Build Instructions  

1. **Clone this repository**:  
   ```bash
   git clone git@github.com:alfahami/crypto-portfolio-api.git
   cd crypto-portfolio-api
   ```

2. **Build the Exchange Rate Service**:  
   ```bash
   cd exchangerateservice
   mvn clean install
   ```

3. **Build the Portfolio Service**:  
   ```bash
   cd portfolioservice
   mvn clean install
   ```

---

## Run Instructions  

1. **Run the Exchange Rate Service**:  
   ```bash
   # Terminal 1:
   cd exchangerateservice
   mvn spring-boot:run
   ```

2. **Run the Portfolio Service**:  
   ```bash
   # Terminal 2:
   cd portfolioservice
   mvn spring-boot:run
   ```

## Running Tests

To ensure the correctness of the services, you can run all unit and integration tests using the following commands.

### 1. Run All Tests for ExchangeRateService
```bash
cd exchangerateservice
mvn test
```

### 2. Run All Tests for PortfolioService
```bash
cd portfolioservice
mvn test
```




---

## Functional Requirements  

### Exchange Rate Service  
**Retrieve Crypto Prices**  
- A REST endpoint returns the current price for given crypto symbols (e.g., BTC, ETH) in a base currency (e.g., USD).   

**Endpoints**  
- `GET /exchange-rate?symbol={symbol}&base={base}` – Returns the current or last known price in the given base currency.  

### Portfolio Service  
**Manage Portfolios**  
- Users can create multiple portfolios and add crypto holdings.  
- CRUD operations: create, update, delete portfolios and holdings.  

**Portfolio Valuation**  
- Retrieves the total value of a portfolio in a specified base currency (e.g., USD).  
- Calls the **Exchange Rate Service** to fetch the latest exchange rates.  

**Endpoints**  
- `POST /portfolios` – Create a new portfolio.  
- `GET /portfolios/{id}` – Get portfolio details (including holdings).  
- `POST /portfolios/{id}/holdings` – Add a holding.  
- `DELETE /portfolios/{id}/holdings/{symbol}` – Remove a holding.  
- `GET /portfolios/{id}/valuation?base={base}` – Get total portfolio value in the given currency.  

---

## Technical Requirements  

- **Backend**: Java 17, Spring Boot 3.4.1  
- **Build Tool**: Maven  
- **Database**: H2 (in-memory) *(can be replaced with PostgreSQL, MySQL, etc.)*  
- **Persistence**: Hibernate/JPA  
- **API Communication**: REST (JSON format)  
- **Testing**:  
  - Unit Tests (`JUnit`, `Mockito`)  
  - Integration Tests (`WebTestClient`)  
- **Documentation**:  
  - API Documentation: **[View Full API Documentation](#api-documentation)**  
  - **Postman collection generated** from the application and can be found in the file `crypto-portfolio.json`.

---

# API Documentation  

<details>
  <summary>Click to expand for detailed API documentation</summary>
  
  For detailed endpoint descriptions, request parameters, and response examples, check the **[Crypto Portfolio API Documentation](#)**.

</details>
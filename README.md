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
  

This section provides details endpoints, descriptions, request methods, and sample payloads of the **Crypto Portfolio API** 

## Base URLs
- **Portfolio Service**: `http://localhost:8080`
- **Exchange Rate Service**: `http://localhost:8081`

---

# 1. Exchange Rate API

## 1.1 Get Latest Exchange Rates
**Endpoint:** `GET /exchange-rate/latest`
- Retrieves the latest exchange rates for supported cryptocurrencies.

**Request Example:**
```http
GET http://localhost:8081/exchange-rate/latest
```

## 1.2 Get Last Price for a Specific Symbol
**Endpoint:** `GET /exchange-rate?symbol={symbol}&base={base}`
- Retrieves the latest exchange rate for a specific cryptocurrency.

**Request Example:**
```http
GET http://localhost:8081/exchange-rate?symbol=BTC&base=MAD
```

---

# 2. User Management

## 2.1 Create User
**Endpoint:** `POST /users`
- Creates a new user.

**Request Example:**
```json
{
  "firstName": "Tupac",
  "lastName": "Amaru",
  "birthDate": "1992-04-29",
  "profession": "Producer"
}
```

## 2.2 Retrieve User
**Endpoint:** `GET /users/{userId}`
- Retrieves details of a user by ID.

## 2.3 Update User
**Endpoint:** `PATCH /users/{userId}`
- Updates user details.

**Request Example:**
```json
{
  "id": "1",
  "lastName": "Shakur",
  "birthDate": "1978-04-29",
  "profession": "King of Rap"
}
```

## 2.4 Remove User
**Endpoint:** `DELETE /users/{userId}`
- Deletes a user by ID.

## 2.5 Retrieve All Portfolios for a User
**Endpoint:** `GET /users/{userId}/portfolios/all`
- Fetches all portfolios owned by a user.

---

# 3. Portfolio Management

## 3.1 Create Portfolio
**Endpoint:** `POST /users/{userId}/portfolios`
- Creates a new portfolio for a user.

**Request Example:**
```json
{
  "name": "Medical Sales Stock"
}
```

## 3.2 Retrieve Portfolio
**Endpoint:** `GET /users/{userId}/portfolios/{portfolioId}`
- Retrieves portfolio details by ID.

## 3.3 Update Portfolio
**Endpoint:** `PATCH /users/{userId}/portfolios/{portfolioId}`
- Updates an existing portfolio.

**Request Example:**
```json
{
  "id": 1,
  "name": "Shakur Music Investment"
}
```

## 3.4 Remove Portfolio
**Endpoint:** `DELETE /users/{userId}/portfolios/{portfolioId}`
- Deletes a portfolio.

## 3.5 Retrieve All Holdings in a Portfolio
**Endpoint:** `GET /users/{userId}/portfolios/{portfolioId}/holdings/all`
- Lists all holdings in a portfolio.

## 3.6 Get Portfolio Valuation
**Endpoint:** `GET /users/{userId}/portfolios/{portfolioId}/valuation?base={currency}`
- Returns the total value of a portfolio in the specified base currency.

**Request Example:**
```http
GET http://localhost:8080/users/1/portfolios/1/valuation?base=MAD
```

---

# 4. Holding Management

## 4.1 Create Holding
**Endpoint:** `POST /users/{userId}/portfolios/{portfolioId}/holdings`
- Adds a cryptocurrency holding to a portfolio.

**Request Example:**
```json
{
  "symbol": "LTC",
  "amount": 15.5
}
```

## 4.2 Retrieve Holding
**Endpoint:** `GET /users/{userId}/portfolios/{portfolioId}/holdings/{holdingId}`
- Retrieves a specific holding by ID.

## 4.3 Update Holding
**Endpoint:** `PATCH /users/{userId}/portfolios/{portfolioId}/holdings/{holdingId}`
- Updates a holding.

**Request Example:**
```json
{
  "symbol": "BTC",
  "amount": 345.123
}
```

## 4.4 Remove Holding
**Endpoint:** `DELETE /users/{userId}/portfolios/{portfolioId}/holdings/{holdingId}`
- Removes a holding from a portfolio.

**Request Example:**
```http
DELETE http://localhost:8080/users/1/portfolios/1/holdings/3
```

---

## Notes
- All endpoints assume a `localhost` setup. Update the base URL for production.
- `DELETE` operations do not return a body but should return `204 No Content`.
- `PATCH` allows partial updates.
- Consider adding authentication and validation layers if necessary.

This documentation provides a structured overview of all available API operations for the crypto portfolio service.
</details>
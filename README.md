# Customer Rewards API

A Spring Boot application that calculates customer reward points based on purchase transactions over a configurable date range.

The application provides a RESTful API and a lightweight web UI for viewing monthly and total reward points.

---

## 1. Overview

This application implements a customer rewards program for a retailer.

Customers earn reward points based on the amount spent in each transaction.

### Reward Rules

| Transaction Amount   |                                   Reward Points |
| -------------------- | ----------------------------------------------: |
| $0 - $50             |                                               0 |
| Above $50 up to $100 |               1 point for every dollar over $50 |
| Above $100           | 50 points + 2 points for every dollar over $100 |

### Example

For a `$120` transaction:

```text
First $50                    = 0 points
Next $50                     = 50 points
Remaining $20 × 2            = 40 points

Total                        = 90 points
```

Therefore:

```text
$120 = 90 reward points
```

The application calculates rewards for each month and provides a total for the requested date range.

---

## 2. Requirements Covered

The implementation covers the following requirements:

* Spring Boot application
* RESTful API
* Customer reward calculation
* Monthly reward calculation
* Total reward calculation
* Dynamic start and end date
* Customer and transaction information in the response
* Sample transaction dataset
* H2/JPA transaction persistence
* Input validation
* Exception handling
* Logging
* Unit tests
* Controller/API tests
* Web UI
* README and technical documentation
* Git/SCM-friendly project structure

---

## 3. Technology Stack

* Java 17
* Spring Boot 4.1.1
* Spring Web
* Spring Validation
* JUnit 5
* Mockito
* Spring Boot Test / MockMvc
* Maven
* HTML
* CSS
* JavaScript
* Git

The application uses H2 with Spring Data JPA for customer and transaction persistence. Sample customer and transaction data is loaded from `data.sql` during application startup.

---

## 4. Architecture

```text
                    Web Browser
                         |
                         | HTTP
                         v
               +---------------------+
               | RewardsController   |
               +----------+----------+
                          |
                          v
               +---------------------+
               |   RewardsService    |
               +----------+----------+
                          |
             +------------+------------+
             |                         |
             v                         v
   +-------------------+     +----------------------+
   | CustomerRepository|     |TransactionRepository |
   +-------------------+     +----------+-----------+
                                        |
                                        v
                              TransactionRepositoryImpl
                                        |
                                        v
                              TransactionJpaRepository
                                        |
                                        v
                              TransactionEntity / H2
                                        |
                                        v
                              RewardCalculator
                                        |
                                        v
                               RewardResponse
```

### Responsibilities

#### Controller

`RewardsController`

* Exposes REST endpoints.
* Accepts customer ID and date range.
* Delegates business processing to the service.

#### Service

`RewardsServiceImpl`

* Validates the request.
* Retrieves customer information.
* Retrieves transactions.
* Groups transactions by month.
* Calculates monthly and total values.
* Builds the API response.

#### Reward Calculator

`RewardCalculator`

* Contains the reward calculation business rule.
* Keeps the calculation independent and easy to test.

#### Repository

`CustomerRepository`

* Provides customer information.

`TransactionRepository`

* Defines transaction retrieval operations.
* Keeps the service layer independent of the persistence implementation.

`TransactionRepositoryImpl`

* Implements transaction retrieval using Spring Data JPA.
* Converts persisted `TransactionEntity` records into domain transactions.

`TransactionJpaRepository`

* Provides Spring Data JPA access to the H2 transaction data.

#### Exception Handling

`GlobalExceptionHandler`

* Provides centralized exception handling.
* Returns consistent error responses.

---

## Project Structure

```text
customer-rewards/
├── src/
│   ├── main/
│   │   ├── java/com/charter/reward/
│   │   │   ├── CustomerRewardsApplication.java
│   │   │   ├── controller/
│   │   │   │   ├── RewardsController.java
│   │   │   │   └── CustomerController.java
│   │   │   ├── service/
│   │   │   │   ├── RewardsService.java
│   │   │   │   ├── RewardsServiceImpl.java
│   │   │   │   └── RewardCalculator.java
│   │   │   ├── repository/
│   │   │   │   ├── CustomerRepository.java
│   │   │   │   ├── CustomerJpaRepository.java
│   │   │   │   ├── CustomerRepositoryImpl.java
│   │   │   │   ├── TransactionRepository.java
│   │   │   │   ├── TransactionJpaRepository.java
│   │   │   │   └── TransactionRepositoryImpl.java
│   │   │   ├── entity/
│   │   │   │   ├── CustomerEntity.java
│   │   │   │   └── TransactionEntity.java
│   │   │   ├── model/
│   │   │   │   ├── Customer.java
│   │   │   │   ├── Transaction.java
│   │   │   │   ├── MonthlyReward.java
│   │   │   │   ├── RewardTransaction.java
│   │   │   │   └── RewardResponse.java
│   │   │   └── exception/
│   │   │       ├── InvalidRequestException.java
│   │   │       ├── CustomerNotFoundException.java
│   │   │       ├── ErrorResponse.java
│   │   │       └── GlobalExceptionHandler.java
│   │   └── resources/
│   │       ├── static/
│   │       │   └── index.html
│   │       ├── application.properties
│   │       └── data.sql
│   └── test/
│       └── java/com/charter/reward/
│           ├── service/
│           ├── controller/
│           └── repository/
│               ├── CustomerRepositoryIntegrationTest.java
│               └── TransactionRepositoryIntegrationTest.java
├── docs/
│   ├── successful-build.png
│   ├── test-results.png
│   ├── postman-success.png
│   ├── postman-error-custIdFormat.png
│   ├── postman-error-custNotFound.png
│   └── postman-error-invalidStartDate.png
├── README.md
├── pom.xml
└── .gitignore
```

---

## 6. Sample Data

The application uses an **H2 database with Spring Data JPA** for customer and transaction persistence. Sample customer and transaction records are loaded from `data.sql` when the application starts.

### Customers

| Customer ID | Customer Name |
| ----------- | ------------- |
| C001        | Customer ONE      |
| C002        | Customer TWO   |
| C003        | Customer THREE    |

### Example Transactions

| Transaction | Customer | Date       |  Amount |
| ----------- | -------- | ---------- | ------: |
| T001        | C001     | 2026-01-05 |  $40.00 |
| T002        | C001     | 2026-01-10 |  $50.00 |
| T003        | C001     | 2026-01-15 |  $75.00 |
| T004        | C001     | 2026-01-20 | $120.00 |
| T005        | C001     | 2026-02-05 | $100.00 |
| T006        | C001     | 2026-02-15 | $200.00 |
| T007        | C001     | 2026-03-10 |  $60.00 |
| T008        | C001     | 2026-03-20 | $150.00 |

The dataset deliberately includes boundary cases such as `$50`, `$100`, and amounts above `$100`.
---

## 7. REST API

### Calculate Customer Rewards

```http
GET /api/v1/customers/{customerId}/rewards
```

### Query Parameters

| Parameter   | Required | Description                       |
| ----------- | -------- | --------------------------------- |
| `startDate` | Yes      | Start date in `yyyy-MM-dd` format |
| `endDate`   | Yes      | End date in `yyyy-MM-dd` format   |

### Example

```http
GET /api/v1/customers/C001/rewards?startDate=2026-01-01&endDate=2026-03-31
```

---

## 8. Successful Response

Example response:

```json
{
  "customerId": "C001",
  "customerName": "Customer ONE",
  "startDate": "2026-01-01",
  "endDate": "2026-03-31",
  "monthlyRewards": [
    {
      "year": 2026,
      "month": "January",
      "points": 115
    },
    {
      "year": 2026,
      "month": "February",
      "points": 300
    },
    {
      "year": 2026,
      "month": "March",
      "points": 160
    }
  ],
  "transactions": [
    {
      "transactionId": "T001",
      "transactionDate": "2026-01-05",
      "amount": 40.00,
      "rewardPoints": 0
    },
    {
      "transactionId": "T002",
      "transactionDate": "2026-01-10",
      "amount": 50.00,
      "rewardPoints": 0
    },
    {
        "transactionId": "T003",
        "transactionDate": "2026-01-15",
        "amount": 75.00,
        "rewardPoints": 25
    },
    {
        "transactionId": "T004",
        "transactionDate": "2026-01-20",
        "amount": 120.00,
        "rewardPoints": 90
    },
    {
        "transactionId": "T005",
        "transactionDate": "2026-02-05",
        "amount": 100.00,
        "rewardPoints": 50
    },
    {
        "transactionId": "T006",
        "transactionDate": "2026-02-15",
        "amount": 200.00,
        "rewardPoints": 250
    },
    {
        "transactionId": "T007",
        "transactionDate": "2026-03-10",
        "amount": 60.00,
        "rewardPoints": 10
    },
    {
        "transactionId": "T008",
        "transactionDate": "2026-03-20",
        "amount": 150.00,
        "rewardPoints": 150
    }
  ],
  "totalTransactions": 8,
  "totalAmount": 795.00,
  "totalRewardPoints": 575
}
```

### Calculation

```text
January:
$40   → 0
$50   → 0
$75   → 25
$120  → 90
Total → 115 points

February:
$100  → 50
$200  → 250
Total → 300 points

March:
$60   → 10
$150  → 150
Total → 160 points

Overall:
115 + 300 + 160 = 575 points
```

---

## 9. Error Handling

The application uses centralized exception handling through `GlobalExceptionHandler`.

### Customer Not Found

Request:

```http
GET /api/v1/customers/C999/rewards?startDate=2026-01-01&endDate=2026-03-31
```

Response:

```http
HTTP/1.1 404 NOT FOUND
```

```json
{
  "status": 404,
  "error": "Customer Not Found",
  "message": "Customer not found: C999"
}
```

### Invalid Date Range

If the start date is after the end date:

```http
HTTP/1.1 400 BAD REQUEST
```

```json
{
  "status": 400,
  "error": "Invalid Request",
  "message": "Start date cannot be after end date"
}
```

### Invalid Date Format

The API expects:

```text
yyyy-MM-dd
```

Example:

```text
2026-01-01
```

---

## 10. Data Persistence

The application uses **H2 with Spring Data JPA** for customer and transaction persistence.

The persistence flow is:

```text
CustomerRepository / TransactionRepository
                    |
                    v
        JPA repository implementations
                    |
                    v
              H2 Database
```

Sample customers and transactions are loaded from `src/main/resources/data.sql` when the application starts.

The repository interfaces keep the service layer independent of the underlying persistence implementation.

---

## 11. Web UI

A lightweight browser UI is included under:

```text
src/main/resources/static/index.html
```

The UI allows the user to:

1. Select a customer.
2. Select a start date.
3. Select an end date.
4. Calculate rewards.
5. View monthly transactions.
6. View monthly reward points.
7. View total transaction count.
8. View total transaction amount.
9. View total reward points.
10. See validation/API errors.

The UI calls the backend customer and rewards endpoints described above.

Customers and reward information are loaded dynamically from the backend; customer names and response date ranges are not hardcoded in the UI.

### UI Flow

```text
User
 |
 | Select customer/date range
 v
Web UI
 |
 | GET request
 v
Spring Boot REST API
 |
 v
Reward calculation
 |
 v
JSON response
 |
 v
Web UI displays results
```

---

## 12. Testing

The project includes unit and controller/API tests.

### Reward Calculator Tests

The following scenarios are covered:

| #  | Test Case                   | Input                     | Expected Result | Actual Result | Status |
| -- | --------------------------- | ------------------------- | --------------- | ------------- | ------ |
| 1  | Amount below $50            | `$40`                     | `0 points`      | `0 points`    | PASS   |
| 2  | Amount exactly $50          | `$50`                     | `0 points`      | `0 points`    | PASS   |
| 3  | Amount between $50 and $100 | `$75`                     | `25 points`     | `25 points`   | PASS   |
| 4  | Amount exactly $100         | `$100`                    | `50 points`     | `50 points`   | PASS   |
| 5  | Amount above $100           | `$120`                    | `90 points`     | `90 points`   | PASS   |
| 6  | Amount above $100           | `$200`                    | `250 points`    | `250 points`  | PASS   |
| 7  | Customer rewards            | `C001, Jan–Mar 2026`      | `575 points`    | `575 points`  | PASS   |
| 8  | Customer not found          | `C999`                    | HTTP `404`      | HTTP `404`    | PASS   |
| 9  | Invalid date range          | Start > End               | HTTP `400`      | HTTP `400`    | PASS   |
| 10 | Invalid date format         | `wrong-date`              | HTTP `400`      | HTTP `400`    | PASS   |
| 11 | No transactions             | Valid customer/date range | Empty result    | Empty result  | PASS   |


### Service Tests

Service tests cover:

* Monthly reward calculation
* Total reward calculation
* Customer lookup
* Customer-not-found scenario
* Invalid date range

### Controller Tests

Controller tests using `MockMvc` cover:

* Successful reward request
* Customer-not-found response
* Invalid date range
* Invalid date format
* Missing request parameters
* Invalid customer ID format and length

### End-to-End Reward Integration Test

The reward API integration test verifies the actual response from the H2-backed application, including:

* Customer ID and name
* Total transaction count: `8`
* Total transaction amount: `795.00`
* Total reward points: `575`
* Monthly reward order: January, February, March
* Transaction list and per-transaction reward points

### Run Tests

```bash
mvn clean test
```

Expected result:

```text
BUILD SUCCESS
```

Test execution results should be included with the submission.

---

## 13. How to Run

### Prerequisites

* Java 17 or compatible JDK
* Maven
* Git

### Clone Repository

```bash
git clone <your-github-repository-url>
cd customer-rewards
```

### Build

```bash
mvn clean install
```

### Run Tests

```bash
mvn test
```

### Start Application

```bash
mvn spring-boot:run
```

### Open UI

```text
http://localhost:8080/
```

### Test REST API

```text
http://localhost:8080/api/v1/customers/C001/rewards?startDate=2026-01-01&endDate=2026-03-31
```

---

## 14. Configuration

The application uses `application.properties`.

Example:

```properties
spring.application.name=customer-rewards
server.port=8080
```

The application also contains H2/JPA configuration for transaction persistence.

---

## 15. Design Decisions

### H2/JPA persistence

Customer and transaction data are stored in an H2 database using Spring Data JPA.

Sample data is loaded from `data.sql` during application startup. The repository interfaces separate persistence concerns from the reward calculation business logic.

### BigDecimal

`BigDecimal` is used for transaction amounts because monetary values should not be represented using floating-point types.

### Service/Repository separation

Business logic is kept out of the controller.

The controller handles HTTP concerns while the service handles business orchestration.

### Dedicated RewardCalculator

Reward calculation is isolated into its own component so that the business rule can be independently unit tested.

### Dynamic timeframe

The API accepts `startDate` and `endDate` instead of assuming a fixed three-month period.

This allows the API to support different reporting periods.

---

## 16. Logging

The application uses SLF4J logging for important processing events.

Examples include:

```text
Calculating rewards for customer
Number of transactions found
Reward calculation completed
```

`System.out.println()` is not used for application logging.

---

## 17. Documentation Screenshots

The `docs` directory contains screenshots demonstrating the completed implementation and review scenarios.

* `successful-build.png` — successful Maven build
* `test-results.png` — automated test execution
* `postman-success.png` — successful reward API response
* `postman-error-custIdFormat.png` — customer ID validation error
* `postman-error-custNotFound.png` — customer-not-found response
* `postman-error-invalidStartDate.png` — invalid date request response

---

## 17. Git / SCM

## Git Commit History

The project is maintained using Git with meaningful commits for major
changes and improvements.

The repository history reflects the actual implementation and review
updates made to the project.

Example commit messages include:

- Add customer rewards application
- Ignore generated effective pom
- Correct technology versions in README
- Align README with project structure
```

Push to GitHub:

git branch -M main
git remote add origin <your-github-repository-url>
git push -u origin main
```

---

## 18. Future Improvements

For a production implementation, the following could be added:

* PostgreSQL/MySQL production database
* Database migrations using Flyway or Liquibase
* Database indexing for customer/date queries
* Authentication and authorization
* OpenAPI/Swagger documentation
* Pagination for large transaction datasets
* External transaction service integration
* Non-blocking WebClient-based API communication
* Docker containerization
* CI/CD pipeline
* Integration tests using Testcontainers
* Production monitoring and metrics

These are intentionally outside the scope of the current coding exercise.

---

## 19. Summary

The Customer Rewards application provides a complete flow from a web UI to a Spring Boot REST API.

```text
Web UI
  |
  v
REST Controller
  |
  v
Rewards Service
  |
  +---- Customer Repository
  |
  +---- Transaction Repository
  |           |
  |           v
  |     H2 / JPA Data Retrieval
  |
  v
Reward Calculator
  |
  v
Monthly + Total Rewards
  |
  v
JSON Response
```

The solution focuses on clean, readable code, separation of responsibilities, testability, validation, exception handling, H2/JPA persistence, and a dynamic API timeframe.

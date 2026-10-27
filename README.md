# Transaction Ledger API

A RESTful backend application built with Java and Spring Boot to manage financial accounts and transactions. The API supports account creation, credit and debit transactions, balance retrieval, transaction history, pagination, and transaction validation.

## Features

- Create and retrieve financial accounts.
- Process credit and debit transactions.
- Retrieve current account balances.
- View transaction history with pagination.
- Filter transactions by type.
- Prevent duplicate transaction references.
- Prevent debits that exceed the available balance.
- Validate requests and return meaningful HTTP error responses.
- Explore and test endpoints through Swagger UI.
- Run automated tests using Maven.

## Technology Stack

- Java 21
- Spring Boot 4.1.2
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Swagger UI / OpenAPI
- JUnit and Spring Boot Test

## Prerequisites

Install the following before running the application:

- JDK 21
- MySQL or a compatible local database
- Git

## Setup Instructions

### 1. Clone the repository

```bash
git clone https://github.com/Vikash6621SR/transaction-ledger-api.git
cd transaction-ledger-api
```

### 2. Configure the database

Create a MySQL database named `transaction_ledger_db`.

Configure your local database connection in `src/main/resources/application.properties`. Set your database username and password locally; do not commit real credentials to GitHub.

### 3. Run the application

On Windows, use the Maven wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

The API will normally start at:

`http://localhost:8080`

### 4. Open Swagger UI

Visit:

http://localhost:8080/swagger-ui/index.html

Use Swagger UI to explore and test the available endpoints.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/accounts` | Create an account |
| GET | `/api/accounts/{id}` | Retrieve an account |
| GET | `/api/accounts/{id}/balance` | Retrieve account balance |
| POST | `/api/accounts/{accountId}/transactions` | Create a credit or debit transaction |
| GET | `/api/accounts/{accountId}/transactions` | Retrieve transaction history |

Transaction history supports pagination using `page` and `limit` query parameters. A transaction type filter is also available.

## Example: Create an Account

**Request**

```http
POST /api/accounts
Content-Type: application/json
```

```json
{
  "name": "Savings Account"
}
```

## Example: Create a Credit Transaction

**Request**

```http
POST /api/accounts/1/transactions
Content-Type: application/json
```

```json
{
  "type": "CREDIT",
  "amount": 1000.00,
  "description": "Initial deposit",
  "reference_id": "PAY-TEST-1001"
}
```

Use an existing account ID and a unique transaction reference when testing.

## Business Rules and Error Handling

- Duplicate transaction references are rejected for the same account.
- Debit transactions exceeding the available balance are rejected.
- Requests for nonexistent accounts return an appropriate not-found response.
- Invalid pagination parameters are rejected.
- Errors are returned using appropriate HTTP status codes.

## Run Automated Tests

Execute the test suite:

```powershell
.\mvnw.cmd clean test
```

## Project Structure

```text
transaction-ledger-api/
├── src/
│   ├── main/
│   │   ├── java/com/example/ledger/
│   │   └── resources/
│   └── test/
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

## Future Improvements

- Add authentication and authorization.
- Add stronger transaction concurrency controls.
- Expand automated tests for transaction processing and validation.
- Deploy the API with a securely configured production database.

## Author

**Vikash S R**

GitHub: https://github.com/Vikash6621SR
Portfolio: https://vikash-sr-portfolio-three.vercel.app/

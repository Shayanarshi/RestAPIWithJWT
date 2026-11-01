# RestAPIWithJWT

A Spring Boot REST API demonstrating JWT-based authentication, role-based authorization, employee management, MySQL integration, and centralized exception handling.

## Overview

This project demonstrates how to build a RESTful backend using Java, Spring Boot, Spring Security, JWT, Spring Data JPA, and MySQL.

Clients can authenticate through a login endpoint, receive a JWT, and use the token to access protected operations according to the configured security rules.

## Features

- RESTful APIs for employee management
- JWT generation and token validation
- Spring Security authentication and authorization
- Role-based access control for selected operations
- MySQL database integration
- Spring Data JPA for database operations
- Bean validation for request data
- Global exception handling
- JSON request and response handling
- Maven build using the Maven Wrapper

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot | Application framework |
| Spring Web MVC | REST API development |
| Spring Security | Authentication and authorization |
| JWT (JJWT) | Token generation and validation |
| Spring Data JPA | Database access |
| Hibernate | ORM |
| MySQL | Relational database |
| Jakarta Bean Validation | Request validation |
| Maven | Dependency and build management |
| Postman | API testing |

## Project Architecture

```text
Client / Postman
       |
       v
Spring Security Filter Chain
       |
       v
JWT Filter
       |
       v
REST Controller
       |
       v
Service Layer
       |
       v
Spring Data JPA Repository
       |
       v
MySQL Database
```

The login endpoint authenticates user credentials and generates a signed JWT. For requests containing a valid Bearer token, the JWT filter validates the token and establishes the authentication context before the request reaches the controller.

## API Endpoints

Base URL: `http://localhost:8080`

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/login` | Authenticate and generate a JWT |
| GET | `/api/employees` | Retrieve all employees |
| GET | `/api/employee/{id}` | Retrieve an employee by ID |
| POST | `/api/save` | Create an employee |
| PUT | `/api/update` | Update an employee |
| PATCH | `/api/update/{id}` | Update an employee's salary |
| DELETE | `/api/delete/{id}` | Delete an employee |

**Note:** Access rules depend on the security configuration. GET endpoints are currently public, employee creation requires authentication, and update/delete operations have additional role restrictions. Verify and document the final behavior after testing.

## Prerequisites

Install the following before running the application:

- JDK 21
- MySQL Server
- Git
- Postman (recommended)

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/Shayanarshi/RestAPIWithJWT.git
cd RestAPIWithJWT
```

### 2. Create the database

Open MySQL and execute:

```sql
CREATE DATABASE sbms;
```

### 3. Configure environment variables

Configure your database connection and JWT signing secret using environment variables. Do not commit real passwords or signing keys to GitHub.

Update `src/main/resources/application.yaml` to read the values from the environment:

```yaml
server:
  port: 8080

spring:
  application:
    name: RestAPIWithJWT
  datasource:
    url: ${DB_URL:jdbc:mysql://localhost:3306/sbms}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

Update `JwtUtil.java` to load the JWT signing secret from a secure environment variable or secret manager. Use a sufficiently strong key suitable for the selected signing algorithm.

For local development, configure `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, and `JWT_SECRET` in your IDE's run configuration or your local environment. Do not add real credentials to this README.

### 4. Run the application

On Windows:

```bash
mvnw.cmd spring-boot:run
```

On Linux or macOS:

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

Alternatively, build the JAR:

```bash
./mvnw clean package
```

Run the generated JAR from the `target` directory:

```bash
java -jar target/RestAPIWithJWT-0.0.1-SNAPSHOT.jar
```

Use `mvnw.cmd` instead of `./mvnw` on Windows.

## Authentication Flow

1. Send user credentials to `POST /api/login`.
2. The application authenticates the credentials using Spring Security.
3. On successful authentication, the API returns a JWT.
4. Send the token in the `Authorization` header for protected endpoints:

```http
Authorization: Bearer YOUR_JWT_TOKEN
```

5. The JWT filter validates the token and sets the security context for the request.

The token expiration is currently configured for two minutes in the code. Change this to a suitable duration for your application and handle expired or invalid tokens consistently.

## Testing with Postman

1. Start the Spring Boot application and MySQL.
2. Send a POST request to `/api/login` with a JSON body containing `username` and `password`.
3. Copy the returned token.
4. Send requests to employee endpoints.
5. For protected operations, select **Authorization → Bearer Token** in Postman and provide the token.
6. Verify HTTP status codes and response bodies for successful requests and error cases.

The login uses Spring Security's JDBC user-details manager. Configure the required user tables and store user passwords using a password encoder before testing authentication.

## Error Handling

The application includes centralized exception handling and custom exceptions for missing employees and invalid or expired JWTs.

Test cases should cover:

- Employee ID not found
- Invalid request data
- Invalid credentials
- Missing, invalid, or expired JWT
- Insufficient permissions
- Database connection failures

## Security Notes

- Never commit database passwords or JWT signing keys.
- Use environment variables or a secret manager for sensitive configuration.
- Store user passwords as BCrypt hashes, not plaintext.
- Apply authentication to every endpoint that should not be public.
- Enforce role-based access on administrative operations.
- Return consistent HTTP status codes and error response bodies.
- Use HTTPS when deploying outside a local development environment.

## Future Improvements

- Add automated unit and integration tests using JUnit and Mockito.
- Add OpenAPI/Swagger documentation.
- Improve validation and standardized error responses.
- Add pagination and sorting for employee listings.
- Add refresh-token support if required.
- Add Docker support and a CI workflow.
- Deploy the application to a cloud environment.

## Author

**Shayan Arshi**

- GitHub: [Shayanarshi](https://github.com/Shayanarshi)
- LinkedIn: [shayanarshi](https://linkedin.com/in/shayanarshi)

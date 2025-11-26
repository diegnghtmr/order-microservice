# Project Architecture & Development Rules

> **Project:** Food Court Backend System  
> **Philosophy:** Hexagonal Architecture (Ports & Adapters), Clean Code, SOLID Principles.

This document outlines the strict guidelines for the development of the microservices ecosystem. Adherence to these rules is mandatory to ensure scalability, maintainability, and architectural purity.

- **Always use context7 MCP and web search for better documentation**

-----

## 1\. Architectural Pattern: Hexagonal (Ports & Adapters)

Every microservice must follow the **Hexagonal Architecture**. The code is divided into three distinct layers with strict dependency rules.

### Directory Structure

```text
src/main/java/com/pragma/powerup/{service-name}
│
├── domain                       <-- THE CORE (Business Logic)
│   ├── api                      <-- Input Ports (Interfaces)
│   ├── model                    <-- Plain Java Objects (Business Models)
│   ├── spi                      <-- Output Ports (Interfaces for Persistence/External)
│   └── usecase                  <-- Business Logic Implementation
│
├── application                  <-- THE ORCHESTRATOR
│   ├── dto                      <-- Data Transfer Objects
│   │   ├── request
│   │   └── response
│   ├── handler                  <-- REST to Domain Converters (Interfaces & Impl)
│   └── mapper                   <-- MapStruct Interfaces (DTO <-> Model)
│
└── infrastructure               <-- THE FRAMEWORK (Spring Boot)
    ├── input
    │   └── rest                 <-- REST Controllers (@RestController)
    ├── output                   <-- Driven Adapters
    │   ├── jpa (or mongo)       <-- Database Implementation
    │   └── feign                <-- External API Communication
    ├── configuration            <-- Manual Bean Injection & Config
    └── exception                <-- Global Exception Handling
```

-----

## 2\. The "Golden Rules" of the Domain Layer

The `domain` package is the heart of the software and must remain **framework agnostic**.

1.  **NO ANNOTATIONS:** usage of framework annotations inside the `domain` package is **strictly prohibited**.
      * ❌ No Spring Boot annotations (`@Service`, `@Autowired`, `@Component`).
      * ❌ No Lombok annotations (`@Data`, `@Getter`, `@Setter`). *Getters, Setters, and Constructors must be generated manually in the Model.*
      * ❌ No JPA/Mongo annotations (`@Entity`, `@Table`, `@Document`, `@Id`).
2.  **POJO Only:** Models inside `domain/model` must be Plain Old Java Objects.
3.  **Interface Driven:** The Domain interacts with the outside world *only* via Interfaces (Ports).
      * `domain/api`: Interfaces defined here are implemented by `usecase`.
      * `domain/spi`: Interfaces defined here are implemented by `infrastructure`.

-----

## 3\. Dependency Injection & Inversion of Control

To maintain purity, we do not rely on Spring's auto-magic inside the core.

1.  **Manual Injection:** The `usecase` classes must be instantiated manually.
2.  **No `@Autowired`:** The use of the `@Autowired` annotation is **forbidden** throughout the entire project (even in infrastructure). Use **Constructor Injection** instead (Spring does this automatically if a class has a single constructor).
3.  **Configuration Class:** Every microservice must have a `BeanConfiguration.java` in `infrastructure/configuration`.
      * This class is responsible for creating the `UseCase` beans and injecting the specific `PersistenceAdapter` implementations into them.

-----

## 4\. Tech Stack & Tools

  * **Language:** Java 25
  * **Framework:** Spring Boot 4 
  * **Build Tool:** Gradle.
  * **Databases:**
      * User & Food Court Services: **PostgreSQL** (Dockerized).
      * Order & Traceability Services: **MongoDB** (Dockerized).
  * **Object Mapping:** MapStruct.
  * **Documentation:** OpenAPI (Swagger) `springdoc-openapi`.
  * **Inter-service Communication:** Spring Cloud OpenFeign.

-----

## 5\. Microservices Ecosystem

Each microservice is a separate Git repository.

| Service Name | Port | Database | Responsibility |
| :--- | :--- | :--- | :--- |
| **`user-microservice`** | `8090` | PostgreSQL | Users, Roles, Auth. |
| **`food-court-microservice`** | `8091` | PostgreSQL | Restaurants, Dishes, Categories. |
| **`order-microservice`** | `8092` | MongoDB | Orders logic, Chef assignment. |
| **`traceability-microservice`**| `8093` | MongoDB | Order History Logs. |

-----

## 6\. Development Workflow & Git

### Branching Strategy

  * **Master/Main:** Stable production-ready code.
  * **Feature Branches:** Each User Story (HU) must be developed in an independent branch.
      * Format: `feat/hu-{number}/{short-description}`
      * Example: `feat/hu-01/create-owner`

### Commit Standards (Conventional Commits)

Commits must be small, descriptive, and follow the standard:

  * `feat: add new endpoint for creating users`
  * `fix: resolve null pointer in order handler`
  * `docs: update swagger documentation`
  * `test: add unit tests for restaurant usecase`
  * `chore: update gradle dependencies`

### Versioning

  * Use **Git Tags** for release versions (e.g., `v1.0.0`).

-----

## 7\. Quality Assurance & Testing

1.  **Unit Tests:** Mandatory for every User Story (HU).
      * Focus on `domain/usecase` logic.
      * Tests must be Fast, Independent, Repeatable, Self-validating, Timely (FIRST).
2.  **Code Coverage:** High coverage required for Domain logic.
3.  **Clean Code:**
      * **DRY (Don't Repeat Yourself):** Extract common logic to utility classes.
      * **SOLID:** Apply Single Responsibility and Dependency Inversion strictly.
      * **Naming:** All code (Classes, Variables, Methods, Comments) must be in **English**.
      * **Professional Naming:** Avoid `foo`, `bar`, `temp`. Use descriptive names like `restaurantRepository`, `calculateTotalOrderPrice`.
- Use Junit y Mockito

-----

## 8\. Documentation

  * **OpenAPI:** Every endpoint must be documented using Swagger annotations (`@Operation`, `@ApiResponse`).
  * **README:** Each repository must have a README explaining how to run it locally (Docker setup, Environment variables).

-----

## 9\. Deployment Environment

  * **Local:** All development is done locally.
  * **Docker:** Databases must run in Docker containers. Do not install PostgreSQL or MongoDB directly on the host machine OS if possible.
  * **Secrets:** Never commit sensitive passwords to Git. Use Environment Variables or a local `application-dev.yml` added to `.gitignore`.
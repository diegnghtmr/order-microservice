# Order Microservice

Order management service for the Food Court system. It owns the order lifecycle and coordinates validation with the User and Food Court services while emitting traceability events.

## System Context
- Part of the Food Court microservices suite.
- Calls User and Food Court services via OpenFeign to validate users, restaurants, and dishes.
- Sends trace data to the Traceability service and can trigger notifications via a messaging adapter.

## Responsibilities
- Create and manage orders through their lifecycle.
- Assign employees and update order status.
- Validate dishes and restaurant ownership before accepting orders.
- Emit traceability events and notifications.

## Architecture (Hexagonal / Ports and Adapters)
This service follows Hexagonal Architecture with explicit ports and adapters.
- `domain/`: core models, business rules, ports, and use cases.
- `application/`: DTOs, mappers, and handlers that orchestrate use cases.
- `infrastructure/`: REST controllers, Mongo adapters, Feign clients, security configuration, and JWT implementation.

## Tech Stack
- Spring Boot 4, Spring Security, JWT
- Spring Data MongoDB
- Spring Cloud OpenFeign
- MapStruct, Springdoc OpenAPI

## Data Store
- MongoDB (local default: `mongodb://localhost:27017/orders_db`)

## API
- Base URL: `http://localhost:8092/order-api`
- Swagger UI: `http://localhost:8092/order-api/swagger-ui.html`

## Local Development
1. Start the database: `docker compose up -d` (uses `docker-compose.yml`)
2. Run the service: `./gradlew bootRun`
3. Run tests: `./gradlew test`

## System Diagrams
![Project Architecture](https://raw.githubusercontent.com/diegnghtmr/food-court-ui/main/docs/project-architecture.png)
![SQL Entities Diagram](https://raw.githubusercontent.com/diegnghtmr/food-court-ui/main/docs/sql-entities-diagram.png)
![Order Flow Diagram](https://raw.githubusercontent.com/diegnghtmr/food-court-ui/main/docs/order-flow-diagram.png)
![NoSQL Entities Diagram](https://raw.githubusercontent.com/diegnghtmr/food-court-ui/main/docs/nosql-entities-diagram.png)

## Related Repositories
- [user-microservice](https://github.com/diegnghtmr/user-microservice)
- [food-court-microservice](https://github.com/diegnghtmr/food-court-microservice)
- [order-microservice](https://github.com/diegnghtmr/order-microservice)
- [traceability-microservice](https://github.com/diegnghtmr/traceability-microservice)
- [food-court-ui](https://github.com/diegnghtmr/food-court-ui)

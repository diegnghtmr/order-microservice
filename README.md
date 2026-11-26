<!-- markdownlint-disable-next-line MD041 -->
<div align="center">

<h1>Food Court — Order API</h1>

<p><strong>The order lifecycle for diners and kitchen staff, from checkout to pickup.</strong></p>

<p>
<a href="https://github.com/diegnghtmr/order-microservice/actions/workflows/ci.yml"><img src="https://img.shields.io/github/actions/workflow/status/diegnghtmr/order-microservice/ci.yml?branch=main&style=for-the-badge&labelColor=1F1F28&label=CI" alt="CI"></a>
<img src="https://img.shields.io/badge/Java-25-E0C15A?style=for-the-badge&labelColor=1F1F28" alt="Java 25">
<img src="https://img.shields.io/badge/Spring%20Boot-4.0-E0C15A?style=for-the-badge&labelColor=1F1F28" alt="Spring Boot 4.0">
<img src="https://img.shields.io/badge/Database-MongoDB-E0C15A?style=for-the-badge&labelColor=1F1F28" alt="MongoDB">
<a href="LICENSE"><img src="https://img.shields.io/badge/license-MIT-7FB4CA?style=for-the-badge&labelColor=1F1F28" alt="License: MIT"></a>
</p>

<p>
<a href="https://diegnghtmr.me/en/work/food-court/"><strong>Case study</strong></a> &bull;
<a href="#quick-start"><strong>Quick start</strong></a> &bull;
<a href="#architecture"><strong>Architecture</strong></a> &bull;
<a href="https://github.com/diegnghtmr/food-court-ui"><strong>Web app</strong></a>
</p>

</div>

<p align="center"><img width="100%" alt="Food Court kitchen board with pending, in-preparation, and ready orders" src="docs/assets/order-queue.png"></p>

*The Food Court kitchen board calls this API to claim orders, mark them ready, and confirm pickup.*

Checkout and pickup need to agree on which dishes were ordered, who is preparing them, and when they can be handed over. This service checks the order against the catalog, stores its current state, and makes the next actions available to diners and kitchen staff. It sends status changes to a separate service for order history.

## Features

- **Checkout validation.** Check that the restaurant exists, its dishes are available and belong to it, and the customer has no active order.
- **Kitchen assignment.** Staff can list the restaurant queue, take a pending order, and mark work in preparation as ready.
- **Pickup PIN.** Confirm handoff only after the employee supplies the correct code; the bundled notification adapter mocks SMS delivery.
- **Customer tracking and cancellation.** Expose order details and let a customer cancel while the order is still pending.
- **Transition reporting.** Send status changes to Traceability after updating the order; a failed trace call does not roll the update back.

## Quick start

Use JDK 25 and Docker with Compose; Gradle 9.2.1 comes from the wrapper. Start MongoDB in Compose and run the API on the host so it can use the checked-in localhost URLs for the other services.

```sh
git clone https://github.com/diegnghtmr/order-microservice.git
cd order-microservice
docker compose up -d order-mongodb
./gradlew bootRun
```

API: `http://localhost:8092/order-api`. Open [Swagger UI](http://localhost:8092/order-api/swagger-ui.html) for the routes under `/orders`, including assignment, ready, delivery, and cancellation.

| Name | Purpose | Default |
| --- | --- | --- |
| `SPRING_MONGODB_URI` | MongoDB connection URI | `mongodb://localhost:27017/orders_db` |
| `SPRING_MONGODB_DATABASE` | Order database name | `orders_db` |
| `CLIENTS_USER_URL` | User lookup API | `http://localhost:8090/user-api` |
| `CLIENTS_FOODCOURT_URL` | Restaurant and dish lookup API | `http://localhost:8091/foodcourt-api` |
| `CLIENTS_TRACEABILITY_URL` | Transition reporting API | `http://localhost:8093/traceability-api` |
| `JWT_SECRET` | JWT verification secret | Local development value; use a private value matching Identity outside local development |

Start [Identity, Catalog, and Traceability](#related-repositories) to try the full workflow. The UI case study uses a seeded restaurant. The notification adapter is a mock, not a live SMS integration.

## Architecture

Spring MVC controllers delegate to application handlers and `OrderUseCase`. The framework-free domain defines `IOrderPersistencePort`, `IExternalUserPort`, `IExternalFoodCourtPort`, `ITraceabilityPort`, and `IMessagingPort`. MongoDB stores the current order and Feign adapters handle the cross-service calls. Traceability keeps historical transitions separately; history can be incomplete if a trace write fails.

```mermaid
flowchart LR
    Web["food-court-ui"] -->|"Checkout, queue, and pickup"| API["Order REST API"]
    API --> Rules["Order lifecycle use case"]
    Rules -->|"Save and read current order"| DB["MongoDB: orders_db"]
    Rules -->|"Look up customer"| Identity["user-microservice"]
    Rules -->|"Check restaurant and dishes"| Catalog["food-court-microservice"]
    Rules -->|"Send status transition"| Trace["traceability-microservice"]
    Rules -->|"Send pickup notification"| SMS["Mock SMS adapter"]
```

See the [full order lifecycle diagram](https://github.com/diegnghtmr/food-court-ui/blob/main/docs/order-flow-diagram.png) and the [other system diagrams](https://github.com/diegnghtmr/food-court-ui/tree/main/docs).

## Tech stack

| Layer | Technology |
| --- | --- |
| HTTP and security | Java 25, Spring Boot 4.0, Spring MVC, Spring Security, JJWT |
| Integrations | Spring Cloud OpenFeign, MapStruct, springdoc OpenAPI |
| Persistence | Spring Data MongoDB |
| Build and tests | Gradle 9.2.1, JUnit Platform |

## Testing

```sh
./gradlew build
```

[CI](.github/workflows/ci.yml) builds and tests on Temurin 25. Use `./gradlew test` for the test task, which includes domain rules for order creation and the status lifecycle.

## Related repositories

| Repository | Role |
| --- | --- |
| [food-court-ui](https://github.com/diegnghtmr/food-court-ui) | Cart, order status, and kitchen board |
| [food-court-microservice](https://github.com/diegnghtmr/food-court-microservice) | Restaurants, dishes, and availability used at checkout |
| [user-microservice](https://github.com/diegnghtmr/user-microservice) | Roles, JWTs, and customer lookup |
| [traceability-microservice](https://github.com/diegnghtmr/traceability-microservice) | Stores status transitions and serves history and reports |

## License

Released under the [MIT License](LICENSE).

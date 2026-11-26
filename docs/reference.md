# Food Court Order API reference

## API endpoints

Base URL: `http://localhost:8092/order-api`. Use the running service’s [Swagger UI](http://localhost:8092/order-api/swagger-ui.html) for request and response schemas. Send `Authorization: Bearer <token>` on API calls.

| Method and path | Purpose |
| --- | --- |
| `POST /orders` | Validate and create an order for the authenticated customer |
| `GET /orders/{orderId}` | Read an order by ID |
| `GET /orders` | List the caller’s restaurant queue as owner or employee; defaults to `status=PENDIENTE`, `page=0`, and `size=10` |
| `PATCH /orders/{orderId}/assign` | Let an employee claim a pending order |
| `PATCH /orders/{orderId}/ready` | Let an employee mark an order ready and trigger the mock pickup notification |
| `POST /orders/deliver/{orderId}/{pin}` | Verify the pickup PIN for a ready order and record delivery |
| `PATCH /orders/cancel/{orderId}` | Let the customer cancel their own pending order |
| `PATCH /orders/{orderId}/chef` | Assign a chef through a request body |
| `PATCH /orders/{orderId}/status` | Update status through a request body |

Creating an order checks restaurant and dish references, dish availability, and whether the customer already has an active order. The kitchen workflow uses the `assign`, `ready`, and `deliver` routes. Traceability receives a separate Feign call after the order update; trace failures do not roll back that update.

## Data store and environment

The service stores orders, line items, and current state in MongoDB; the trace history is in the separate Traceability store. The [Compose database](../docker-compose.yml) maps MongoDB to host port `27017`.

| Name | Purpose | Default with a host-run API |
| --- | --- | --- |
| `SPRING_MONGODB_URI` | MongoDB connection URI | `mongodb://localhost:27017/orders_db` |
| `SPRING_MONGODB_DATABASE` | Order database name | `orders_db` |
| `CLIENTS_USER_URL` | User lookup API | `http://localhost:8090/user-api` |
| `CLIENTS_FOODCOURT_URL` | Restaurant and dish lookup API | `http://localhost:8091/foodcourt-api` |
| `CLIENTS_TRACEABILITY_URL` | Transition reporting API | `http://localhost:8093/traceability-api` |
| `JWT_SECRET` | Spring override for JWT verification | Local development value; verification must match Identity’s signing configuration |

`docker compose up -d order-mongodb` starts only MongoDB. `docker compose up -d` starts this API and its database; it expects the other services to be reachable separately. The Compose service uses `host.docker.internal` for its sibling URLs.

## Ports and adapters

The framework-free domain owns the order rules and declares the ports. Application DTOs, mappers, and handlers adapt REST requests to `OrderUseCase`. Spring wiring and the Mongo, Feign, and messaging implementations live under infrastructure.

| Domain output port | Adapter | Dependency |
| --- | --- | --- |
| `IOrderPersistencePort` | `OrderMongoAdapter` | The MongoDB order store |
| `IExternalUserPort` | `UserFeignAdapter` | Identity customer lookup |
| `IExternalFoodCourtPort` | `FoodCourtFeignAdapter` | Catalog restaurant and dish lookup |
| `ITraceabilityPort` | `TraceabilityFeignAdapter` | Traceability transition ingestion |
| `IMessagingPort` | `TwilioMockAdapter` | Local mock SMS; no live SMS delivery |

## System diagrams

- [System context](https://github.com/diegnghtmr/food-court-ui/blob/main/docs/project-architecture.png) — UI, services, and data stores.
- [SQL entities](https://github.com/diegnghtmr/food-court-ui/blob/main/docs/sql-entities-diagram.png) — historical relational design; current orders are not stored in PostgreSQL.
- [Document entities](https://github.com/diegnghtmr/food-court-ui/blob/main/docs/nosql-entities-diagram.png) — the order and trace document model.
- [Order lifecycle](https://github.com/diegnghtmr/food-court-ui/blob/main/docs/order-flow-diagram.png) — status transitions through pickup or cancellation.

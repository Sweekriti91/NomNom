# NomNom Project Guidelines

## Architecture

NomNom is a food delivery platform built as Spring Boot microservices with a React frontend.

| Service | Port | Package |
|---------|------|---------|
| api-gateway | 8080 | `com.nomnom.gateway` |
| order-service | 8081 | `com.nomnom.order` |
| restaurant-service | 8082 | `com.nomnom.restaurant` |
| notification-service | 8083 | `com.nomnom.notification` |
| React frontend | 5173 | `/frontend` |

## Java & Spring Boot

- Java 21, Spring Boot 3.4.4
- **Constructor injection only** — never use `@Autowired` on fields
- Layer architecture: Controller → Service → Repository
- All request DTOs must use Jakarta validation annotations (`@NotBlank`, `@NotNull`, `@Valid`, etc.)
- Return `ResponseEntity<T>` from all controller methods with appropriate HTTP status codes
- Use `@RestController` and `@RequestMapping` with plural resource nouns
- H2 in-memory database for all services — configure in `application.yml`
- Use `record` types for DTOs where appropriate

## REST API Conventions

- Plural nouns for resources: `/api/orders`, `/api/restaurants`, `/api/menu-items`
- Proper HTTP methods: GET (read), POST (create), PUT (full update), PATCH (partial update), DELETE (remove)
- Status codes: 200 (OK), 201 (Created), 204 (No Content), 400 (Bad Request), 404 (Not Found), 409 (Conflict)
- Consistent error response shape: `{ "error": "...", "message": "...", "status": 400 }`

## Build & Test

- Gradle Kotlin DSL (`build.gradle.kts`) — multi-project build
- Run all services: `./gradlew build`
- Run specific service tests: `./gradlew :order-service:test`
- JUnit 5 for all tests
- `@SpringBootTest` for integration tests
- `@WebMvcTest` with `MockMvc` for controller tests
- `@MockBean` to mock service dependencies in controller tests
- Test naming: `should_ExpectedBehavior_When_Condition()`

## React Frontend

- Functional components with hooks — no class components
- Tailwind CSS utility classes — no CSS modules or styled-components
- Dark mode support via `dark:` variants
- Vite dev server on port 5173

## Code Quality

- Keep methods short and focused
- Prefer immutability — use `final` for local variables and parameters where practical
- No `@SuppressWarnings` without a comment explaining why
- Logging via SLF4J (`private static final Logger log = LoggerFactory.getLogger(...)`)

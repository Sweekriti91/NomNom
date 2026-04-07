---
description: "Use when creating or modifying REST controllers, API endpoints, request/response DTOs, or error handling in NomNom services."
applyTo: "**/controller/**/*.java"
---

# REST API Standards

## Controller Methods

- Return `ResponseEntity<T>` — never return bare objects
- Use `@Valid` on `@RequestBody` parameters
- Map to correct status codes:
  - `POST` creation → `ResponseEntity.status(HttpStatus.CREATED).body(result)`
  - `GET` found → `ResponseEntity.ok(result)`
  - `GET` not found → `ResponseEntity.notFound().build()`
  - `DELETE` success → `ResponseEntity.noContent().build()`
  - Validation error → `ResponseEntity.badRequest().body(errors)`

## URL Design

- Plural nouns: `/api/orders`, `/api/restaurants`, `/api/menu-items`
- Nested resources: `/api/restaurants/{id}/menu-items`
- No verbs in paths — use HTTP methods to convey action

## Validation

- `@NotBlank` for required strings
- `@NotNull` for required objects and numbers
- `@Positive` for prices and quantities
- `@Size` for constrained lengths
- Group validations logically in the DTO

## Error Handling

- Use `@ExceptionHandler` or `@ControllerAdvice` for consistent error responses
- Error shape: `{ "error": "NOT_FOUND", "message": "Order not found with id: 42", "status": 404 }`
- Never leak stack traces or internal details in responses

## Example

```java
@PostMapping
public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
    OrderResponse created = orderService.createOrder(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
}
```

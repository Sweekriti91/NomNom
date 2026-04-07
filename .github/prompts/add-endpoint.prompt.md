---
description: "Scaffold a new REST endpoint in a NomNom service — generates controller method, service method, repository method, and DTO if needed."
agent: "agent"
---

Add a new REST endpoint to the NomNom project.

## Inputs

- **Service**: ${{service}} (e.g., order-service, restaurant-service, notification-service)
- **HTTP Method**: ${{method}} (GET, POST, PUT, PATCH, DELETE)
- **Path**: ${{path}} (e.g., /api/orders/{id}/status)
- **Description**: ${{description}}

## Instructions

1. Search the codebase for the existing controller in the specified service
2. Add the new endpoint method to the controller with:
   - Proper `@RequestMapping` annotation for the HTTP method
   - `@Valid @RequestBody` if it accepts a body
   - `ResponseEntity<T>` return type with appropriate status code
3. Create or update the service method that the controller delegates to
4. Create or update the repository method if data access is needed
5. Create a request DTO if the endpoint accepts a body (with Jakarta validation annotations)
6. Create a response DTO if the shape differs from the entity
7. Follow the conventions in `.github/copilot-instructions.md`

## Output

Show all files created or modified with their full paths.

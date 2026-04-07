# NomNom

## Start the Demo

1. Start all four backend services (from the repo root) or use Run&Debug -> Launch all Services:
   ```
   ./gradlew :order-service:bootRun &
   ./gradlew :restaurant-service:bootRun &
   ./gradlew :notification-service:bootRun &
   ./gradlew :api-gateway:bootRun &
   ```

2. Start the frontend:
   ```
   cd frontend
   npm run dev
   ```

3. Open http://localhost:5173

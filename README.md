# Snippr Snippets API (Spring Boot)

Minimal REST API to store and retrieve code snippets (in-memory).

## Endpoints
- `POST /snippets` — create a snippet
- `GET /snippets` — list all snippets; filter by language via `?lang=python`
- `GET /snippets/{id}` — get a snippet by id

## Quick Start
```bash
# From the project root
mvn spring-boot:run
# or
mvn -q -DskipTests package && java -jar target/snippr-api-0.0.1-SNAPSHOT.jar
```

## Example Requests
```bash
# Create
curl -X POST http://localhost:8080/snippets \
  -H "Content-Type: application/json" \
  -d '{ "lang": "python", "code": "print(\"hi\")" }'

# List all
curl http://localhost:8080/snippets

# Filter by lang
curl http://localhost:8080/snippets?lang=python

# Get by id
curl http://localhost:8080/snippets/1
```

## Seed Data
The app auto-loads the first existing file among: `seedData.json`, `seedData.yaml`, or `seedData.xml` from `src/main/resources/`.
An example `seedData.json` is included.
You can replace it with your own, keeping the same array-of-objects structure:
```json
[
  { "id": 1, "lang": "python", "code": "print('x')" },
  { "id": 2, "lang": "javascript", "code": "console.log('y');" }
]
```

## Notes
- In-memory storage only (no database). Restarting the app resets unsaved changes unless seeded.
- Java 17, Spring Boot 3.x
```

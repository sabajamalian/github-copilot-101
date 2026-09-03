# Library API

Small Spring Boot REST API for GitHub Copilot lab exercises.

## Run it

- From this directory: `mvn spring-boot:run`
- The API serves on http://localhost:8080
- H2 console is enabled at http://localhost:8080/h2-console

## Example requests

```bash
curl -s http://localhost:8080/books
curl -s http://localhost:8080/books/1
curl -s -X POST http://localhost:8080/books   -H 'Content-Type: application/json'   -d '{"title":"The Left Hand of Darkness","author":"Ursula K. Le Guin","isbn":"9780441478125","available":true}'
curl -s -X POST http://localhost:8080/loans   -H 'Content-Type: application/json'   -d '{"bookId":1,"memberId":1}'
curl -s -X POST http://localhost:8080/loans/1/return
```

## Known gaps (on purpose)

These rough edges are intentional lab material. Students should not be surprised by them.

- No bean validation anywhere. Books with null fields are accepted.
- Controllers return JPA entities directly. There are no DTO classes.
- Error handling is inconsistent: missing books return HTTP 200 with an empty body, missing members throw a raw runtime exception, and missing books during checkout throw `IllegalArgumentException`.
- Controller and service method names are mixed instead of following one naming style.
- Layering is inconsistent. Books and members use services, but loans put business logic directly in the controller.
- The 14-day loan period constant is duplicated in `BookService` and `LoanController`.
- There is one thin context-load test only.
- `GET /books` has no pagination and returns every book.

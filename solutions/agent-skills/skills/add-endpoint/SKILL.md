---
name: add-endpoint
description: Add a REST endpoint to library-api end to end, including controller method, service logic, request and response records, validation, error handling, and a MockMvc test. Use this whenever someone asks to add, expose, or create an endpoint, route, or API operation in library-api.
---

# Add an endpoint to library-api

Work through these steps in order. Do not stop before step 6.

## 1. Pick the package

Code lives in `library-api/src/main/java/com/example/library/`, one package per domain area:
`book`, `member`, `loan`. Put the new endpoint in the package that owns the resource.

## 2. Define the records

Never accept or return a JPA entity from a controller. In the same package, create Java records:

- Request: `CreateBookRequest`, `CheckoutLoanRequest`, and so on
- Response: `BookResponse`, `LoanResponse`, and so on

Annotate request record fields with Jakarta Bean Validation (`@NotBlank`, `@NotNull`,
`@Positive`). If `spring-boot-starter-validation` is not in `library-api/pom.xml`, add it.

## 3. Put the logic in a service

The controller method should be three lines or fewer: take the request, call the service,
return the response. If the package has no service class, create one. The `loan` package
does not have one yet, so a loan endpoint needs `LoanService` created first, and the existing
logic in `LoanController` moved into it.

## 4. Fail consistently

- Not found: throw `NotFoundException` from `com.example.library.support`, creating it if needed.
- Invalid state, for example returning a loan that is already returned: throw
  `IllegalStateException` with a message naming the problem.
- Never return `null` for a missing resource. Never throw bare `RuntimeException`.

Make sure a `@RestControllerAdvice` exists that maps `NotFoundException` to 404 and
`IllegalStateException` to 409, returning `{"error": "...", "detail": "..."}`.

## 5. Reuse policy constants

The loan period is 14 days. It is currently declared in both `LoanController` and
`BookService`. Do not add a third copy. Move it to a single constant and reference that.

## 6. Test it

Add a `@WebMvcTest` class using `MockMvc`. Cover the happy path and every failure path the
endpoint can produce. Name tests `method_condition_expectedResult`, for example
`renewLoan_whenAlreadyReturned_returns409`. Assert with AssertJ.

Then run `mvn -f library-api/pom.xml test` and fix anything red before reporting back.

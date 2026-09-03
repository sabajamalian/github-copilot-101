---
name: Java API conventions
description: DTO, validation, and error handling rules for library-api main sources.
applyTo: "library-api/src/main/java/**/*.java"
---

# API conventions

## Never expose JPA entities over HTTP

Controller method signatures must not accept or return `Book`, `Member`, or `Loan`.
Use Java records in the same package instead:

- Responses: `BookResponse`, `MemberResponse`, `LoanResponse`
- Requests: `CreateBookRequest`, `CheckoutLoanRequest`

Map between the entity and the record in the service, not the controller.

## Validate every request body

Annotate the record fields with Jakarta Bean Validation (`@NotBlank`, `@NotNull`, `@Positive`)
and put `@Valid` on the controller parameter. If `spring-boot-starter-validation` is missing
from `library-api/pom.xml`, add it.

## Fail in exactly one way

- Not found: throw `NotFoundException` (create it in `com.example.library.support` if it
  does not exist yet).
- Bad input that validation cannot express: throw `IllegalStateException` with a message
  that names the field.
- Never return `null` to signal a missing resource.
- Never throw bare `RuntimeException`.

Handle these centrally in a `@RestControllerAdvice` and return a consistent JSON error body:
`{"error": "...", "detail": "..."}`. Create the advice class if it does not exist.

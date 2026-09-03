# library-api

A Spring Boot 3 REST API for a library. Java 21, Spring Data JPA, H2 in memory.
The application code lives in `library-api/`. Everything else in this repo is course material.

## Layout

One package per domain area under `library-api/src/main/java/com/example/library/`:
`book`, `member`, `loan`. Keep new code in the package it belongs to.

## Conventions

- Controllers handle HTTP only. Business logic belongs in a service class in the same package.
- Name controller methods after the HTTP action: `getBooks`, `createBook`, `returnLoan`.
  Do not use `fetch`, `retrieve`, or `list` prefixes. The existing code is inconsistent about
  this. Follow the rule, not the neighbouring file.
- Policy constants like the loan period are declared once. If you need one that already exists
  somewhere else, import it. Do not redeclare it.

## Build

Run `mvn -f library-api/pom.xml test` before claiming a change works.

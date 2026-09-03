---
name: smoke-test
description: Smoke test a locally running library-api and report which endpoints are healthy. Use this after changing a controller, or when asked to check, verify, or smoke test whether the API still works.
---

# Smoke test library-api

The API must already be running. If it is not, start it with
`mvn -f library-api/pom.xml spring-boot:run` and wait for the startup log line.

Run `smoke.sh` from this skill's directory. It takes an optional base URL and defaults to
`http://localhost:8080`.

Report the output as-is, then say in one line whether the API is healthy.

## Reading the results

The last two checks assert current buggy behaviour on purpose:

- `GET /books/999` answers `200` with an empty body instead of `404`
- `GET /members/999` answers `500` instead of `404`

If either of those now FAILs, that is good news, not bad. It means error handling was fixed,
and `smoke.sh` should be updated to expect `404`.

---
name: Test conventions
description: How to write tests for library-api.
applyTo: "library-api/src/test/**/*.java"
---

# Test conventions

- Use `@WebMvcTest` plus `MockMvc` for controller tests. Do not boot the whole application
  with `@SpringBootTest` to exercise a single endpoint.
- Name tests `method_condition_expectedResult`, for example
  `getBook_whenIdUnknown_returns404`.
- Every endpoint that looks something up needs a test for the not-found path.
- Assert with AssertJ (`assertThat`), not bare JUnit `assertEquals`.
- No `Thread.sleep`. Ever.

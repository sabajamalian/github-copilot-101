# Custom instructions

**~25 minutes.** Standalone: nothing here needs the other two modules.

## Why this exists

Copilot doesn't know your team's rules. So you tell it "use a DTO, not the entity," it fixes that file, and then the next request it hands you an entity again. You're paying for the same correction forever.

Custom instructions are plain Markdown files Copilot reads *before* every chat and agent request. Say it once, in a file, in the repo.

The file types in this lab follow GitHub's [repository instructions guide](https://docs.github.com/en/copilot/how-tos/configure-custom-instructions-in-your-ide/add-repository-instructions-in-your-ide).
Microsoft's [VS Code reference](https://code.visualstudio.com/docs/agent-customization/custom-instructions)
explains always-on instructions, file-based rules, and manual attachment.

## Watch it first

<div class="video-embed">
  <iframe
    src="https://www.youtube-nocookie.com/embed/0jEzUhU8bLc"
    title="Your codebase, your rules: Customizing Copilot with context engineering"
    loading="lazy"
    referrerpolicy="strict-origin-when-cross-origin"
    allow="accelerometer; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
    allowfullscreen></iframe>
</div>

**[Your codebase, your rules: Customizing Copilot with context engineering](https://www.youtube.com/watch?v=0jEzUhU8bLc)**, 16 min, official GitHub channel.

Optional, and it goes wider than this module: it's VS Code only, and it covers instructions alongside MCP and prompt files. Watch it for the *why*. The lab below is the *how*, on both surfaces.

## What you'll build

Three files, each doing a different job:

| File | Scope |
| --- | --- |
| `.github/copilot-instructions.md` | Repo-wide. Loaded on every request. |
| `.github/instructions/java-api.instructions.md` | Only when touching main Java sources. |
| `.github/instructions/tests.instructions.md` | Only when touching tests. |

Then you'll ask for a feature nobody has described the conventions for, and watch Copilot follow them anyway.

## Build it

Creating the files is identical in VS Code and Copilot CLI, so do this part in whichever you prefer. The tabs come back when it's time to verify.

Work from the **repo root**, not `library-api/`.

### 1. Repo-wide instructions

```bash
mkdir -p .github
```

Create `.github/copilot-instructions.md`:

```markdown title=".github/copilot-instructions.md"
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
```

No frontmatter on this one. It's always on.

### 2. Scoped instructions for the API code

```bash
mkdir -p .github/instructions
```

Create `.github/instructions/java-api.instructions.md`:

```markdown title=".github/instructions/java-api.instructions.md"
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
```

The `applyTo` glob is what makes this file conditional. It's relative to the **workspace root**, which is why it starts with `library-api/`.

Keep the [VS Code instruction format reference](https://code.visualstudio.com/docs/agent-customization/custom-instructions)
open when changing the glob. The Java layering and error-handling rules above are this lab's
conventions; the documentation describes how Copilot discovers and applies the file.

### 3. Scoped instructions for tests

Create `.github/instructions/tests.instructions.md`:

```markdown title=".github/instructions/tests.instructions.md"
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
```

### 4. Check Copilot actually loaded them

=== "VS Code"

    Open a new Chat, switch the mode picker to **Agent**, and ask:

    ```text
    Which custom instruction files are you using for this workspace?
    ```

    You can also see them listed under the Chat view's configuration menu, in the
    agent customization list.

    If nothing shows up, the usual cause is that you opened `library-api/` as the workspace
    instead of the repo root.

=== "Copilot CLI"

    From the repo root, start `copilot` and run:

    ```text
    /instructions
    ```

    That lists every instruction file the CLI discovered, and lets you toggle each one on
    and off. Keep this command in mind, you'll want it in a minute.

    The CLI looks in more places than VS Code does, including `~/.copilot/instructions/`,
    `AGENTS.md`, and `CLAUDE.md`. If something unexpected is loaded, this is where you find out.

## Try it

Start a **fresh** chat session (old context will muddy the result), make sure you're in Agent mode, and paste:

```text
Add search to the books API. GET /books/search?q= should match title or author,
case insensitive, and return the matching books. Include tests.
```

Notice what you did *not* say in that prompt: nothing about DTOs, nothing about validation, nothing about how to fail, nothing about `@WebMvcTest`.

A good result comes back with:

- A `BookResponse` record, not the `Book` entity, in the controller signature
- Search logic in `BookService`, not in `BookController`
- A method named `searchBooks`, not `findBooks` or `retrieveBooks`
- A `@WebMvcTest` test class with a `MockMvc` test, named in `method_condition_expectedResult` form
- AssertJ assertions

Then run it:

```bash
mvn -f library-api/pom.xml test
```

!!! note "It won't be perfect"
    Instructions raise the floor, they don't guarantee an outcome. If Copilot missed one of
    the rules, that's useful information: either the rule was vague, or the file is long
    enough that it's getting diluted. Tighten the wording and try again. That editing loop
    is the actual skill this module teaches.

## See the difference

This is the part worth doing. Turn the instructions off and run the exact same prompt.

=== "VS Code"

    Temporarily move the files out of the way:

    ```bash
    mv .github/copilot-instructions.md /tmp/
    mv .github/instructions /tmp/instructions-backup
    ```

    Open a **new** chat and paste the same search prompt.

    Put them back when you're done:

    ```bash
    mv /tmp/copilot-instructions.md .github/
    mv /tmp/instructions-backup .github/instructions
    ```

=== "Copilot CLI"

    No file shuffling needed:

    ```text
    /instructions
    ```

    Toggle all three files off, then start a new session and paste the same search prompt.
    Toggle them back on afterwards.

Compare the two outputs side by side. Typically the uninstructed version returns `List<Book>` straight out of the controller, puts the query logic in the controller, picks a method name at random, and either skips tests or writes a `@SpringBootTest` that boots the whole application.

That gap is what the file bought you.

## Gotchas

- **Instructions do nothing for inline completions.** The grey ghost text as you type ignores them completely. Chat and agent only. This is the single most common "it isn't working" report.
- **Use `applyTo` for file scoping.** The [VS Code reference](https://code.visualstudio.com/docs/agent-customization/custom-instructions) also documents selection by a matching `description`. Keep the explicit glob in this lab so the intended Java paths are clear.
- **Globs are relative to the workspace root.** `src/main/java/**` matches nothing here. It has to be `library-api/src/main/java/**/*.java`.
- **Copilot CLI defines no precedence order.** It reads `.github/copilot-instructions.md`, `.github/instructions/`, `~/.copilot/`, `AGENTS.md`, `CLAUDE.md`, and `GEMINI.md`, and the docs are explicit that there is no defined ordering between them. Don't build a layered override scheme on top of that assumption. Use `/instructions` to see reality.
- **Longer is not better.** Past a certain length, individual rules start getting ignored. If a rule matters, it needs to be short and specific. "Write clean code" does nothing.
- **Repo-wide instructions apply to this whole repository**, including the `docs/` folder. That's why the file above says which directory is the application.

## Official docs

| Link | What it's good for |
| --- | --- |
| [Configure custom instructions in your IDE](https://docs.github.com/en/copilot/how-tos/configure-custom-instructions-in-your-ide/add-repository-instructions-in-your-ide) | The full `applyTo` glob reference |
| [Response customization](https://docs.github.com/en/copilot/concepts/prompting/response-customization) | The concept, tabbed per editor |
| [Custom instructions support matrix](https://docs.github.com/en/copilot/reference/custom-instructions-support) | Exactly which instruction types work on which surface |
| [Add custom instructions to Copilot CLI](https://docs.github.com/en/copilot/how-tos/copilot-cli/customize-copilot/add-custom-instructions) | The CLI's wider discovery model |
| [VS Code: custom instructions](https://code.visualstudio.com/docs/agent-customization/custom-instructions) | VS Code specifics and settings |
| [Your first custom instructions](https://docs.github.com/en/copilot/tutorials/customization-library/custom-instructions/your-first-custom-instructions) | GitHub's own walkthrough |
| [Customization library](https://docs.github.com/en/copilot/tutorials/customization-library) | Eight more ready-made instruction recipes |
| [MS Learn: configure instructions and custom agents](https://learn.microsoft.com/en-us/training/modules/configure-customize-github-copilot-visual-studio-code/) | A longer guided module, C# rather than Java |
| [GitHub Skills: customize your Copilot experience](https://github.com/skills/customize-your-github-copilot-experience) | Free hands-on course, under 30 minutes |
| [GitHub: prompt engineering](https://docs.github.com/en/copilot/concepts/prompting/prompt-engineering) | Concrete requirements, examples, and iteration when a rule is missed |
| [Microsoft Learn: prompt engineering with Copilot](https://learn.microsoft.com/en-us/training/modules/introduction-prompt-engineering-with-github-copilot/) | Practice giving context and refining requests |
| [VS Code: agent customization](https://code.visualstudio.com/docs/agents/concepts/customization) | Decide whether a requirement belongs in instructions, a skill, or an enforcement mechanism |

## Next

Nothing here is a prerequisite for anything, so take your pick:

- [Agent skills](agent-skills.md)
- [Custom agents](custom-agents.md)
- [Capstone](../capstone.md)

Finished files are in [`solutions/custom-instructions/`](https://github.com/sabajamalian/github-copilot-101/tree/main/solutions/custom-instructions).

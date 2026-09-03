# Custom agents

**~25 minutes.** Standalone: nothing here needs the other two modules.

## Why this exists

Instructions change how Copilot writes code. Skills give it a procedure. A custom agent changes *what job it thinks it has*, including which tools it's allowed to touch.

That last part is the interesting bit. An agent with `tools: ["read", "search"]` cannot edit your files. Not "has been asked not to", cannot. That turns "please just review this, don't change anything" from a hopeful request into a constraint.

!!! info "These used to be called custom chat modes"
    Same feature, new name, and `.chatmode.md` files are now `.agent.md` in `.github/agents/`.
    Old files still work and VS Code offers a rename quick-fix. If you're following an older
    tutorial that says "chat mode", it means this.

## What you'll build

```text
.github/agents/
├── api-reviewer.agent.md     # can read and search, cannot edit
└── feature-builder.agent.md  # can build and run, follows house rules
```

Then you'll turn the reviewer loose on `LoanController` (which has plenty to say about it), and hand the builder a real feature.

## Build it

The files are the same on both surfaces. Loading and invoking are not, so those get tabs.

Work from the **repo root**.

### 1. The reviewer

```bash
mkdir -p .github/agents
```

Create `.github/agents/api-reviewer.agent.md`:

```markdown title=".github/agents/api-reviewer.agent.md"
---
name: api-reviewer
description: Reviews library-api code for layering, error handling, validation, and API design problems. Reports findings only and never edits files. Use when asked to review, audit, or critique code in library-api.
tools: ["read", "search"]
---

You review Java code in `library-api/`. You do not write code and you do not edit files.
You have no edit tool, so if you find yourself wanting to fix something, describe the fix
instead.

## What to look for, in priority order

1. **Layering.** Business logic in a controller instead of a service. Repositories injected
   directly into controllers.
2. **Error handling.** Returning `null` for a missing resource. Bare `RuntimeException`.
   The same failure producing different HTTP statuses in different endpoints.
3. **Validation.** Request bodies accepted without Jakarta Bean Validation and `@Valid`.
4. **API surface.** JPA entities used in controller signatures instead of records.
5. **Duplication.** The same policy constant declared in more than one class.
6. **Tests.** Endpoints with no test, especially their not-found paths.

## How to report

Group findings by file. For each one give:

- The file and line
- What is wrong, in one sentence
- Why it matters, in one sentence
- The concrete fix, described in prose, not as a patch

Sort by severity. Say so plainly if a file is fine. Do not pad the list to look thorough.
Finish with the single change that would improve the codebase most, and nothing else.
```

The `tools` list is the whole point. `read` and `search` and nothing else.

### 2. The builder

Create `.github/agents/feature-builder.agent.md`:

```markdown title=".github/agents/feature-builder.agent.md"
---
name: feature-builder
description: Implements a feature in library-api end to end, including service logic, records, validation, error handling, and tests, then runs the build. Use when asked to add, implement, or build a feature or endpoint in library-api.
tools: ["read", "edit", "search", "execute", "todo"]
---

You implement features in `library-api`, a Spring Boot 3 REST API on Java 21 with
Spring Data JPA and H2.

## Before writing anything

Read the package you are about to change. The code is inconsistent on purpose, so follow the
rules below rather than the nearest file.

## House rules

- Controllers handle HTTP only. Logic goes in a service in the same package. The `loan`
  package has no service yet, so a loan feature means creating `LoanService` and moving the
  existing checkout and return logic out of `LoanController` first.
- Never accept or return a JPA entity from a controller. Use Java records in the same package,
  named `XxxRequest` and `XxxResponse`.
- Validate request records with Jakarta Bean Validation and `@Valid` on the controller
  parameter. Add `spring-boot-starter-validation` to `library-api/pom.xml` if it is missing.
- Not found throws `NotFoundException` from `com.example.library.support`. Invalid state
  throws `IllegalStateException`. Never return `null`, never throw bare `RuntimeException`.
  Map both centrally in a `@RestControllerAdvice`.
- The loan period is 14 days and is currently declared in both `LoanController` and
  `BookService`. Do not add a third copy.
- Name controller methods after the HTTP action: `getBooks`, `createBook`, `returnLoan`.

## Finish the job

Add `@WebMvcTest` tests with `MockMvc` for the happy path and every failure path. Name them
`method_condition_expectedResult`.

Then run `mvn -f library-api/pom.xml test` and fix what breaks. Do not report success on a
red build.

When you are done, summarise in under ten lines: files added, files changed, tests added,
and anything you deliberately left out.
```

### 3. Load them

=== "VS Code"

    Agents in `.github/agents/` are picked up from the workspace. Open Chat and use the
    **agent picker** at the top of the chat input. `api-reviewer` and `feature-builder`
    should be in the list alongside the built-in modes.

    Pick one and it stays selected until you change it.

    VS Code detects any `.md` file in `.github/agents/` as a custom agent, but stick with the
    `.agent.md` suffix so the same file works in the CLI and on GitHub.com.

=== "Copilot CLI"

    **Restart the CLI.** Unlike skills, there is no reload command for agents, so a new
    `.agent.md` won't appear in a running session.

    Then:

    ```text
    /agent
    ```

    Pick from the list and type your prompt. You can also run one straight from your shell:

    ```bash
    copilot --agent api-reviewer --prompt "Review LoanController"
    ```

    The name there is the filename without `.agent.md`, which is usually but not necessarily
    the `name` in the frontmatter.

    `/agent` also has a **Create new agent** flow that will interview you and write the file,
    including picking tools. Worth trying once you've written one by hand and know what the
    fields do.

    Note: the CLI's built-in agents (`explore`, `task`) don't show up in `/agent`.

## Try it

### Review something

=== "VS Code"

    Select `api-reviewer` in the agent picker, then:

    ```text
    Review the loan package.
    ```

=== "Copilot CLI"

    ```text
    /agent
    ```

    Choose `api-reviewer`, then:

    ```text
    Review the loan package.
    ```

    Or in one shot from your shell:

    ```bash
    copilot --agent api-reviewer --prompt "Review the loan package."
    ```

You should get a grouped list of findings and **no file changes**. It'll flag the checkout and return logic sitting in the controller, `BookRepository` injected straight into `LoanController`, `IllegalArgumentException` for not-found, the `Loan` entity used as both request and response, and the duplicated `LOAN_PERIOD_DAYS`.

Now the part worth watching: ask it to fix something.

```text
Good. Now fix the first one.
```

It can't. It has no edit tool. It'll describe the change instead. That's a hard boundary from the frontmatter, not the model being agreeable.

### Build something

Switch to `feature-builder` and give it a real feature:

```text
Add holds. A member should be able to place a hold on a book that is currently checked out,
and the hold queue should be first come first served. Include the endpoints to place a hold
and to list the holds on a book.
```

Then check the work:

```bash
mvn -f library-api/pom.xml test
git diff --stat
```

Look for `LoanService` being created and the old controller logic moved into it, records rather than entities on the wire, validation on the request, a `@RestControllerAdvice`, and tests covering the "book isn't actually checked out" case.

## See the difference

Run the same review prompt without the agent.

=== "VS Code"

    Switch the agent picker back to the built-in **Agent** mode and paste:

    ```text
    Review the loan package.
    ```

=== "Copilot CLI"

    Start a plain session, no `/agent`, and paste:

    ```text
    Review the loan package.
    ```

Default agent mode will usually start editing files partway through the review, because that's what a general-purpose coding agent does. You asked for a review and you got a diff.

`git status` afterwards tells the story better than anything written here.

## Subagents

A subagent is a separate agent spun up with its own context window to handle a piece of work. It keeps a big messy task from clogging the main conversation.

This is where the two surfaces genuinely diverge.

=== "VS Code"

    Let one agent call another with the `agents` frontmatter property, which acts as an
    allowlist:

    ```markdown
    ---
    description: Plans a change, then hands off to the builder.
    tools: ["read", "search"]
    agents: ["feature-builder"]
    ---
    ```

    VS Code also supports `handoffs`, which puts a button at the end of a response to pass
    the work along:

    ```markdown
    ---
    description: Produces an implementation plan for a library-api feature.
    tools: ["read", "search"]
    handoffs:
      - label: Build it
        agent: feature-builder
        prompt: Implement the plan above.
        send: false
    ---
    ```

    `handoffs` and `argument-hint` are VS Code only. GitHub.com ignores them, which is fine,
    the file still works, those fields just do nothing there.

=== "Copilot CLI"

    In the CLI the two ideas collapse: work performed by a custom agent *is* carried out by a
    subagent with its own context window. You don't wire it up, it's how invocation works.

    The CLI also ships built-in agents you can delegate to:

    - `explore` for read-only codebase search
    - `task` for running tests, builds, and linters

    These don't appear in `/agent`. Ask for them by name:

    ```text
    Use the explore agent to find everywhere the loan period is defined.
    ```

## Frontmatter worth knowing

Only `description` is required. Everything else has a default.

| Property | Notes |
| --- | --- |
| `description` | **Required.** Also how Copilot decides to pick this agent on its own. |
| `name` | Optional display name. Falls back to the filename. |
| `tools` | List or comma-separated string. Omit or `["*"]` for all tools, `[]` for none. |
| `model` | Optional. Inherits your default if unset. |
| `disable-model-invocation` | `true` means only you can pick it, Copilot never will. |
| `user-invocable` | `false` means it can only be reached programmatically. |
| `target` | `vscode` or `github-copilot`. Both if unset. |
| `infer` | **Retired.** Use the two properties above instead. |

Tool aliases you'll actually use: `read`, `edit`, `search`, `execute` (also spelled `shell`), `agent`, `web`, `todo`.

## Gotchas

- **Omitting `tools` grants everything.** A "reviewer" without a `tools` list is just regular Copilot wearing a hat. The restriction is the feature.
- **The CLI needs a restart** to see a new agent. There's no `/agent reload`. Skills have `/skills reload`, agents don't.
- **Home directory wins over the repo.** An agent named `api-reviewer` in `~/.copilot/agents/` shadows the one in `.github/agents/`. Confusing when a teammate's copy behaves differently from yours.
- **Unknown tool names are silently ignored.** Typo `serach` and you just don't get search, with no error. Check against the alias list.
- **`.chatmode.md` is the old name.** Rename to `.agent.md`.
- **`mcp-servers` and `metadata` are cloud-only**, `handoffs` and `argument-hint` are VS Code only. Mixing them is harmless, the irrelevant ones are ignored.
- **Prompts cap at 30,000 characters.** Not a limit you'll hit by accident, but it exists.
- **Subagents don't work everywhere.** VS Code yes, Copilot CLI yes, GitHub.com no, Visual Studio no.

## Official docs

| Link | What it's good for |
| --- | --- |
| [Custom agents configuration](https://docs.github.com/en/copilot/reference/custom-agents-configuration) | Every frontmatter property and the full tool alias table |
| [About custom agents (Copilot CLI)](https://docs.github.com/en/copilot/concepts/agents/copilot-cli/about-custom-agents) | How subagents work in the CLI, plus the built-in agents |
| [Create custom agents for Copilot CLI](https://docs.github.com/en/copilot/how-tos/copilot-cli/customize-copilot/create-custom-agents-for-cli) | The `/agent` creation flow and the four ways to invoke one |
| [About custom agents (cloud agent)](https://docs.github.com/en/copilot/concepts/agents/cloud-agent/about-custom-agents) | The GitHub.com side |
| [Create custom agents on GitHub.com](https://docs.github.com/en/copilot/how-tos/copilot-on-github/customize-copilot/customize-cloud-agent/create-custom-agents) | Org and enterprise level agents |
| [VS Code: custom agents](https://code.visualstudio.com/docs/agent-customization/custom-agents) | `handoffs`, the `agents` allowlist, and the chat mode rename |
| [Your first custom agent](https://docs.github.com/en/copilot/tutorials/customization-library/custom-agents/your-first-custom-agent) | GitHub's walkthrough, plus three more ready-made agents |
| [How to use agents, skills, and instructions in Copilot CLI](https://www.youtube.com/watch?v=-yKALFS5ewY) | GitHub's own video walkthrough |
| [MS Learn: configure instructions and custom agents](https://learn.microsoft.com/en-us/training/modules/configure-customize-github-copilot-visual-studio-code/) | Longer guided module, includes agent handoffs |

## Next

No module depends on this one, so take your pick:

- [Custom instructions](custom-instructions.md)
- [Agent skills](agent-skills.md)
- [Capstone](../capstone.md)

Finished files are in [`solutions/custom-agents/`](https://github.com/sabajamalian/github-copilot-101/tree/main/solutions/custom-agents).

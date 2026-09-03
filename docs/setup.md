# Setup

Five minutes. Clone, run, confirm Copilot is alive.

## What you need

- **JDK 21 or newer.** Check with `java -version`.
- **Maven 3.9+.** Check with `mvn -v`.
- **A GitHub Copilot subscription**, and at least one of:
    - [Copilot in VS Code](https://code.visualstudio.com/docs/copilot/setup)
    - [Copilot CLI](https://docs.github.com/en/copilot/how-tos/copilot-cli/set-up-copilot-cli/install-copilot-cli)
    - [The GitHub Copilot app](https://github.com/features/ai/github-app)
- **curl**, or any HTTP client you like.

No Docker, no database to install. The app uses H2 in memory and seeds itself on startup.

The three customization modules use VS Code or the CLI, and every step has a tab for each. The [Copilot CLI](modules/copilot-cli.md) and [GitHub Copilot app](modules/copilot-app.md) modules walk you through installing their own tool, so you don't need it before you start.

## Clone and run

```bash
git clone https://github.com/sabajamalian/github-copilot-101.git
cd github-copilot-101/library-api
mvn spring-boot:run
```

First run pulls dependencies and takes a minute or two. After that it's a few seconds.

## Confirm it works

In another terminal:

```bash
curl -s localhost:8080/books
```

You should get a JSON array of seeded books. A couple more worth trying, because you'll meet them again in the labs:

```bash
# A book that doesn't exist. Returns HTTP 200 and an empty body. That is a bug.
curl -i localhost:8080/books/999

# A member that doesn't exist. Returns HTTP 500. Also a bug, and a different one.
curl -i localhost:8080/members/999

# Check out a book
curl -s -X POST localhost:8080/loans \
  -H 'Content-Type: application/json' \
  -d '{"bookId": 1, "memberId": 1}'
```

Those two inconsistent failures are the app's real behavior, and they're the reason the custom instructions lab has something to bite on.

## Confirm Copilot works

=== "VS Code"

    1. Open the repo root in VS Code (the folder containing both `docs/` and `library-api/`), not just `library-api/`. Copilot looks for customization files under `.github/` at the workspace root.
    2. Open Chat with ++cmd+ctrl+i++ on macOS or ++ctrl+alt+i++ on Windows and Linux.
    3. Switch the mode picker to **Agent**.
    4. Ask: `What does BookController do?`

    If you get a real answer that names actual methods, you're set.

=== "Copilot CLI"

    1. From the repo root:

        ```bash
        cd github-copilot-101
        copilot
        ```

    2. If it's your first time, run `/login` and follow the prompts.
    3. Ask: `What does BookController do?`

    If you get a real answer that names actual methods, you're set.

    Useful once you start the labs: `/instructions` lists what Copilot loaded, `/agent` lists available agents.

!!! tip "Open the repo root, not `library-api/`"
    Copilot discovers `.github/copilot-instructions.md`, `.github/skills/`, and `.github/agents/` relative to the workspace or repo root. Open `library-api/` on its own and every lab will look like it silently did nothing.

## About the code

`library-api` is a small library lending API. Three entities, `Book`, `Member`, and `Loan`, and endpoints to list books, check one out, and return it.

It also has eight deliberate flaws. They're listed in [`library-api/README.md`](https://github.com/sabajamalian/github-copilot-101/blob/main/library-api/README.md) so you don't waste lab time wondering whether you broke something. Short version: no validation, no DTOs, three different ways of failing, inconsistent method naming, business logic in the wrong layer, a magic number declared twice, one empty test, and no pagination.

Leave them alone for now. Each lab points at a different one.

## Where to go

The surfaces:

- [Copilot CLI](modules/copilot-cli.md)
- [GitHub Copilot app](modules/copilot-app.md)

The customizations:

- [Custom instructions](modules/custom-instructions.md)
- [Agent skills](modules/agent-skills.md)
- [Custom agents](modules/custom-agents.md)

Any order. No dependencies between them.

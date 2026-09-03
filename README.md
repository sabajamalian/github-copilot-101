# Agentic coding with GitHub Copilot

Self-paced labs for GitHub Copilot: the two surfaces you run it on, and the three ways you shape
what it does. Taught against a real Spring Boot API that lives in this repo.

**[Start here →](https://sabajamalian.github.io/github-copilot-101/)**

## The modules

| Module | What you build | Time |
| --- | --- | --- |
| [Copilot CLI](https://sabajamalian.github.io/github-copilot-101/modules/copilot-cli/) | Interactive sessions, then a headless script and an Actions workflow | ~45 min |
| [GitHub Copilot app](https://sabajamalian.github.io/github-copilot-101/modules/copilot-app/) | Projects, parallel worktree sessions, a canvas, repo config | ~40 min |
| [Custom instructions](https://sabajamalian.github.io/github-copilot-101/modules/custom-instructions/) | Repo-wide and path-scoped instruction files | ~20 min |
| [Agent skills](https://sabajamalian.github.io/github-copilot-101/modules/agent-skills/) | Two skills, one with a bundled shell script | ~25 min |
| [Custom agents](https://sabajamalian.github.io/github-copilot-101/modules/custom-agents/) | A read-only reviewer and a feature builder | ~25 min |

**They're independent.** No ordering, no prerequisites. Start with whichever one you need today.

The three customization modules have instructions for both **VS Code** and **Copilot CLI**.

## What's in here

```text
docs/         the site
library-api/  Spring Boot 3 / Java 21 REST API with deliberate rough edges
solutions/    finished files from every module
```

`library-api/` has real problems in it on purpose: missing validation, JPA entities on the wire,
inconsistent error handling, business logic in a controller. That's the material. See
[`library-api/README.md`](library-api/README.md) for the full list.

There is deliberately **no** `.github/copilot-instructions.md`, `.github/skills/`,
`.github/agents/`, or `.github/github-app.yml` in this repo. Those are what you create.

## Run the API

```bash
mvn -f library-api/pom.xml spring-boot:run
curl localhost:8080/books
```

Needs JDK 21+ and Maven 3.9+.

## Build the site locally

```bash
pip install -r requirements.txt
mkdocs serve
```

## Deploying your own copy

`.github/workflows/docs.yml` builds and deploys on every push to `main` that touches `docs/`,
`mkdocs.yml`, or `requirements.txt`.

One manual step: **Settings → Pages → Source → GitHub Actions**. The workflow can't set that
for you, and the first deploy fails without it.

Change `site_url` and `repo_url` in `mkdocs.yml` to point at your fork.

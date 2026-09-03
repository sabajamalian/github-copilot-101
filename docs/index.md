# Agentic coding with GitHub Copilot

Five hands-on labs. Most take about 25 minutes. Do them in any order you like.

You'll work against a Spring Boot API that lives in this same repo, and it has real problems in it: no validation, JPA entities leaking out of controllers, error handling that does something different in every file. That's on purpose. Toy code makes customization look pointless, because a clean codebase gives Copilot nothing to get wrong.

The modules split into two halves: **where you run Copilot**, and **how you shape what it does**.

## The surfaces

Where the agent actually runs. Both are standalone, and neither is required for the customization modules.

|  | What it is | You reach for it when |
| --- | --- | --- |
| [Copilot CLI](modules/copilot-cli.md) | The agent in your terminal, interactive or headless | You live in a shell, or you want Copilot in a script, a git hook, or CI |
| [GitHub Copilot app](modules/copilot-app.md) | The desktop app, built on the CLI | You want parallel sessions, real diffs, PRs, and canvases |

## The customizations

How you shape what the agent does. These work in **VS Code** and **Copilot CLI**, and every step has a tab for each.

|  | What it is | You reach for it when |
| --- | --- | --- |
| [Custom instructions](modules/custom-instructions.md) | Standing context Copilot reads on every request | You keep making the same correction over and over |
| [Agent skills](modules/agent-skills.md) | A named procedure Copilot pulls in when the task fits | You keep doing the same multi-step task by hand |
| [Custom agents](modules/custom-agents.md) | A persona with its own prompt, tool list, and model | You want a narrower, safer, or more focused Copilot |

The short version of how to choose between those three: instructions change *how* Copilot writes code. Skills give it *a procedure* to follow. Agents give it *a job description*.

GitHub's [customization cheat sheet](https://docs.github.com/en/copilot/reference/customization-cheat-sheet) has the full comparison, including which editors support what.

## Pick one and go

<div class="grid cards" markdown>

-   **[Copilot CLI](modules/copilot-cli.md)**

    Install it, pick a model, learn the slash commands that matter, run two sessions side by side. Then go headless with a fine-grained token and put it in a script and a workflow.

-   **[GitHub Copilot app](modules/copilot-app.md)**

    Add a project, run parallel sessions in isolated worktrees, build a canvas you and the agent both edit, and configure the repo so it's clone-and-go for the whole team.

-   **[Custom instructions](modules/custom-instructions.md)**

    Teach Copilot this project's conventions once, stop repeating yourself. Then add search-by-author and watch it follow rules nobody mentioned in the prompt.

-   **[Agent skills](modules/agent-skills.md)**

    Package "add a REST endpoint here, properly" as a reusable skill, script and all. Then ask for loan renewal and watch the skill fire on its own.

-   **[Custom agents](modules/custom-agents.md)**

    Build a reviewer that can read but not write, and a builder that ships a feature end to end. Then hand it the holds feature.

</div>

## No module depends on another

This matters, so plainly: **you can do these in any order, or just one.**

Every module creates its own files and reads nothing from the others. If you want custom agents and have no interest in skills, go straight to custom agents. Nothing will be missing.

Where a module would genuinely be nicer with another module's output, the finished files are sitting in [`solutions/`](https://github.com/sabajamalian/github-copilot-101/tree/main/solutions) so you can copy them without doing the lab.

## Before you start

[Installation](installation.md) gets Copilot running in VS Code, IntelliJ IDEA, Eclipse, or the CLI, and compares what each one supports.

[Setup](setup.md) takes about five minutes: clone, `mvn spring-boot:run`, one curl to confirm it's alive.

## Then what

The [capstone](capstone.md) ships an overdue-fee feature using instructions, skills, and agents together. Skip it if you only came for one module.

[Resources](resources.md) is every official link in one place: GitHub Docs, VS Code docs, Microsoft Learn, GitHub Skills, and the GitHub YouTube channel.

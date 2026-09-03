# Agentic coding with GitHub Copilot

Three hands-on labs. Roughly 25 minutes each. Do them in any order you like.

You'll customize Copilot against a Spring Boot API that lives in this same repo, and it has real problems in it: no validation, JPA entities leaking out of controllers, error handling that does something different in every file. That's on purpose. Toy code makes customization look pointless, because a clean codebase gives Copilot nothing to get wrong.

Everything works in **VS Code** and **Copilot CLI**. Every step below has a tab for each.

## The three things

|  | What it is | You reach for it when |
| --- | --- | --- |
| [Custom instructions](modules/custom-instructions.md) | Standing context Copilot reads on every request | You keep making the same correction over and over |
| [Agent skills](modules/agent-skills.md) | A named procedure Copilot pulls in when the task fits | You keep doing the same multi-step task by hand |
| [Custom agents](modules/custom-agents.md) | A persona with its own prompt, tool list, and model | You want a narrower, safer, or more focused Copilot |

The short version of how to choose: instructions change *how* Copilot writes code. Skills give it *a procedure* to follow. Agents give it *a job description*.

GitHub's [customization cheat sheet](https://docs.github.com/en/copilot/reference/customization-cheat-sheet) has the full comparison, including which editors support what.

## Pick one and go

<div class="grid cards" markdown>

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

[Setup](setup.md) takes about five minutes: clone, `mvn spring-boot:run`, one curl to confirm it's alive.

## Then what

The [capstone](capstone.md) ships an overdue-fee feature using all three together. Skip it if you only came for one module.

[Resources](resources.md) is every official link in one place: GitHub Docs, VS Code docs, Microsoft Learn, GitHub Skills, and the GitHub YouTube channel.

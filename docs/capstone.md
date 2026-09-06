# Capstone

**Optional. ~30 minutes.** Skip it without guilt, the three modules stand on their own.

## The idea

Each module gave you one tool. This is what happens when they overlap.

Ship **late fees**: charge a member when a loan comes back past its due date.

| Piece | Doing what |
| --- | --- |
| Custom instructions | Records not entities, `@Valid` on requests, `NotFoundException` for missing rows. Set once, never repeated in a prompt. |
| Agent skill | The endpoint recipe. Controller, service, records, tests, run the build. |
| Custom agent | `feature-builder` does the work, `api-reviewer` checks it after. |

None of them tell it *what* to build. That's still your job, and it's the point: the setup handles the how so your prompt only has to carry the what.

Microsoft's [agent customization guide](https://code.visualstudio.com/docs/agents/concepts/customization)
explains how persistent instructions, reusable workflows, and specialized roles combine.
GitHub's [customization cheat sheet](https://docs.github.com/en/copilot/reference/customization-cheat-sheet)
is the reference for which parts your chosen surface supports.

## Setup

You need the artifacts from all three modules. Either do the modules, or copy the finished files:

```bash
cp solutions/custom-instructions/copilot-instructions.md .github/copilot-instructions.md
mkdir -p .github/instructions .github/skills .github/agents
cp solutions/custom-instructions/instructions/*.instructions.md .github/instructions/
cp -r solutions/agent-skills/skills/* .github/skills/
cp solutions/custom-agents/agents/*.agent.md .github/agents/
chmod +x .github/skills/smoke-test/smoke.sh
```

=== "VS Code"

    Reload the window: ++cmd+shift+p++ or ++ctrl+shift+p++, then **Developer: Reload Window**.

=== "Copilot CLI"

    Restart the CLI. Agents only load at startup.

    ```text
    /instructions
    /skills list
    /agent
    ```

    All three should show your files.

## The prompt

Pick `feature-builder`, then:

```text
Add late fees.

When a loan is returned after its due date, charge the member 25 cents per day overdue,
capped at the replacement cost of the book. Store the fee on the loan. Add an endpoint to
list a member's outstanding fees and one to mark them paid.

Books don't have a replacement cost yet. Add one, default it to 20 dollars for the seeded
books, and use money types that don't lose cents.
```

Watch what you didn't have to say: no mention of DTOs, validation, exception types, test naming, or running the build. That's the instructions and the skill doing their jobs.

## Check it

```bash
mvn -f library-api/pom.xml test
git diff --stat
```

Then have the reviewer grade the work. Switch to `api-reviewer` and:

```text
Review the late fee changes on this branch.
```

It can read and search but not edit, so you get a critique and not a second round of edits on top of the first.

If it flags something real, hand the finding back to `feature-builder`. Two agents, one pipeline, and you're the one deciding what ships.

GitHub's [writing tests tutorial](https://docs.github.com/en/copilot/tutorials/write-tests)
shows how to ask for boundary and failure cases. Apply that here by checking the exact due-date
boundary, the replacement-cost cap, and repeated payment requests. Read the assertions yourself;
a passing test can still encode the wrong policy.

Before merging, use GitHub's [protected branches guide](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-protected-branches/about-protected-branches)
to configure required reviews and status checks if this is a shared repository. The reviewer
agent's response doesn't create a merge requirement.

## The bit worth noticing

`BigDecimal` for money without being asked. The fee cap enforced in a service rather than a controller. Tests for the exactly-on-time boundary. A `LoanService` finally existing.

Nothing in the prompt asked for any of that. All of it came from files you wrote once.

That's the whole argument for customization: you're not writing better prompts, you're building a place for the context to live so you stop retyping it.

## Official follow-up

| Reference | Apply it here |
| --- | --- |
| [GitHub: Copilot Agents application card](https://docs.github.com/en/copilot/responsible-use/agents) | Understand the limits of agent-generated code and review feedback |
| [VS Code: trust and safety](https://code.visualstudio.com/docs/agents/concepts/trust-and-safety) | Review diffs and keep approvals and trust boundaries in place |
| [Microsoft Learn: develop unit tests using Copilot](https://learn.microsoft.com/en-us/training/modules/develop-unit-tests-using-github-copilot-tools/) | Practice edge-case test generation; exercises use C# and .NET rather than this lab's Java stack |
| [Microsoft Learn: code reviews and pull requests](https://learn.microsoft.com/en-us/training/modules/code-reviews-pull-requests-github-copilot/) | Evaluate suggested changes before accepting them |
| [Microsoft Learn: responsible AI with Copilot](https://learn.microsoft.com/en-us/training/modules/responsible-ai-with-github-copilot/) | Review operational risks and accountability for generated output |

## Where to go next

You wrote every one of those files by hand, and that was the point: you now know what goes in them and why. From here you don't have to. [Awesome GitHub Copilot](https://awesome-copilot.github.com/) is a browsable gallery of community-contributed agents, instructions, skills, plugins, and canvas extensions, filterable by type and copyable straight from the page. Read a file before you install it, especially any skill that asks for shell access.

- [Copilot CLI](modules/copilot-cli.md) and the [GitHub Copilot app](modules/copilot-app.md) are the two surfaces this all runs on. Everything you built above works on both, unchanged.
- [Customization library](https://docs.github.com/en/copilot/tutorials/customization-library) has ready-made recipes for all three types, written by GitHub rather than the community
- [github/awesome-copilot](https://github.com/github/awesome-copilot) is the repo behind the gallery, where you read the raw file or open a PR to contribute one
- [Copilot customization cheat sheet](https://docs.github.com/en/copilot/reference/customization-cheat-sheet) tells you what works where
- [All resources](resources.md)

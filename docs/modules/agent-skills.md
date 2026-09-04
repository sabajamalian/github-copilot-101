# Agent skills

**~25 minutes.** Standalone: nothing here needs the other two modules.

## Why this exists

Custom instructions are always on. That makes them great for "how we write code here" and bad for anything long, because a big always-on file dilutes everything in it.

A skill is a folder Copilot pulls into context **only when the task matches**. It can carry scripts and other files, not just text. GitHub's own guidance splits it this way: instructions for simple things relevant to nearly every task, skills for detailed procedures Copilot should only see when relevant.

Concretely: if you keep typing the same six-step request, that's a skill.

## Watch it first

<div class="video-embed">
  <iframe
    src="https://www.youtube-nocookie.com/embed/-yKALFS5ewY"
    title="How to use agents, skills, and instructions in Copilot CLI"
    loading="lazy"
    referrerpolicy="strict-origin-when-cross-origin"
    allow="accelerometer; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
    allowfullscreen></iframe>
</div>

**[How to use agents, skills, and instructions in Copilot CLI](https://www.youtube.com/watch?v=-yKALFS5ewY)**, 6 min, official GitHub channel.

Optional. It's CLI-flavoured and covers all three customization types, so the skills portion is only part of it. Good for seeing a skill fire in a real session before you write one.

Want to watch someone build a skill from scratch instead? [RDT: Building agent Skills and the new MCP release](https://www.youtube.com/watch?v=2AZJbyD-9ME) is an hour-long livestream that does exactly that.

## What you'll build

```text
.github/skills/
├── add-endpoint/
│   └── SKILL.md
└── smoke-test/
    ├── SKILL.md
    └── smoke.sh
```

The first is pure procedure. The second bundles a shell script, which is the part people usually don't realise skills can do.

Then you'll ask for loan renewal and watch Copilot reach for the skill without being told to.

## Build it

Same files on both surfaces, so build them wherever you like. The tabs come back for loading and invoking.

Work from the **repo root**.

### 1. The `add-endpoint` skill

```bash
mkdir -p .github/skills/add-endpoint
```

Create `.github/skills/add-endpoint/SKILL.md`:

```markdown title=".github/skills/add-endpoint/SKILL.md"
---
name: add-endpoint
description: Add a REST endpoint to library-api end to end, including controller method, service logic, request and response records, validation, error handling, and a MockMvc test. Use this whenever someone asks to add, expose, or create an endpoint, route, or API operation in library-api.
---

# Add an endpoint to library-api

Work through these steps in order. Do not stop before step 6.

## 1. Pick the package

Code lives in `library-api/src/main/java/com/example/library/`, one package per domain area:
`book`, `member`, `loan`. Put the new endpoint in the package that owns the resource.

## 2. Define the records

Never accept or return a JPA entity from a controller. In the same package, create Java records:

- Request: `CreateBookRequest`, `CheckoutLoanRequest`, and so on
- Response: `BookResponse`, `LoanResponse`, and so on

Annotate request record fields with Jakarta Bean Validation (`@NotBlank`, `@NotNull`,
`@Positive`). If `spring-boot-starter-validation` is not in `library-api/pom.xml`, add it.

## 3. Put the logic in a service

The controller method should be three lines or fewer: take the request, call the service,
return the response. If the package has no service class, create one. The `loan` package
does not have one yet, so a loan endpoint needs `LoanService` created first, and the existing
logic in `LoanController` moved into it.

## 4. Fail consistently

- Not found: throw `NotFoundException` from `com.example.library.support`, creating it if needed.
- Invalid state, for example returning a loan that is already returned: throw
  `IllegalStateException` with a message naming the problem.
- Never return `null` for a missing resource. Never throw bare `RuntimeException`.

Make sure a `@RestControllerAdvice` exists that maps `NotFoundException` to 404 and
`IllegalStateException` to 409, returning `{"error": "...", "detail": "..."}`.

## 5. Reuse policy constants

The loan period is 14 days. It is currently declared in both `LoanController` and
`BookService`. Do not add a third copy. Move it to a single constant and reference that.

## 6. Test it

Add a `@WebMvcTest` class using `MockMvc`. Cover the happy path and every failure path the
endpoint can produce. Name tests `method_condition_expectedResult`, for example
`renewLoan_whenAlreadyReturned_returns409`. Assert with AssertJ.

Then run `mvn -f library-api/pom.xml test` and fix anything red before reporting back.
```

!!! tip "The description is the trigger"
    `description` is not documentation, it's the routing rule. Copilot reads it to decide
    whether to load the skill. Notice it says both *what the skill does* and *when to use it*,
    including the words a person would actually type: add, expose, create, endpoint, route,
    API operation. A description like "Helper for endpoints" will basically never fire.

### 2. The `smoke-test` skill, with a script

```bash
mkdir -p .github/skills/smoke-test
```

Create `.github/skills/smoke-test/smoke.sh`:

```bash title=".github/skills/smoke-test/smoke.sh"
#!/usr/bin/env bash
set -uo pipefail

BASE="${1:-http://localhost:8080}"
failures=0

check() {
  local method="$1" path="$2" expected="$3"
  local code
  code=$(curl -s -o /dev/null -w '%{http_code}' -X "$method" "$BASE$path")
  if [ "$code" = "$expected" ]; then
    printf 'ok    %-6s %-16s %s\n' "$method" "$path" "$code"
  else
    printf 'FAIL  %-6s %-16s got %s, want %s\n' "$method" "$path" "$code" "$expected"
    failures=$((failures + 1))
  fi
}

echo "Smoke testing $BASE"
check GET /books       200
check GET /books/1     200
check GET /members     200
check GET /members/1   200
check GET /loans       200

# Known bugs, asserted so we notice when they get fixed.
# A missing book answers 200 with an empty body; a missing member blows up with 500.
check GET /books/999   200
check GET /members/999 500

echo
if [ "$failures" -eq 0 ]; then
  echo "All checks passed."
else
  echo "$failures check(s) failed."
fi
exit "$failures"
```

Make it executable:

```bash
chmod +x .github/skills/smoke-test/smoke.sh
```

Create `.github/skills/smoke-test/SKILL.md`:

```markdown title=".github/skills/smoke-test/SKILL.md"
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
```

!!! warning "About `allowed-tools`"
    You can add `allowed-tools: shell` to the frontmatter to let a skill run commands without
    asking you first. It's convenient and it removes your last checkpoint. Only do it for
    scripts you have read yourself, from a source you trust. Left off, as it is above, Copilot
    asks before running `smoke.sh`. That's the safer default and it costs you one keypress.

### 3. Load them

=== "VS Code"

    Skills in `.github/skills/` are picked up from the workspace. Open a new Chat in **Agent**
    mode and ask:

    ```text
    What skills are available in this workspace?
    ```

    Both `add-endpoint` and `smoke-test` should be listed, with their descriptions.

    If you don't see them, check that the file is named exactly `SKILL.md` and that the
    `name` in the frontmatter matches the directory name.

=== "Copilot CLI"

    If a session is already open, pick up the new files without restarting:

    ```text
    /skills reload
    ```

    Then:

    ```text
    /skills list
    ```

    For details on one, including where it was loaded from:

    ```text
    /skills info add-endpoint
    ```

    `/skills` on its own opens a picker where you can enable and disable individual skills.
    You'll want that in a minute.

## Try it

Fresh session, Agent mode:

```text
Add an endpoint to renew a loan. POST /loans/{id}/renew should push the due date out by
the standard loan period, but only if the loan has not already been returned.
```

Watch for Copilot announcing it's using `add-endpoint`. Then check the result:

- Did it create `LoanService` and move the existing checkout and return logic into it, or did it pile more code into `LoanController`?
- Did it add `RenewLoanResponse` or similar rather than returning the `Loan` entity?
- Did it reuse the loan period constant instead of adding a third `14`?
- Is there a `@WebMvcTest` test for the already-returned case?

Then verify:

```bash
mvn -f library-api/pom.xml test
```

You can also force a skill instead of waiting for Copilot to choose it. Reference it by name with a slash:

```text
Use the /smoke-test skill to check the API.
```

Start the app first, in another terminal, or the skill will tell you to.

## See the difference

Run the same renewal prompt with the skill switched off.

=== "VS Code"

    ```bash
    mv .github/skills /tmp/skills-backup
    ```

    New chat, same prompt. Then put it back:

    ```bash
    mv /tmp/skills-backup .github/skills
    ```

=== "Copilot CLI"

    ```text
    /skills
    ```

    Disable `add-endpoint`, start a new session, run the same prompt. Re-enable it afterwards.

Without the skill you'll usually get a working endpoint with the logic sitting directly in `LoanController`, the `Loan` entity returned straight to the client, a fourth copy of `14`, and either no test or one that boots the entire application.

The endpoint works either way. The difference is whether it looks like the rest of a codebase you'd want to maintain.

## Skills or instructions?

Both are Markdown that changes Copilot's behaviour, so the line is worth being clear about.

| | Custom instructions | Agent skills |
| --- | --- | --- |
| Loaded | Always, or by file glob | Only when the description matches |
| Best for | Short rules that apply nearly everywhere | Long procedures for specific tasks |
| Can bundle scripts | No | Yes |
| Who decides | You, via `applyTo` | The model, via `description` |
| You can force it | Attach by hand | `/skill-name` in your prompt |

Rule of thumb: "always use records instead of entities" is an instruction. "Here are the six steps for adding an endpoint" is a skill.

## Gotchas

- **The filename must be exactly `SKILL.md`.** Uppercase, both words. `Skill.md` and `skill.md` do not load.
- **`name` must match the directory name**, lowercase with hyphens. Mismatches and invalid characters (slashes, colons, dots, spaces) make the skill fail to load **silently**. No error, it just isn't there. If a skill doesn't show up in `/skills list`, check this first.
- **Only `name` and `description` are required.** `license` is optional. Everything else you may have seen is editor-specific.
- **A vague `description` means the skill never fires.** It's the routing rule. Include the words people actually type.
- **`allowed-tools: shell` removes a safety step.** It pre-approves running terminal commands, which is exactly what a prompt injection would want. Read the script first.
- **Reference bundled files from `SKILL.md`.** GitHub's docs say the whole directory is made available; VS Code's docs say to reference extra files explicitly. Referencing them works on both, so just do it.
- **Added a skill mid-session in the CLI?** Run `/skills reload` rather than wondering why nothing happened.
- **Skills go further than prompt files.** They work in VS Code, Copilot CLI, GitHub.com, Copilot code review, and the cloud agent. Prompt files don't. If you're choosing between them today, this is the deciding factor.

## Official docs

| Link | What it's good for |
| --- | --- |
| [About agent skills](https://docs.github.com/en/copilot/concepts/agents/about-agent-skills) | The concept and where skills can live |
| [Add skills (Copilot CLI)](https://docs.github.com/en/copilot/how-tos/copilot-cli/customize-copilot/add-skills) | Full `/skills` command list, `allowed-tools`, script example |
| [Add skills (GitHub.com and cloud agent)](https://docs.github.com/en/copilot/how-tos/copilot-on-github/customize-copilot/customize-cloud-agent/add-skills) | Same feature from the GitHub.com side, plus `gh skill` |
| [VS Code: agent skills](https://code.visualstudio.com/docs/agent-customization/agent-skills) | The richest frontmatter reference, VS Code specifics |
| [Agent Skills specification](https://github.com/agentskills/agentskills) | The open standard, not GitHub-specific |
| [Copilot customization cheat sheet](https://docs.github.com/en/copilot/reference/customization-cheat-sheet) | Which surfaces support skills |
| [Awesome GitHub Copilot: skills](https://awesome-copilot.github.com/skills/) | Community skill gallery, browsable and filterable. Read before installing, it's third-party. |
| [How to use agents, skills, and instructions in Copilot CLI](https://www.youtube.com/watch?v=-yKALFS5ewY) | GitHub's own video walkthrough |
| [GitHub Skills: customize your Copilot experience](https://github.com/skills/customize-your-github-copilot-experience) | Free hands-on course, under 30 minutes |

## Next

No module depends on this one, so go wherever:

- [Custom instructions](custom-instructions.md)
- [Custom agents](custom-agents.md)
- [Capstone](../capstone.md)

Finished files are in [`solutions/agent-skills/`](https://github.com/sabajamalian/github-copilot-101/tree/main/solutions/agent-skills).

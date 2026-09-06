# Copilot CLI

**~45 minutes**, or about 20 if you stop at the halfway mark. Standalone: nothing here needs the other modules.

## Why this exists

Copilot CLI is the same agent you get in VS Code, running in the place where your build, your git history, and your ssh sessions already are. No context switch, no editor required, and it works over ssh into a box that will never run a GUI.

It's also the foundation the [GitHub Copilot app](copilot-app.md) is built on, so anything you learn here carries over.

Two halves to this module:

1. **Interactive.** Install, sign in, pick a model, drive it with slash commands, keep several sessions going at once. This is the part you'll use every day.
2. **Headless.** The same agent with no terminal attached: `-p`, a token, a shell script, a GitHub Actions workflow. This is the part that turns it into infrastructure.

There's a clearly marked stopping point between them.

## Install

Pick one. They all give you the same `copilot` binary.

=== "npm"

    Needs **Node.js 22 or later**.

    ```bash
    npm install -g @github/copilot
    ```

    If you have `ignore-scripts=true` in your `~/.npmrc`, the install needs a nudge:

    ```bash
    npm_config_ignore_scripts=false npm install -g @github/copilot
    ```

=== "Homebrew"

    macOS and Linux.

    ```bash
    brew install --cask copilot-cli
    ```

    The `--cask` matters. `brew install copilot-cli` without it doesn't resolve.

=== "WinGet"

    Windows. You'll also want **PowerShell 6 or later**.

    ```powershell
    winget install GitHub.Copilot
    ```

=== "Install script"

    macOS and Linux, no Node required.

    ```bash
    curl -fsSL https://gh.io/copilot-install | bash
    ```

    Pipe to `sudo bash` instead to install into `/usr/local/bin`. `PREFIX` and `VERSION` are both honoured if you need to pin.

Confirm it landed:

```bash
copilot --version
```

Updates are automatic by default, except in CI. To do it by hand, `copilot update`. To turn the automatic check off, `--no-auto-update` or `COPILOT_AUTO_UPDATE=false`.

You also need an active Copilot subscription. If your seat comes from an organization or enterprise, an admin can disable the CLI for you, in which case none of this will work until they don't.

## Sign in

```bash
copilot login
```

On a local desktop that opens your browser and catches the result on a loopback callback. Over ssh, in a container, in Codespaces, or in CI, it falls back to the OAuth device code flow instead: you get a one-time code to paste at `github.com/login/device`.

Force either one if the detection guesses wrong:

```bash
copilot login --web-flow
copilot login --device-code
```

For GitHub Enterprise Cloud with data residency:

```bash
copilot login --host https://example.ghe.com
```

The token goes into your OS keychain under the service name `copilot-cli`. If there's no keychain available, the CLI asks before falling back to a plaintext file at `~/.copilot/config.json`.

`/logout` deletes the local copy. It does not revoke the token on GitHub.

## Your first session

Work from the **repo root**, the folder holding both `docs/` and `library-api/`. Copilot discovers `.github/copilot-instructions.md`, `.github/skills/`, and `.github/agents/` relative to the repo root, so opening `library-api/` on its own quietly hides all of it.

```bash
cd github-copilot-101
copilot
```

Ask something real:

```text
What does BookController do?
```

If you get back actual method names, you're in business.

### Things that aren't slash commands

The prompt box understands five prefixes that people miss for months:

| Prefix | Does |
| --- | --- |
| `@` | Pull a file into context. `@library-api/pom.xml` |
| `#` | Pull in a GitHub issue or PR by number. `#12` |
| `!` | Run a shell command directly, without asking the model. `!git status` |
| `$` | Hand the terminal to a real interactive shell, then come back |
| `?` | Quick help |

`!` is the one worth internalising. When you want `git status`, you want `git status`, not a model's summary of `git status`.

## Choosing a model

Run `/model` to open the picker. It shows every model you have access to, plus its relative cost, so you can weigh the decision before you make it.

Five ways to set it, and they resolve in this order, highest first:

1. A custom agent's own `model`, if you're running one
2. `--model` on the command line
3. The `COPILOT_MODEL` environment variable
4. The `model` key in `~/.copilot/settings.json`
5. The CLI's built-in default

```bash
copilot --model gpt-5.4
```

`auto` is a real value, and a good one: it picks per task, and paid plans get a discount on model costs while using it.

```bash
copilot --model auto
```

`/model` also decides *where* your choice sticks:

- `/model --session` (the default) is this session only
- `/model --repo` or `--local` pins it for this repository
- `/model --global` sets your future default

Reasoning effort is separate, via `--effort` or the picker. More effort costs more and takes longer.

!!! tip "Don't memorise model names"
    The available list changes, and the docs and the README have contradicted each other on which model is the default. Run `/model` and read what your install actually offers.

### Watching the bill

Usage is measured in **AI credits**. On the legacy billing platform you'll see premium requests instead.

| Where | Shows |
| --- | --- |
| The footer | Remaining budget, continuously |
| `/usage` | Credits used this session, token breakdown, limit progress |
| `/context` | Context window usage, and which parts of the prompt are eating it |
| `/model` | Per-token cost of each model, before you switch |
| `/exit` | A summary of what the session cost |

You can put a soft ceiling on a session:

```text
/limits set max-ai-credits 5
```

Or from the command line, `--max-ai-credits 5`. It's a soft cap, so treat it as a smoke alarm rather than a fuse.

**Exercise.** Ask the same thing on two different models and compare:

```text
Why does GET /members/999 return 500 instead of 404? Answer in three sentences.
```

Run it, `/model` to something cheaper, `/clear`, run it again. Then `/usage`.

## Slash commands

There are around ninety. `/help` prints the authoritative list for your installed version, which is the only list that's ever fully correct. These are the ones that earn their keep.

### Setting up the environment

| Command | Does |
| --- | --- |
| `/init` | Generate a starting `.github/copilot-instructions.md` for this repo |
| `/env` | Show everything currently loaded: instructions, MCP servers, skills, agents, hooks, plugins, LSPs, extensions |
| `/instructions` | List instruction files, and toggle individual ones off |
| `/skills` | Skills dashboard. `/skills list`, `/skills info NAME`, `/skills reload` |
| `/agent` | Browse and pick a custom agent |
| `/mcp` | Add, edit, list, and authenticate MCP servers |
| `/plugin` | Install plugins and manage marketplaces |

`/env` is the first thing to run when Copilot is ignoring a file you swear you wrote.

### Working on code

| Command | Does |
| --- | --- |
| `/plan` | Produce an implementation plan and wait for you to approve it |
| `/diff` | Review the changes made in the working directory |
| `/review` | Run the code review agent over your changes |
| `/security-review` | Analyse staged and unstaged changes for vulnerabilities |
| `/rubber-duck` | An independent critique from a second agent, usually on a different model |
| `/pr` | View, create, or fix the PR for the current branch |
| `/ide` | Connect to an open VS Code workspace |

### Managing the session

| Command | Does |
| --- | --- |
| `/resume` | Switch to another session, by ID or name |
| `/rename` | Name the current session so you can find it later |
| `/fork` | Branch the session, leaving the original intact |
| `/compact` | Summarize history to reclaim context window. Takes focus instructions |
| `/rewind` | Undo the last turn, reverting file changes with it |
| `/share` | Export the session to markdown, HTML, a gist, or a link |
| `/clear` | Abandon this session and start clean |

`/rewind` is the safety net. It reverts the files too, not just the conversation, which makes it very different from `/clear`.

### Changing how it works

| Command | Does |
| --- | --- |
| `/autopilot` | Let it run without stopping for approval on each step |
| `/fleet` | Fan the work out across parallel subagents |
| `/delegate` | Push the work to GitHub and have Copilot open a PR |
| `/permissions` | Switch permission modes |

**Exercise.** Plan a fix, then throw it away:

```text
/plan Fix GET /members/999 so it returns 404 instead of 500
```

Read the plan, approve it, let it edit. Then:

```text
/diff
```

Then put the repo back:

```text
/rewind
```

## Permissions

By default the CLI asks before it does anything consequential. That's the right default and you should keep it. When you get tired of approving the same command forty times, narrow the rule rather than removing it.

Rules are patterns of the form `kind(argument)`:

| Pattern | Matches |
| --- | --- |
| `shell(git:*)` | Any `git` subcommand. Matches `git push`, not `gitea` |
| `shell(git push)` | Exactly `git push` |
| `write` | Any file creation or modification |
| `write(.env)` | A file named `.env` in any directory. Use an absolute path to pin it to one |
| `url(github.com)` | HTTPS requests to github.com. Protocol-aware, so this does not allow `http://` |
| `MyMCP(create_issue)` | One tool from one MCP server |

```bash
copilot --allow-tool='shell(git:*)' --deny-tool='shell(git push)'
```

**Deny always wins**, including over `--allow-all`. That combination above is the useful shape: let it use git freely, never let it push.

There's a second, separate axis that people conflate with this one:

- `--available-tools` and `--excluded-tools` control what the model can **see**
- `--allow-tool` and `--deny-tool` control what it can do **without asking**

Excluding a tool removes it from the menu. Denying it leaves it visible and refuses it. Different failure modes, different debugging.

GitHub's [allowing and denying tool use guide](https://docs.github.com/en/copilot/how-tos/copilot-cli/use-copilot-cli/allowing-tools)
documents both layers, including saved approvals and deny-rule precedence. Read it before
expanding the permissions for the headless exercises.

In-session: `/permissions` to switch modes, `/add-dir` to grant access to another directory, `/list-dirs` to see what's granted, `/reset-allowed-tools` to start over.

!!! warning "`--yolo` and `--allow-all`"
    `--allow-all` (and its alias `--yolo`) turns off confirmation for tools, paths, and URLs all at once. It's genuinely useful in a sandbox or CI container, and it's exactly what a prompt injection in a README, an issue comment, or a dependency's docs would want you to have enabled. Never point it at content you didn't write, and never in a directory holding credentials you'd mind losing.

## Running sessions side by side

You are not limited to one conversation. This is the CLI's best feature and the least advertised.

```bash
# Name it now so you can find it in an hour
copilot --name "loan renewal"

# Resume the most recent session in this directory
copilot --continue

# Pick from a list
copilot --resume

# Jump straight to one, by ID, ID prefix, or exact name
copilot --resume="loan renewal"
```

Inside a session, the sidebar and the Sessions tab (++tab++, or `/session`) both let you move between live sessions without leaving the CLI.

### Working across directories

```bash
# Start somewhere else
copilot -C ~/code/other-project

# Grant access to a second directory
copilot --add-dir ~/code/shared-lib --add-dir /tmp
```

`--add-dir` does more than unlock file reads. It also loads that directory's `.github/skills` and `.github/agents` as trusted configuration, which is how you share customization across repos without copying files.

`/cwd` changes the working directory mid-session. `/list-dirs` shows what you've allowed.

### Alongside an editor

`/ide` connects the session to an open VS Code workspace, so the CLI knows what file you're looking at.

If you want genuinely parallel work on the same repo, git worktrees are the answer, and the CLI has `/worktree` and `-w` for it. Both are **experimental**, so you need `--experimental` or `/experimental on` first, and both are subject to change.

**Exercise.** Two sessions, one repo:

```bash
# Terminal 1
copilot --name "reviewer" -C ~/code/github-copilot-101

# Terminal 2
copilot --name "builder" -C ~/code/github-copilot-101
```

Have `builder` add validation to `MemberController`. Have `reviewer` critique what it did. Two contexts, no cross-contamination, and you decide what survives.

---

## Stop here if you want

That's the CLI as a daily tool. Install, sign in, pick a model, drive it, keep a few sessions going, keep permissions tight.

Everything below is about running the same agent with nobody watching: in a script, in a git hook, in CI. Useful, but a different job with a different risk profile.

---

## Headless

Same agent, no terminal, exits when it's done.

### The two ways in

```bash
copilot -p "Explain library-api/src/main/java/com/example/library/loan/LoanController.java"
```

```bash
echo "Explain LoanController" | copilot
```

If you supply both, `-p` wins and the piped input is ignored.

The flags that make it scriptable:

| Flag | Does |
| --- | --- |
| `-p`, `--prompt` | Run one prompt and exit |
| `-s`, `--silent` | Output only the response, no stats or decoration |
| `--no-ask-user` | Disable the "ask the human a question" tool, so it can't block |
| `--allow-tool` | Grant exactly the permissions the job needs |
| `--allow-all-tools` | Grant everything. Required for fully non-interactive runs |
| `--output-format json` | JSONL, one object per line |
| `--share PATH` | Write a markdown transcript when it finishes |

**Exercise.** Three shapes worth knowing, run from the repo root.

Straight question:

```bash
copilot -p "How many REST endpoints does library-api expose? Give the number only." -s
```

Capture into a variable:

```bash
version=$(copilot -p 'What Java version does library-api/pom.xml target?
  Give the number only, no other text.' -s)
echo "Targets Java $version"
```

Branch on the answer:

```bash
if copilot -p 'Does library-api have any test that asserts a 404 response? Reply only YES or NO.' -s \
  | grep -qi '^no'; then
  echo "No 404 coverage."
fi
```

!!! warning "Assert on output, not on exit status"
    Copilot CLI does not publish an exit-code contract. Nothing in the docs defines what a non-zero exit means, so don't build a pipeline that depends on one. Ask for a constrained answer and match the text, like the `grep -qi` above.

`--output-format json` emits JSONL and is genuinely useful for logging, but the object schema isn't documented either. Store it, don't parse it into a decision yet.

### A token for automation

Interactive `/login` is useless in CI. You need a token in the environment, and this is where most people get stuck, because the obvious answer is wrong.

**Classic personal access tokens (`ghp_`) do not work.** They are explicitly not supported.

What works:

| Token | Prefix | Notes |
| --- | --- | --- |
| OAuth token from `copilot login` | `gho_` | The default interactive path |
| Fine-grained PAT | `github_pat_` | Must be owned by your **personal account**, with the **Copilot Requests** permission |
| GitHub App user-to-server token | `ghu_` | Via environment variable |
| Classic PAT | `ghp_` | **Not supported** |

To create the fine-grained one:

1. Go to [Fine-grained personal access tokens](https://github.com/settings/personal-access-tokens/new).
2. Under **Resource owner**, choose your **personal account**. Not an organization. The Copilot Requests permission only exists on user-owned tokens, and this is the step everyone gets wrong.
3. Under **Repository access**, grant the minimum the job needs. For a workflow that only reads this repo, that's this repo.
4. Under **Permissions**, open the **Account** tab.
5. Click **Add permissions** and select **Copilot Requests**.
6. **Generate token**, and copy it. You won't see it again.

Then export it. The CLI checks three variables, in this order:

```bash
export COPILOT_GITHUB_TOKEN=github_pat_...
# then GH_TOKEN
# then GITHUB_TOKEN
```

After those three it falls back to the system keychain, and after that to `gh auth token`.

!!! warning "An environment token silently overrides your login"
    If you have `GH_TOKEN` exported for some other tool, Copilot CLI uses that token instead of the one from `copilot login`. No warning, no prompt. If the CLI is suddenly acting as the wrong account, or claiming you have no Copilot access when you obviously do, check your environment before you check anything else.

If you'd rather not put the token in the environment at all:

```bash
copilot login --with-token < token.txt
```

### A script worth keeping

Save this as `daily-review.sh`. It summarizes today's commits and flags anything that changed a controller without touching a test.

```bash title="daily-review.sh"
#!/usr/bin/env bash
set -uo pipefail

SINCE="${1:-midnight}"

copilot -p "Look at the git log for commits since ${SINCE} on this repository.

Write a short report in this exact shape:

## Commits
One bullet per commit: the subject line, the author, and the short SHA.

## Needs attention
List any commit that changed a file under library-api/src/main/java and did NOT
change a file under library-api/src/test/java. For each, name the commit and the
production files it touched. If there are none, write 'Nothing flagged.'

Do not modify any files. Do not run any command other than git." \
  --allow-tool='shell(git:*)' \
  --no-ask-user \
  -s
```

Note what it is and isn't allowed to do: `git` only, no writes, no questions, response only. That's the shape to copy. Broad permissions in an unattended script are how a "summarize my commits" job turns into a force push.

```bash
chmod +x daily-review.sh
./daily-review.sh "3 days ago"
```

The finished copy is in [`solutions/copilot-cli/`](https://github.com/sabajamalian/github-copilot-101/tree/main/solutions/copilot-cli).

### In GitHub Actions

In Actions you don't need a PAT at all. The built-in `GITHUB_TOKEN` works, as long as you ask for the right permission.

The Copilot-specific setup is documented in [using Copilot CLI with `GITHUB_TOKEN`](https://docs.github.com/en/copilot/how-tos/copilot-cli/use-copilot-cli-in-actions).
GitHub's [workflow authentication guide](https://docs.github.com/en/actions/tutorials/authenticate-with-github_token)
explains how workflow-level and job-level `permissions` restrict that token.

```yaml title=".github/workflows/copilot-summary.yml"
name: Copilot summary

on:
  workflow_dispatch:

permissions:
  contents: read
  copilot-requests: write

jobs:
  summarize:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v5
        with:
          fetch-depth: 0

      - uses: actions/setup-node@v4
        with:
          node-version: 22

      - run: npm install -g @github/copilot

      - name: Summarize recent changes
        env:
          GITHUB_TOKEN: ${{ github.token }}
        run: |
          copilot -p "Summarize the last 20 commits to library-api as a bullet list.
          Write the result to summary.md." \
            --allow-tool='shell(git:*)' \
            --allow-tool='write' \
            --no-ask-user \
            -s

      - run: cat summary.md >> "$GITHUB_STEP_SUMMARY"
```

Three things are doing real work there:

- **`copilot-requests: write`** is the workflow permission that lets `GITHUB_TOKEN` make Copilot requests. Without it the job fails on auth, and the error won't say "you forgot a permission".
- **`--no-ask-user`** stops the agent from blocking on a question nobody can answer.
- **Scoped `--allow-tool` rules** rather than `--yolo`. Official examples use `--yolo` for brevity; prefer the narrow version once you know which tools the job needs.

If you'd rather bill the run to a specific person's seat, use a fine-grained PAT in a secret instead:

```yaml
env:
  COPILOT_GITHUB_TOKEN: ${{ secrets.COPILOT_TOKEN }}
```

Create `COPILOT_TOKEN` using GitHub's [Actions secrets guide](https://docs.github.com/en/actions/how-tos/write-workflows/choose-what-workflows-do/use-secrets).
Store the token as a secret, never in the workflow file or a committed shell script.

Which you pick changes who pays. `GITHUB_TOKEN` in an organization repo meters to the organization; a PAT meters to that user's seat. If your seat comes from an org, an admin also has to have **Allow use of Copilot CLI billed to the organization** turned on.

For anything more elaborate than a single step, GitHub points at [GitHub Agentic Workflows](https://github.com/github/gh-aw) rather than hand-rolling `copilot` invocations.

### Safety when nobody's watching

Use GitHub's [Actions secure use reference](https://docs.github.com/en/actions/reference/security/secure-use)
for least-privilege credentials, untrusted input, and action pinning. The
[Copilot Agents application card](https://docs.github.com/en/copilot/responsible-use/agents)
explains agent limitations and why generated changes still need review.

- **Never run `--yolo` against content you didn't write.** Issue bodies, PR descriptions, dependency READMEs, and web fetches are all attacker-controllable in the general case.
- **`--secret-env-vars`** strips named variables out of shell and MCP environments, and redacts them from output. Use it for anything sensitive that has to be in scope.
- **Deny rules survive `--allow-all`.** A standing `--deny-tool='shell(git push)'` is cheap insurance.
- **Prompt mode is deliberately less trusting.** Project MCP servers, repo hooks, and extensions don't load under `-p` unless the directory is already trusted. If a headless run behaves differently from your interactive one, that's usually why.

## Gotchas

- **npm installs need Node.js 22 or later.** Older Node fails in ways that don't mention Node.
- **Homebrew needs `--cask`.** `brew install copilot-cli` doesn't resolve. The GitHub README is out of date on this.
- **Classic PATs are rejected.** Fine-grained only, user-owned, **Copilot Requests** permission under the **Account** tab.
- **An exported `GH_TOKEN` silently beats your `/login` session.** First thing to check when auth acts strange.
- **Editing an instructions or skills file doesn't affect a live session.** `/skills reload` picks up skills; instructions and agents need `/new` or a restart.
- **Bare `--resume` needs a terminal.** Under `-p` or piped stdin it errors rather than silently starting fresh, which is the correct behaviour but surprises people.
- **Experimental features need opting in.** `/worktree`, `/move`, `-w`, `/sandbox`, `/every`, `/after` all require `--experimental`.
- **Config lives in `~/.copilot`, not an XDG path.** Override with `COPILOT_HOME`. `--config-dir` is deprecated.
- **`--log-level` wants `warning`, not `warn`.** It also accepts `all` and `default`.
- **`/reset` is an alias for `/clear`**, not a permissions reset. That's `/reset-allowed-tools`.
- **Exit codes aren't a documented contract.** Don't gate CI on them.

## Official docs

| Link | What it's good for |
| --- | --- |
| [About Copilot CLI](https://docs.github.com/en/copilot/concepts/agents/copilot-cli/about-copilot-cli) | The concept, supported platforms, known limitations |
| [Quickstart](https://docs.github.com/en/copilot/get-started/cli-quickstart) | The five-minute version |
| [Install Copilot CLI](https://docs.github.com/en/copilot/how-tos/copilot-cli/set-up-copilot-cli/install-copilot-cli) | Every install method and its prerequisites |
| [Authenticate Copilot CLI](https://docs.github.com/en/copilot/how-tos/copilot-cli/set-up-copilot-cli/authenticate-copilot-cli) | Token types, the Copilot Requests permission, full precedence order |
| [CLI command reference](https://docs.github.com/en/copilot/reference/copilot-cli-reference/cli-command-reference) | Every flag, every slash command, every environment variable |
| [Programmatic reference](https://docs.github.com/en/copilot/reference/copilot-cli-reference/cli-programmatic-reference) | `-p` mode specifics and model precedence |
| [Run the CLI programmatically](https://docs.github.com/en/copilot/how-tos/copilot-cli/automate-copilot-cli/run-cli-programmatically) | Official scripting examples |
| [Use Copilot CLI in Actions](https://docs.github.com/en/copilot/how-tos/copilot-cli/use-copilot-cli-in-actions) | The `copilot-requests: write` workflow pattern |
| [Copilot CLI in GitHub Actions](https://docs.github.com/en/copilot/concepts/agents/copilot-cli/copilot-cli-in-github-actions) | Who gets billed for what |
| [Allowing tools](https://docs.github.com/en/copilot/how-tos/copilot-cli/use-copilot-cli/allowing-tools) | Permission pattern syntax in full |
| [Work with multiple sessions](https://docs.github.com/en/copilot/how-tos/copilot-cli/use-copilot-cli/work-with-multiple-sessions) | The sidebar, the Sessions tab, resume semantics |
| [Config directory reference](https://docs.github.com/en/copilot/reference/copilot-cli-reference/cli-config-dir-reference) | What lives in `~/.copilot` and what you may edit |
| [Supported models](https://docs.github.com/en/copilot/reference/ai-models/supported-models) | Which models reach which surface |
| [github/copilot-cli](https://github.com/github/copilot-cli) | Releases, issues, discussions |
| [GitHub Actions: workflow authentication](https://docs.github.com/en/actions/tutorials/authenticate-with-github_token) | Pass `GITHUB_TOKEN` and restrict its permissions |
| [GitHub Actions: using secrets](https://docs.github.com/en/actions/how-tos/write-workflows/choose-what-workflows-do/use-secrets) | Store a PAT for the optional user-billed workflow |
| [GitHub Actions: secure use reference](https://docs.github.com/en/actions/reference/security/secure-use) | Untrusted input, secret handling, and dependency pinning |
| [GitHub: Copilot Agents application card](https://docs.github.com/en/copilot/responsible-use/agents) | CLI capabilities, risks, and review responsibilities |

## Next

Nothing depends on this module, so go anywhere:

- [GitHub Copilot app](copilot-app.md), the same agent with a UI
- [Custom instructions](custom-instructions.md)
- [Agent skills](agent-skills.md)
- [Custom agents](custom-agents.md)

Finished files are in [`solutions/copilot-cli/`](https://github.com/sabajamalian/github-copilot-101/tree/main/solutions/copilot-cli).

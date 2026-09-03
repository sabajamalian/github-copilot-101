# GitHub Copilot app

**~40 minutes.** Standalone: nothing here needs the other modules.

GitHub's official name for the desktop application is the **GitHub Copilot app**. It's currently a **technical preview**, so expect the UI to move around between releases. Everything below was checked against the current docs, and anything that's only in the release notes rather than the documentation is marked as such.

## Why this exists

The app is [Copilot CLI](copilot-cli.md) with a UI wrapped around the parts that are awkward in a terminal: several agents working in parallel, a real diff view, pull requests, and side-panel artifacts you can edit while the agent is still working on them.

That's not a marketing framing, it's the actual architecture. The docs say the app is built on Copilot CLI, and it shows: your skills, MCP servers, custom agents, and instruction files all work in the app with no changes, and `/chronicle` reads history from both surfaces.

Three things make it worth a separate module:

| | Why it's different here |
| --- | --- |
| **Projects** | Repos and folders the app manages, with per-project setup scripts and instructions |
| **Sessions** | Each one gets its own git worktree and branch, so parallel work stops being a chore |
| **Canvases** | An interactive side panel the agent and you both edit, live |

## Install

Download from the [GitHub Copilot app page](https://github.com/features/ai/github-app), or grab a build directly from [`github/app` releases](https://github.com/github/app/releases).

| Platform | Asset |
| --- | --- |
| macOS (Apple silicon) | `GitHub-Copilot-darwin-arm64.dmg` |
| macOS (Intel) | `GitHub-Copilot-darwin-x64.dmg` |
| Windows (x64) | `GitHub-Copilot-windows-x64-setup.exe` |
| Windows (ARM) | `GitHub-Copilot-windows-arm64-setup.exe` |
| Linux | `GitHub-Copilot-linux-x64.AppImage` |

You need a GitHub account and git installed. You do **not** strictly need a Copilot plan: the app supports bringing your own model provider, in which case you sign in with GitHub but pay your provider instead. With a plan, every tier works.

On first launch:

1. Click **Sign in to GitHub**. For GitHub Enterprise Server, choose **Use GitHub Enterprise** and enter your server address.
2. Pick repositories from your recent GitHub activity, add a local folder, or skip.
3. Choose a theme.

!!! note "If you're on an org or enterprise seat"
    The **GitHub Copilot app policy** has to be enabled. It's on by default, and it's a separate policy from the Copilot CLI one, so having the CLI enabled doesn't tell you anything about the app.

## Projects

A project is a codebase the app knows about. It's either a folder already on your machine, including a repo you cloned yourself, or a repo the app clones for you.

Click **+** next to **Sessions** in the sidebar. Under **Add project from**, you get three options:

| Option | Use when |
| --- | --- |
| **Local folder or repository** | It's already on disk |
| **GitHub repository** | You want to browse GitHub and let the app clone it |
| **Repository URL** | It's on Azure DevOps, or elsewhere, or the app can't see it on GitHub |

**Exercise.** Add this course repo as a project. If you cloned it during [setup](../setup.md), use **Local folder or repository** and point at the repo root, the folder holding both `docs/` and `library-api/`. Otherwise let the app clone `sabajamalian/github-copilot-101` for you.

Once it's added, open the project's **Settings**. You can write repository-specific instructions there, which get appended to the system prompt for every session in that project. If global app instructions also exist, global applies first, then the project's.

Those settings are backed by a file, which is [further down](#repository-configuration).

## Sessions

Every session runs in its own isolated workspace, which is what makes running several at once actually safe rather than merely possible.

When you start one, a dropdown under the prompt box picks **where it runs**:

| Where | What happens |
| --- | --- |
| **In a new working tree** | A dedicated git worktree and branch. The default, and the reason parallel sessions don't collide |
| **In your local repository** | Your normal checkout, on a branch. No worktree |
| **In a cloud sandbox** | A fully isolated Linux environment hosted by GitHub. **Public preview**, and off by default for org-provided seats |

Then you pick a **mode**:

| Mode | Behaviour |
| --- | --- |
| **Plan** | Writes a plan and waits for you to approve it before touching anything |
| **Interactive** | Suggests changes and checks in as it goes |
| **Autopilot** | Writes code, runs tests, and iterates without stopping |

Model and reasoning effort sit next to it, and both can change mid-session. **Auto** picks per task, and once the session is running the picker tells you which model handled each response.

!!! tip "Chats are not sessions"
    The **Chats** section in the sidebar opens a conversation with no branch and no worktree. Use it for "how does this work" questions you don't want a git branch for.

### Exercise A: plan, review, ship

Start a session on the course project, **in a new working tree**, in **Plan** mode:

```text
GET /members/999 returns HTTP 500 instead of 404. Fix it so a missing member returns
404 with a JSON body, and add a test that proves it.
```

Then:

1. Read the plan. Approve it, or send it back with a note.
2. When it finishes, click **Changes** above the prompt box to read the diff.
3. Leave a review comment on a line you don't like, or just tell it what to change.
4. Click **Create PR**. It follows the repository's PR template if there is one.

The worktree means your main checkout never moved. That's the whole point.

### Exercise B: two at once

Start a second session on the same project, also in a new working tree:

```text
BookController and BookService both declare the 14 day loan period. Move it to a single
constant and use it in both places.
```

Both sessions are now editing the same repository on separate branches, in separate directories, with separate conversations. Neither can see the other's uncommitted work.

This is the thing that's genuinely hard to do by hand and trivial here.

### Splitting and delegating

| Command | Does |
| --- | --- |
| `/fork` | Fork the current session at the latest turn |
| `/merge-to-parent` | Merge a forked session's work back into its parent |
| `/spawn [PROMPT]` | Create a focused child session for delegated work |
| `/fleet [PROMPT]` | Launch several agents in parallel on one task |
| `/orchestrate [PROMPT]` | Coordinate work across sessions and repositories by guiding child sessions |

`/fork` is the "try it a different way without losing this" button.

### Housekeeping

Sessions accumulate, and each worktree is real disk. Go to app settings, then **Sessions**, then **Manage sessions**. You can search and filter, select several, and archive or delete them. The list shows disk usage for working files and chat history in separate sortable columns, which makes it obvious what to clear first.

## Canvases

A canvas is a shared, interactive surface for a work artifact: a plan, a triage board, a browser session, a dashboard, a spreadsheet. They open in the app's right side panel.

The part that matters: **canvases are bidirectional**. The agent updates the canvas while it works, and you edit the same surface it's reading. It's a shared workspace, not a rendered output.

There are three participants:

- **You** inspect, steer, edit, and verify
- **The agent** reads canvas state, takes structured actions, and updates the surface
- **The app** connects the canvas to the underlying artifact and enforces which actions are allowed

### Finding them

**Customize** in the sidebar, then **Canvas**. Browse the featured list, install a plugin if one is required, then **New session** to open a session with that canvas available. **Installed** shows everything you already have.

Some canvases arrive inside a plugin alongside skills and MCP servers. Installing the Azure DevOps plugin, for example, brings a canvas for planning and managing Azure DevOps work.

!!! note "Built-in canvases"
    The app ships built-in canvases for the editor, browser, terminal, and Word, Excel, and PowerPoint documents. That list comes from the release notes rather than the documentation reference, so treat it as accurate today and not as a contract.

### Exercise C: build one

In an active session on the course project:

```text
/create-canvas A tracker for the eight deliberate flaws listed in library-api/README.md.
Each row should show the flaw, which files it affects, a status of open, in progress, or
fixed, and a notes field. I want to be able to change status myself, and I want you to be
able to update a row when you fix something.
```

The agent builds it and opens it in the side panel. Then iterate the way you would with any other agent work: "add a severity column", "sort by severity", "mark the missing-validation one as in progress".

Now fix one of the flaws in the same session and watch whether the agent updates the row. That feedback loop is what a canvas is for.

### Where the code lives

Canvas extensions are files, so they can be committed and shared:

| Path | Scope |
| --- | --- |
| `.github/extensions/` | Team-shared, committed to the repository |
| `~/.copilot/extensions/` | Personal, just your machine |

Each extension sits in its own directory and typically has a `package.json` for metadata and dependencies, an entry file such as `extension.mjs` defining behaviour and capabilities, and optionally JSON files holding persisted canvas state. Implementations vary.

!!! warning "There's no published API reference yet"
    GitHub documents `/create-canvas` as the way to build and troubleshoot canvas extensions, and there is a built-in `create-canvas` skill for exactly that. There is no published schema for `extension.mjs` and no list of canvas capability primitives. Build them conversationally for now, and read what the agent writes before you commit it.

## Repository configuration

The app reads `.github/github-app.yml` from a project and uses it to configure sessions. This is how you make a repo clone-and-go for everyone on the team.

```yaml title=".github/github-app.yml"
instructions: |
  This is a Spring Boot 3 / Java 21 REST API. Use Maven, not Gradle.
  Run tests with: mvn -f library-api/pom.xml test

scripts:
  - name: Warm up Maven
    command: mvn -f library-api/pom.xml -q dependency:go-offline
    triggers:
      - session.create

  - name: Run the API
    command: mvn -f library-api/pom.xml spring-boot:run

  - name: Clean up
    command: mvn -f library-api/pom.xml -q clean
    triggers:
      - session.archive

server_ready_pattern: 'Tomcat started on port\(s\): (\d+)'
auto_open_in_browser: true

automation:
  auto_issue_session: true
  remote_control: false
```

### The keys

| Key | Type | Does |
| --- | --- | --- |
| `instructions` | string | Repository guidance appended to the system prompt. Global app instructions apply first, then this |
| `scripts` | list | Commands surfaced in the app UI |
| `scripts[].name` | string | Display name |
| `scripts[].command` | string | What runs |
| `scripts[].triggers` | list | `session.create` and `session.archive`. No triggers means the script is manual |
| `server_ready_pattern` | regex | Detects that a run script has started a server |
| `auto_open_in_browser` | bool | Open the detected URL in the integrated browser. Defaults to true if omitted |
| `automation.auto_issue_session` | bool | Automatically start a session with issue context. Defaults to true if omitted |
| `automation.remote_control` | bool | Allow reaching sessions from github.com and GitHub Mobile. Defaults to false |

`server_ready_pattern` uses Rust regex syntax and the app reads **capture group 1**. A full URL is used as-is; a bare port becomes `http://localhost:PORT`. The example above catches Spring Boot's `Tomcat started on port(s): 8080` line and turns `8080` into a URL. An invalid pattern silently falls back to the app's default detection.

`session.create` and `session.archive` are the canonical trigger names. `workspace.create` and `workspace.archive` still parse, but the `COPILOT_SCRIPT_TRIGGER` variable your script receives always holds the canonical name.

### Scripts get real credentials

Triggered and manual scripts run with environment variables including `GH_TOKEN`, `GH_HOST`, `COPILOT_WORKSPACE_PATH`, `COPILOT_ROOT_PATH`, `COPILOT_DEFAULT_BRANCH`, and per-account tokens. GitHub's own warning is blunt about the consequence: never configure a script to log or persist these environment variables.

!!! warning "The app won't trust the file until you say so"
    The app does not apply instructions, scripts, or settings from `github-app.yml` until you review and accept the configuration. Changes made through the app UI are trusted automatically and written back to the file. Changes made outside the app, **including to whitespace or comments**, require you to accept it again. Until you do, the app keeps using the previously accepted settings, which looks exactly like the file being ignored.

**Exercise.** Create the config above at `.github/github-app.yml` in your clone, then accept it in the app. Start a new session on the project and watch the warm-up run. Then use the **Run the API** script and see whether the integrated browser opens on `localhost:8080`.

A ready-made copy is in [`solutions/copilot-app/`](https://github.com/sabajamalian/github-copilot-101/tree/main/solutions/copilot-app).

## Customize

**Customize** in the sidebar is where the app's extensibility lives, and most of it is shared with the CLI rather than app-specific:

| Section | Notes |
| --- | --- |
| **Skills** | Skills configured for your repositories or Copilot CLI are automatically available |
| **MCP** | Same. Browse featured servers, or add your own. Install and manage from **Installed** |
| **Plugins** | Installable bundles of agents, skills, hooks, MCP configs. Default marketplaces are `github/copilot-plugins` and `github/awesome-copilot` |
| **Canvas** | Covered above |

Custom agents show up in the agent picker in the prompt box, or via `/agent`.

!!! tip "If you did the other modules, they're already here"
    Open the course project in the app and check **Customize → Skills** and `/agent`. Anything you built in the [agent skills](agent-skills.md) or [custom agents](custom-agents.md) modules is loaded, because both surfaces read the same `.github/` files. You didn't have to do anything.

There are also built-in skills you get for free: `af` (agent finder), `agent-merge`, `create-canvas`, `orchestrate`, `pr-stack`, and `customize-cloud-agent`.

## The rest of the surface

Worth knowing about, not worth a full exercise each.

**My work.** A sidebar view of your issues and PRs across connected repos, with **All**, **Active**, **Review requests**, and **Done** sections. Sections are editable, and search supports qualifiers like `label:bug`.

**Pull requests.** Open a PR's **Files changed** tab to review the diff, leave review comments inline, or just ask the agent to make the change. Then submit from the PR detail view. `/pr-open`, `/pr-merge`, and `/pr-fix-checks` cover the common actions.

**Agent merge.** Turn it on and the session reads your PR, fixes what's blocking it, and merges as soon as GitHub allows. It runs in the background, survives app restarts, and switches itself off once the PR merges.

**Automations.** Scheduled prompts, either **local** (your machine) or **cloud** (runs even when your computer is off). Triggers include manual, hourly, daily, weekly, CRON for local ones, and issue or pull request events with optional filters.

**Deep links.** `ghapp://` URLs open the app from anywhere: `ghapp://github.com/OWNER/REPO` opens or clones a repo, `ghapp://session/new?repo=OWNER%2FREPO&mode=plan&prompt=...` starts a session preloaded, `ghapp://mywork` and `ghapp://automations` open those views.

**Second opinions.** `/rubber-duck` gets a critique from an agent running on a *different* model than your session. `/security-review` (public preview) analyses in-progress changes and returns findings with severity and confidence scores.

**Voice dictation.** App settings, then **Voice dictation**. Downloads a local transcription model and inserts text into the prompt box for you to review before sending.

**`/chronicle`.** Session history and insights across both the app and your Copilot CLI sessions, since they share a store.

## Gotchas

- **It's a technical preview.** Menu labels and layout move between releases. When this page and the app disagree, the app is right.
- **Org and enterprise seats need the app policy enabled.** It's separate from the Copilot CLI policy, and having one on says nothing about the other.
- **Cloud sandboxes are off by default for org-provided seats.** That's the **Cloud Sandbox access** policy, and it's billed by compute time.
- **`automation.remote_control: true` isn't sufficient on its own.** If your seat comes from an organization, the "Store local sessions in the Cloud" policy has to be set to **View and control**, and enterprise-managed settings can restrict it further.
- **Editing `github-app.yml` outside the app requires re-accepting it.** Including whitespace-only edits. The symptom is the file appearing to have no effect.
- **Scripts receive credentials in their environment.** Don't log or persist them.
- **Worktree sessions mean your changes aren't in your main checkout.** Obvious once you know, confusing the first time you go looking for a file the agent said it wrote.
- **Copilot Memory isn't documented for the app.** It's documented for the cloud agent, code review, and Copilot CLI. Don't assume it carries over.
- **The app may produce code matching public code** even when the "Suggestions matching public code" policy is set to Block. That's documented behaviour, not a bug.

## Official docs

| Link | What it's good for |
| --- | --- |
| [About the GitHub Copilot app](https://docs.github.com/en/copilot/concepts/agents/github-copilot-app) | The concept, supported platforms, plan and policy requirements |
| [Quickstart](https://docs.github.com/en/copilot/get-started/quickstart-copilot-app) | Install, sign in, add a project, first session |
| [Agent sessions](https://docs.github.com/en/copilot/how-tos/github-copilot-app/agent-sessions) | Session types, modes, model picker, managing sessions |
| [Working with canvas extensions](https://docs.github.com/en/copilot/how-tos/github-copilot-app/working-with-canvas-extensions) | What canvases are, `/create-canvas`, where extensions live |
| [Repository configuration](https://docs.github.com/en/copilot/reference/github-copilot-app-reference/repository-configuration) | The full `github-app.yml` schema, trust model, script env vars |
| [Slash commands](https://docs.github.com/en/copilot/reference/github-copilot-app-reference/slash-commands) | Every command the app supports |
| [Built-in skills](https://docs.github.com/en/copilot/reference/github-copilot-app-reference/built-in-skills) | What ships already |
| [Customize the app](https://docs.github.com/en/copilot/how-tos/github-copilot-app/customize-github-copilot-app) | Skills, MCP, plugins, agents, instructions |
| [Managing issues and pull requests](https://docs.github.com/en/copilot/how-tos/github-copilot-app/managing-issues-and-pull-requests) | My work, PR review, agent merge |
| [Using automations](https://docs.github.com/en/copilot/how-tos/github-copilot-app/using-automations) | Local vs cloud automations and their triggers |
| [Open with deep links](https://docs.github.com/en/copilot/how-tos/github-copilot-app/open-with-deep-links) | Every `ghapp://` URL shape |
| [BYOK models](https://docs.github.com/en/copilot/how-tos/github-copilot-app/use-byok-models) | Using your own provider instead of a Copilot plan |
| [Cloud and local sandboxes](https://docs.github.com/en/copilot/concepts/about-cloud-and-local-sandboxes) | What a cloud sandbox is and how it's billed |
| [About plugins](https://docs.github.com/en/copilot/concepts/agents/about-plugins) | The plugin format and marketplaces |
| [Supported surfaces for policies](https://docs.github.com/en/copilot/reference/supported-surfaces-for-policies) | Admins: which policy affects which surface |
| [github/app](https://github.com/github/app) | Downloads, release notes, issues, discussions |

## Next

Nothing depends on this module:

- [Copilot CLI](copilot-cli.md), which is what the app runs on
- [Custom instructions](custom-instructions.md)
- [Agent skills](agent-skills.md)
- [Custom agents](custom-agents.md)

Finished files are in [`solutions/copilot-app/`](https://github.com/sabajamalian/github-copilot-101/tree/main/solutions/copilot-app).

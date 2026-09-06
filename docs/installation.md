# Installation

Copilot isn't one program. It's one subscription behind several **harnesses**: the VS Code experience, the JetBrains plugin, the Eclipse plugin, Copilot CLI, the GitHub Copilot app, and Copilot on GitHub.com. Same account, same models, different capabilities.

This page installs the ones you'd realistically use for Java work. Pick one and move on. You do not need all of them for the labs.

!!! note "Which one do the labs need?"
    Any of **VS Code** or **Copilot CLI**. Both are fully supported everywhere on this site, and every customization lab step has a tab for each. IntelliJ IDEA and Eclipse are covered below because that's what most Java teams actually open, but some customization features are still in preview there. The [comparison table](#feature-comparison) says exactly which.

    The [Copilot CLI](modules/copilot-cli.md) and [GitHub Copilot app](modules/copilot-app.md) modules install their own tool as part of the lab, so you don't need either one before you start.

## Before anything else

You need access to Copilot: either the free tier or a paid plan on your GitHub account. See [What is GitHub Copilot?](https://docs.github.com/en/copilot/get-started/what-is-github-copilot#get-access).

If your Copilot access comes through an organization or enterprise, an admin can disable individual harnesses. Copilot CLI in particular is a policy your org can switch off. See [Managing policies and features for Copilot in your organization](https://docs.github.com/en/copilot/how-tos/administer-copilot/manage-for-organization/manage-policies).

## Install a harness

=== "VS Code"

    Nothing to install by hand. The Copilot extensions ship with VS Code and install themselves the first time you turn AI features on.

    1. Install [Visual Studio Code](https://code.visualstudio.com/) if you don't have it.
    2. Hover the Copilot icon in the Status Bar and choose **Use AI Features**.
    3. Pick a sign-in method and follow the prompts. If your account already has a Copilot plan, VS Code uses it. If not, you're put on Copilot Free.
    4. Open Chat with ++cmd+ctrl+i++ on macOS or ++ctrl+alt+i++ on Windows and Linux, and switch the mode picker to **Agent**.

    Full steps: [Set up GitHub Copilot in VS Code](https://code.visualstudio.com/docs/copilot/setup).

    **For the Java app in this repo**, also install the [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) and the [Spring Boot Extension Pack](https://marketplace.visualstudio.com/items?itemName=vmware.vscode-boot-dev-pack). Copilot works without them, but you won't get Java language support, so no jump-to-definition and no test running. See the [VS Code Java tutorial](https://code.visualstudio.com/docs/java/java-tutorial).

    For this repo's build and tests, Microsoft's [Java build tools guide](https://code.visualstudio.com/docs/java/java-build)
    covers Maven project discovery and dependency management. The [Spring Boot guide](https://code.visualstudio.com/docs/java/java-spring-boot)
    and [Java testing guide](https://code.visualstudio.com/docs/java/java-testing) cover running the API and debugging tests.

    !!! tip "On GHE.com?"
        Managed user accounts need a setting change before sign-in works. See [Authenticate to GHE.com](https://docs.github.com/en/copilot/how-tos/configure-personal-settings/authenticate-to-ghecom?tool=vscode#authenticating-from-vs-code).

=== "IntelliJ IDEA"

    The plugin is a Marketplace install, not a bundled component.

    1. Open a compatible JetBrains IDE. Copilot supports IntelliJ IDEA (Ultimate, Community, Educational), Android Studio, CLion, DataGrip, DataSpell, GoLand, MPS, PhpStorm, PyCharm, Rider, RubyMine, RustRover, WebStorm, JetBrains Client, and Code With Me Guest.
    2. Go to **Settings → Plugins → Marketplace**, search for **GitHub Copilot**, click **Install**.
    3. Click **Restart IDE**.
    4. After the restart, go to **Tools → GitHub Copilot → Login to GitHub**.
    5. In the "Sign in to GitHub" dialog, click **Copy and Open**. A device activation page opens in your browser.
    6. Paste the device code, click **Continue**, then **Authorize GitHub Copilot Plugin**.
    7. Back in the IDE, click **OK**.

    Full steps and version compatibility: [Installing the Copilot extension in your environment](https://docs.github.com/en/copilot/how-tos/set-up/install-copilot-extension?tool=jetbrains) and [GitHub Copilot plugin versions](https://plugins.jetbrains.com/plugin/17718-github-copilot/versions).

    **Two other ways in**, both without the plugin:

    - **JetBrains AI Assistant.** If you have a valid Copilot subscription, Copilot shows up in the AI Assistant agent picker automatically over the [Agent Client Protocol](https://agentclientprotocol.com/get-started/introduction). Chat and agent only: no code completion, no next edit suggestions, no inline chat, no code review, no commit message generation.
    - **Copilot CLI in the integrated terminal.** Install it as below and run it in the IDE terminal.

    GitHub's own comparison of the three: [Using GitHub Copilot in JetBrains IDEs](https://docs.github.com/en/copilot/concepts/agents/copilot-in-jetbrains).

    !!! info "The plugin is moving to Copilot CLI underneath"
        GitHub is transitioning the JetBrains plugin from its own local agent harness to Copilot CLI as the default agent harness, to get closer to feature parity faster. That's why several rows below say *preview* today. See the [announcement](https://devblogs.microsoft.com/java/github-copilot-for-jetbrains-is-moving-to-copilot-cli-as-the-default-agent-harness/).

=== "Eclipse"

    Requires Eclipse **2024-03 or later**.

    1. Install GitHub Copilot from the [Eclipse Marketplace](https://aka.ms/copiloteclipse), or via the Eclipse Update Site. See [Installing New Software](https://help.eclipse.org/latest/topic/org.eclipse.platform.doc.user/tasks/tasks-124.htm) in the Eclipse docs.
    2. Restart Eclipse.
    3. In the bottom right of the workbench, click the **Copilot** icon, then **Sign In to GitHub**.
    4. Click **Copy Code and Open**. A device activation page opens in your browser.
    5. Paste the device code, click **Continue**, then **Authorize GitHub Copilot Plugin**.
    6. Click **OK**.

    Full steps: [Installing the Copilot extension in your environment](https://docs.github.com/en/copilot/how-tos/set-up/install-copilot-extension?tool=eclipse).

    !!! warning "Eclipse is the thinnest harness of the three"
        No agent skills, no prompt files, no Copilot code review, no checkpoints. Custom instructions are in preview. If you're doing the [agent skills module](modules/agent-skills.md), do it in VS Code or Copilot CLI.

=== "Copilot CLI"

    Runs in any terminal, including the one embedded in IntelliJ IDEA or Eclipse. That makes it the practical way to get agent skills and hooks alongside a Java IDE that doesn't support them yet.

    ```bash
    # npm, all platforms. Needs Node.js 22+
    npm install -g @github/copilot

    # Homebrew, macOS and Linux
    brew install --cask copilot-cli

    # WinGet, Windows. Needs PowerShell 6+
    winget install GitHub.Copilot

    # Install script, macOS and Linux
    curl -fsSL https://gh.io/copilot-install | bash
    ```

    Then start it and authenticate:

    ```bash
    copilot
    ```

    On first launch, run `/login` and follow the prompts. You can also authenticate non-interactively with a fine-grained personal access token carrying the **Copilot Requests** permission, exported as `COPILOT_GITHUB_TOKEN`.

    Full steps: [Installing GitHub Copilot CLI](https://docs.github.com/en/copilot/how-tos/copilot-cli/set-up-copilot-cli/install-copilot-cli). For the deep version, including models, permission patterns, and headless use in GitHub Actions, do the [Copilot CLI module](modules/copilot-cli.md).

=== "Copilot app"

    The desktop app. It's built on Copilot CLI, so your instructions, skills, custom agents, and MCP servers work in it unchanged.

    1. Download from the [GitHub Copilot app page](https://github.com/features/ai/github-app), or take a build from [`github/app` releases](https://github.com/github/app/releases). There are macOS (Apple silicon and Intel), Windows (x64 and ARM), and Linux AppImage assets.
    2. Launch it and click **Sign in to GitHub**. For GitHub Enterprise Server, choose **Use GitHub Enterprise** and enter your server address.
    3. Add a project, or skip and do it later.

    You need a GitHub account and git. A Copilot plan is not strictly required, because the app supports bringing your own model provider.

    !!! note "It's a technical preview, and a separate policy"
        On an org or enterprise seat, the **GitHub Copilot app policy** has to be enabled. It's on by default, and it's separate from the Copilot CLI policy, so having the CLI enabled tells you nothing about the app.

    Projects, worktree-backed parallel sessions, and canvases are covered in the [GitHub Copilot app module](modules/copilot-app.md).

=== "GitHub.com"

    Nothing to install. Copilot Chat and the cloud agent are available on GitHub.com wherever you're signed in with a Copilot plan.

    Worth knowing for this course: custom instructions, custom agents, and agent skills all work on GitHub.com, so the files you write in the labs travel to the cloud agent and to Copilot code review. Prompt files and subagents don't.

## Feature comparison

Two things matter here, and GitHub publishes them in two different tables. Both are reproduced below.

### General Copilot features

VS Code against the two IDEs Java developers actually use. From GitHub's [Copilot feature matrix](https://docs.github.com/en/copilot/reference/copilot-feature-matrix#features-by-ide), latest stable version of each.

**Key:** ✓ supported &nbsp;·&nbsp; ✗ not supported &nbsp;·&nbsp; P public preview

| Feature | VS Code | JetBrains | Eclipse |
| --- | :---: | :---: | :---: |
| Chat | ✓ | ✓ | ✓ |
| Code completion | ✓ | ✓ | ✓ |
| Agent mode | ✓ | ✓ | ✓ |
| Edit mode | ✓ | ✓ | ✗ |
| Next edit suggestions | ✓ | P | P |
| Copilot code review | ✓ | ✓ | ✗ |
| Checkpoints | ✓ | ✓ | ✗ |
| Workspace indexing | ✓ | ✓ | ✓ |
| Code referencing | ✓ | ✓ | ✓ |
| MCP | ✓ | ✓ | ✓ |
| Vision | P | P | ✓ |
| BYOK | P | P | P |
| Java Upgrade Agent | P | ✗ | ✗ |

Read that as: VS Code and JetBrains are close on day-to-day coding. Eclipse is usable but loses code review, checkpoints, and edit mode. The Java Upgrade Agent, which is the one row you'd expect to favour a Java IDE, is VS Code only and still in preview.

### Customization features

This is the table that decides whether the labs on this site will work. From the [Copilot customization cheat sheet](https://docs.github.com/en/copilot/reference/customization-cheat-sheet). Copilot CLI is included because it's the escape hatch when your IDE hasn't shipped a feature yet.

| Feature | VS Code | JetBrains | Eclipse | Copilot CLI |
| --- | :---: | :---: | :---: | :---: |
| [Custom instructions](modules/custom-instructions.md) | ✓ | P | P | ✓ |
| [Agent skills](modules/agent-skills.md) | ✓ | P | ✗ | ✓ |
| [Custom agents](modules/custom-agents.md) | ✓ | P | P | ✓ |
| Subagents | ✓ | P | P | ✓ |
| Prompt files | ✓ | P | ✗ | ✗ |
| Hooks | P | ✗ | ✗ | ✓ |
| MCP servers | ✓ | ✓ | ✓ | ✓ |

!!! note "Two official tables, one disagreement"
    The cheat sheet lists custom agents in Eclipse as preview; the feature matrix lists them as supported in Eclipse 0.13.0 and later. Both are official GitHub pages. Treat Eclipse custom agents as "probably works, check your plugin version."

The **GitHub Copilot app** isn't in either official table because it's a technical preview, but it's built on Copilot CLI, so read it as the CLI column: instructions, skills, custom agents, and MCP servers all work in it unchanged.

### What this means in practice

| If you're on | Do this |
| --- | --- |
| VS Code | Everything on this site works. Nothing to think about. |
| IntelliJ IDEA | The plugin covers the labs, in preview. Use the **Agent Customizations editor** (Chat panel → settings icon → **Customizations**) to manage instructions, skills, agents, and prompt files from one place. |
| Eclipse | Do the [custom instructions](modules/custom-instructions.md) module in Eclipse if you like. Run the skills and agents modules in Copilot CLI from the Eclipse terminal. |
| Any IDE, features missing | Install Copilot CLI. It supports every customization type in the table, reads the same files from the same repo, and runs in your IDE's terminal. |

The customization files themselves are just Markdown in `.github/`. They're checked into the repo, not into an IDE, so a team can mix IntelliJ, VS Code, and CLI and still share one set of instructions.

## Confirm it works

Whatever you installed, the check is the same: ask it something only this repo can answer.

```
What does BookController do?
```

A real answer that names actual methods means you're set. A generic answer about Spring controllers means Copilot isn't seeing your workspace.

In VS Code, also check [Workspace Trust](https://code.visualstudio.com/docs/editing/workspaces/workspace-trust).
Restricted Mode disables agents and limits features that execute project code. Review the repository
before granting trust. Once trusted, [agent approvals and permissions](https://code.visualstudio.com/docs/agents/run/approvals)
control which tools and terminal commands can run.

[Setup](setup.md) has the per-harness version of this check, plus cloning and running the app.

## Keeping this page honest

Support levels move fast, and preview rows in particular flip within weeks. The tables above are a snapshot. The live sources, in order of usefulness:

| Source | What it covers |
| --- | --- |
| [Copilot feature matrix](https://docs.github.com/en/copilot/reference/copilot-feature-matrix) | Every feature, per IDE, and per IDE **version** |
| [Customization cheat sheet](https://docs.github.com/en/copilot/reference/customization-cheat-sheet) | Instructions, skills, agents, hooks, MCP, per surface |
| [Custom instructions support](https://docs.github.com/en/copilot/reference/custom-instructions-support) | Which *types* of instruction file work where |
| [Install the Copilot extension](https://docs.github.com/en/copilot/how-tos/set-up/install-copilot-extension) | Install steps for every IDE, with a tool switcher |

GitHub's standing advice, and it's the reason half the preview rows above resolve themselves: run the latest stable IDE, the latest Copilot extension, and the latest Copilot CLI.

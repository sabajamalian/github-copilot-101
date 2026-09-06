# Resources

References for the labs, grouped by the task you're doing. GitHub Docs, Microsoft Learn, and
Microsoft's VS Code documentation are the primary sources for product behavior. Use the
surface-specific documentation when a setting or permission behaves differently across tools.

Training modules provide guided practice, sometimes in a different programming language.
Marketplace listings, community examples, and the open skills specification are labeled separately;
being hosted on GitHub doesn't make a resource official product documentation.

## Start here

| Link | Why |
| --- | --- |
| [Copilot customization cheat sheet](https://docs.github.com/en/copilot/reference/customization-cheat-sheet) | The support matrix. Which customization works on which surface. Check this before you build anything. |
| [Copilot feature matrix](https://docs.github.com/en/copilot/reference/copilot-feature-matrix) | Every Copilot feature, per IDE and per IDE version. Public preview, updated often. |
| [Customization library](https://docs.github.com/en/copilot/tutorials/customization-library) | GitHub's ready-made recipes for instructions, skills, and agents |
| [VS Code: agent customization](https://code.visualstudio.com/docs/agents/concepts/customization) | Microsoft's explanation of instructions, skills, agents, tools, and hooks, with a decision table |
| [GitHub: Copilot Agents application card](https://docs.github.com/en/copilot/responsible-use/agents) | Capabilities, limitations, and responsible use of the CLI, app, cloud agent, and code review |

## Installing Copilot

| Link | Surface |
| --- | --- |
| [Install the Copilot extension in your environment](https://docs.github.com/en/copilot/how-tos/set-up/install-copilot-extension) | Every IDE, with a tool switcher |
| [Set up Copilot in VS Code](https://code.visualstudio.com/docs/copilot/setup) | VS Code |
| [Using Copilot in JetBrains IDEs](https://docs.github.com/en/copilot/concepts/agents/copilot-in-jetbrains) | JetBrains, compares plugin vs AI Assistant vs CLI |
| [GitHub Copilot plugin](https://plugins.jetbrains.com/plugin/17718-github-copilot) | JetBrains Marketplace, version compatibility |
| [Install Copilot CLI](https://docs.github.com/en/copilot/how-tos/copilot-cli/set-up-copilot-cli/install-copilot-cli) | CLI, all package managers |
| [GitHub Copilot app quickstart](https://docs.github.com/en/copilot/get-started/quickstart-copilot-app) | Copilot app, install and first session |
| [Copilot plans and access](https://docs.github.com/en/copilot/get-started/what-is-github-copilot#get-access) | Prerequisite for all of the above |

## Java workspace setup

Use these alongside [Installation](installation.md) and [Setup](setup.md). The repo already contains
the Spring Boot application; you don't need to generate another project.

| Reference | What it covers |
| --- | --- |
| [Microsoft Learn: Microsoft Build of OpenJDK downloads](https://learn.microsoft.com/en-us/java/openjdk/download) | JDK distributions for supported platforms, including the JDK 21 used by the labs |
| [VS Code: Java build tools](https://code.visualstudio.com/docs/java/java-build) | Discover Maven projects, run goals, and inspect dependencies |
| [VS Code: Spring Boot](https://code.visualstudio.com/docs/java/java-spring-boot) | Spring extensions and running and debugging the application |
| [VS Code: testing Java](https://code.visualstudio.com/docs/java/java-testing) | Run, debug, and inspect Java tests from the editor |
| [VS Code: Workspace Trust](https://code.visualstudio.com/docs/editing/workspaces/workspace-trust) | Restricted Mode and when project code and agents are allowed to run |

## Prompting and choosing context

These support the [custom instructions lab](modules/custom-instructions.md) and help when a
lab prompt produces a generic answer or misses a requirement.

| Reference | What it covers |
| --- | --- |
| [GitHub: prompt engineering for Copilot Chat](https://docs.github.com/en/copilot/concepts/prompting/prompt-engineering) | Specific requirements, examples, relevant files, and breaking a task into smaller steps |
| [VS Code: prompt files](https://code.visualstudio.com/docs/agent-customization/prompt-files) | Manually invoked reusable prompts, how they differ from skills, and agent-host limitations |
| [Microsoft Learn: prompt engineering with Copilot](https://learn.microsoft.com/en-us/training/modules/introduction-prompt-engineering-with-github-copilot/) | Guided practice in giving context, writing prompts, and refining results |

## Copilot CLI

| Link | What it's good for |
| --- | --- |
| [About Copilot CLI](https://docs.github.com/en/copilot/concepts/agents/copilot-cli/about-copilot-cli) | The concept, what it can and can't do |
| [Quickstart](https://docs.github.com/en/copilot/get-started/cli-quickstart) | Install, log in, first session |
| [Install Copilot CLI](https://docs.github.com/en/copilot/how-tos/copilot-cli/set-up-copilot-cli/install-copilot-cli) | Every install method and its prerequisites |
| [Authenticate](https://docs.github.com/en/copilot/how-tos/copilot-cli/set-up-copilot-cli/authenticate-copilot-cli) | Token precedence, PAT requirements, GHE hosts |
| [Configure](https://docs.github.com/en/copilot/how-tos/copilot-cli/set-up-copilot-cli/configure-copilot-cli) | `settings.json`, models, MCP, logging |
| [Using Copilot CLI](https://docs.github.com/en/copilot/how-tos/copilot-cli/use-copilot-cli/overview) | Sessions, prompts, input prefixes |
| [Allowing tools](https://docs.github.com/en/copilot/how-tos/copilot-cli/use-copilot-cli/allowing-tools) | Permission pattern syntax, deny beats allow |
| [Multiple sessions](https://docs.github.com/en/copilot/how-tos/copilot-cli/use-copilot-cli/work-with-multiple-sessions) | Running work side by side |
| [Connecting to VS Code](https://docs.github.com/en/copilot/how-tos/copilot-cli/use-copilot-cli/connecting-vs-code) | `/ide` and what it gives the agent |
| [Run programmatically](https://docs.github.com/en/copilot/how-tos/copilot-cli/automate-copilot-cli/run-cli-programmatically) | Headless flags, `-p`, piping, output formats |
| [Automate with Actions](https://docs.github.com/en/copilot/how-tos/copilot-cli/automate-copilot-cli/automate-with-actions) | Workflow structure, scheduling, and a PAT-based example |
| [Use Copilot CLI with `GITHUB_TOKEN`](https://docs.github.com/en/copilot/how-tos/copilot-cli/use-copilot-cli-in-actions) | The `copilot-requests: write` permission and organization policy for the lab workflow |
| [Copilot CLI in GitHub Actions](https://docs.github.com/en/copilot/concepts/agents/copilot-cli/copilot-cli-in-github-actions) | Billing, org policy, when to use gh-aw instead |
| [CLI command reference](https://docs.github.com/en/copilot/reference/copilot-cli-reference/cli-command-reference) | Every flag and slash command |
| [Programmatic reference](https://docs.github.com/en/copilot/reference/copilot-cli-reference/cli-programmatic-reference) | Headless flags in one table |
| [Config directory reference](https://docs.github.com/en/copilot/reference/copilot-cli-reference/cli-config-dir-reference) | What lives in `~/.copilot` |
| [Supported models](https://docs.github.com/en/copilot/reference/ai-models/supported-models) | Which models each surface offers |
| [github/copilot-cli](https://github.com/github/copilot-cli) | Release notes and issues |

## GitHub Copilot app

| Link | What it's good for |
| --- | --- |
| [About the GitHub Copilot app](https://docs.github.com/en/copilot/concepts/agents/github-copilot-app) | The concept, platforms, plan and policy requirements |
| [Quickstart](https://docs.github.com/en/copilot/get-started/quickstart-copilot-app) | Install, sign in, add a project, first session |
| [Agent sessions](https://docs.github.com/en/copilot/how-tos/github-copilot-app/agent-sessions) | Session types, modes, models, housekeeping |
| [Canvas extensions](https://docs.github.com/en/copilot/how-tos/github-copilot-app/working-with-canvas-extensions) | What canvases are and how to build one |
| [Repository configuration](https://docs.github.com/en/copilot/reference/github-copilot-app-reference/repository-configuration) | The `github-app.yml` schema and trust model |
| [Slash commands](https://docs.github.com/en/copilot/reference/github-copilot-app-reference/slash-commands) | Every command the app supports |
| [Customize the app](https://docs.github.com/en/copilot/how-tos/github-copilot-app/customize-github-copilot-app) | Skills, MCP, plugins, agents |
| [Issues and pull requests](https://docs.github.com/en/copilot/how-tos/github-copilot-app/managing-issues-and-pull-requests) | My work, PR review, agent merge |
| [Automations](https://docs.github.com/en/copilot/how-tos/github-copilot-app/using-automations) | Local and cloud scheduled prompts |
| [Deep links](https://docs.github.com/en/copilot/how-tos/github-copilot-app/open-with-deep-links) | Every `ghapp://` URL shape |
| [BYOK models](https://docs.github.com/en/copilot/how-tos/github-copilot-app/use-byok-models) | Using your own provider |
| [Cloud and local sandboxes](https://docs.github.com/en/copilot/concepts/about-cloud-and-local-sandboxes) | What a cloud sandbox is and how it's billed |
| [About plugins](https://docs.github.com/en/copilot/concepts/agents/about-plugins) | The plugin format and marketplaces |
| [Supported surfaces for policies](https://docs.github.com/en/copilot/reference/supported-surfaces-for-policies) | Admins: which policy affects which surface |
| [Download](https://github.com/features/ai/github-app) | The product page |
| [github/app](https://github.com/github/app) | Release assets, changelog, issues |

## Custom instructions

| Link | Surface |
| --- | --- |
| [Response customization](https://docs.github.com/en/copilot/concepts/prompting/response-customization) | Concept, tabbed per editor |
| [Add repository instructions in your IDE](https://docs.github.com/en/copilot/how-tos/configure-custom-instructions-in-your-ide/add-repository-instructions-in-your-ide) | IDE, full `applyTo` reference |
| [Add repository instructions on GitHub.com](https://docs.github.com/en/copilot/how-tos/copilot-on-github/customize-copilot/add-custom-instructions/add-repository-instructions) | GitHub.com |
| [Add custom instructions to Copilot CLI](https://docs.github.com/en/copilot/how-tos/copilot-cli/customize-copilot/add-custom-instructions) | CLI, wider discovery model |
| [Custom instructions support](https://docs.github.com/en/copilot/reference/custom-instructions-support) | Which instruction types work where |
| [VS Code: custom instructions](https://code.visualstudio.com/docs/agent-customization/custom-instructions) | VS Code |
| [Your first custom instructions](https://docs.github.com/en/copilot/tutorials/customization-library/custom-instructions/your-first-custom-instructions) | Tutorial |

## Agent skills

| Link | Surface |
| --- | --- |
| [About agent skills](https://docs.github.com/en/copilot/concepts/agents/about-agent-skills) | Concept |
| [Add skills to Copilot CLI](https://docs.github.com/en/copilot/how-tos/copilot-cli/customize-copilot/add-skills) | CLI, full `/skills` command list |
| [Add skills for the cloud agent](https://docs.github.com/en/copilot/how-tos/copilot-on-github/customize-copilot/customize-cloud-agent/add-skills) | GitHub.com, plus `gh skill` |
| [VS Code: agent skills](https://code.visualstudio.com/docs/agent-customization/agent-skills) | VS Code, richest frontmatter reference |
| [Built-in skills](https://docs.github.com/en/copilot/reference/github-copilot-app-reference/built-in-skills) | What ships already |

## Custom agents

| Link | Surface |
| --- | --- |
| [Custom agents configuration](https://docs.github.com/en/copilot/reference/custom-agents-configuration) | Every frontmatter property, full tool alias table |
| [About custom agents (CLI)](https://docs.github.com/en/copilot/concepts/agents/copilot-cli/about-custom-agents) | CLI, how subagents work |
| [Create custom agents for CLI](https://docs.github.com/en/copilot/how-tos/copilot-cli/customize-copilot/create-custom-agents-for-cli) | CLI, the `/agent` flow |
| [About custom agents (cloud agent)](https://docs.github.com/en/copilot/concepts/agents/cloud-agent/about-custom-agents) | GitHub.com |
| [Create custom agents on GitHub.com](https://docs.github.com/en/copilot/how-tos/copilot-on-github/customize-copilot/customize-cloud-agent/create-custom-agents) | GitHub.com, org and enterprise |
| [VS Code: custom agents](https://code.visualstudio.com/docs/agent-customization/custom-agents) | VS Code, `handoffs` and the `agents` allowlist |
| [VS Code: subagents](https://code.visualstudio.com/docs/agents/run/subagents) | VS Code |
| [Your first custom agent](https://docs.github.com/en/copilot/tutorials/customization-library/custom-agents/your-first-custom-agent) | Tutorial |
| [Prepare an org for custom agents](https://docs.github.com/en/copilot/how-tos/administer-copilot/manage-for-organization/prepare-for-custom-agents) | Admins |

## Review, testing, and safe agent use

Use these while running the [skills lab](modules/agent-skills.md), the
[reviewer and builder agents](modules/custom-agents.md), and the [capstone](capstone.md).
The built-in Copilot code review service is separate from the `api-reviewer` profile you create here.

| Reference | What it covers |
| --- | --- |
| [GitHub: writing tests with Copilot](https://docs.github.com/en/copilot/tutorials/write-tests) | Unit and integration tests, edge cases, and checking generated results; examples use Python |
| [GitHub: about Copilot code review](https://docs.github.com/en/copilot/concepts/agents/code-review) | Built-in review capabilities, availability, and configuration |
| [VS Code: approvals and permissions](https://code.visualstudio.com/docs/agents/run/approvals) | Session permission levels, tool and URL approvals, and terminal command controls |
| [VS Code: trust and safety](https://code.visualstudio.com/docs/agents/concepts/trust-and-safety) | Review of generated changes, trust boundaries, and sandboxing concepts |
| [GitHub: protected branches](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-protected-branches/about-protected-branches) | Required reviews and status checks before changes can merge |

## Automation and publishing

These explain the credentials and review controls behind the
[CLI headless exercises](modules/copilot-cli.md#headless), plus this site's Pages deployment.

| Reference | What it covers |
| --- | --- |
| [GitHub Actions: workflow authentication](https://docs.github.com/en/actions/tutorials/authenticate-with-github_token) | Passing `GITHUB_TOKEN` and setting minimum workflow or job permissions |
| [GitHub Actions: using secrets](https://docs.github.com/en/actions/how-tos/write-workflows/choose-what-workflows-do/use-secrets) | Repository, environment, and organization secrets for the optional PAT-based workflow |
| [GitHub Actions: secure use reference](https://docs.github.com/en/actions/reference/security/secure-use) | Least privilege, untrusted workflow input, secret handling, and pinning actions |
| [GitHub Pages: custom workflows](https://docs.github.com/en/pages/getting-started-with-github-pages/using-custom-workflows-with-github-pages) | Build artifacts, deployment permissions, and the `github-pages` environment |

## Courses

| Course | When to use it |
| --- | --- |
| [Microsoft Learn: introduction to GitHub Copilot](https://learn.microsoft.com/en-us/training/modules/introduction-to-github-copilot/) | Before the labs, for setup, chat, completions, and troubleshooting |
| [GitHub Skills: customize your Copilot experience](https://github.com/skills/customize-your-github-copilot-experience) | Another hands-on customization course |
| [Microsoft Learn: configure instructions and custom agents](https://learn.microsoft.com/en-us/training/modules/configure-customize-github-copilot-visual-studio-code/) | Instructions, agents, and handoffs in VS Code; assumes C# experience |
| [Microsoft Learn: Copilot agent mode](https://learn.microsoft.com/en-us/training/modules/github-copilot-agent-mode/) | Guided practice working with agent mode |
| [Microsoft Learn: develop unit tests using Copilot](https://learn.microsoft.com/en-us/training/modules/develop-unit-tests-using-github-copilot-tools/) | Edge-case test generation and execution; exercises use C# and .NET |
| [Microsoft Learn: code reviews and pull requests](https://learn.microsoft.com/en-us/training/modules/code-reviews-pull-requests-github-copilot/) | Interpret review feedback and combine it with human judgment and testing |
| [Microsoft Learn: responsible AI with Copilot](https://learn.microsoft.com/en-us/training/modules/responsible-ai-with-github-copilot/) | Limitations, risk mitigation, and accountability for generated output |
| [Microsoft Learn: GitHub Copilot learning path](https://learn.microsoft.com/en-us/training/paths/copilot/) | Broader study beyond these labs |

## Video

Everything below is from the [official GitHub channel](https://www.youtube.com/@GitHub). The two marked **embedded** are the ones playing on the module pages.

### Covers all three topics

| Link | Length | Note |
| --- | --- | --- |
| [How to use agents, skills, and instructions in Copilot CLI](https://www.youtube.com/watch?v=-yKALFS5ewY) | 6 min | **Embedded** on agent skills and custom agents. The single best overview, CLI-flavoured. |
| [GitHub Copilot playlist](https://www.youtube.com/playlist?list=PL0lo9MOBetEHEHi9h0k_lPn0XZdEeYZDS) | Playlist | Everything Copilot, newest first |

### Custom instructions

| Link | Length | Note |
| --- | --- | --- |
| [Your codebase, your rules: Customizing Copilot with context engineering](https://www.youtube.com/watch?v=0jEzUhU8bLc) | 16 min | **Embedded** on the custom instructions module. VS Code, goes wider than instructions alone. |
| [Create GitHub Copilot instructions in just one click](https://www.youtube.com/watch?v=wOV0ebsqb88) | 1 min | The "Generate instructions" button, which writes a starting file for you |
| [Rubber Duck Thursdays: the one with custom instructions](https://www.youtube.com/watch?v=_DOrUBjJgUw) | 1 hr 41 min | Livestream, unedited |

### Agent skills

| Link | Length | Note |
| --- | --- | --- |
| [RDT: Building agent Skills and the new MCP release](https://www.youtube.com/watch?v=2AZJbyD-9ME) | 1 hr | Livestream. Builds a skill from nothing. |
| [How to extend Copilot code review with MCP and custom skills](https://www.youtube.com/watch?v=JRDN_-4E9ts) | 3 min | Skills applied to code review rather than authoring |

### Custom agents

| Link | Length | Note |
| --- | --- | --- |
| [RDT: Let's build with custom agents](https://www.youtube.com/watch?v=-eRjenGV2OM) | 1 hr 49 min | Livestream, part one |
| [RDT: Let's build with custom agents (again!)](https://www.youtube.com/watch?v=X9jbNK1006E) | 2 hr 2 min | Livestream, part two |
| [How to get a multi-agent code review in Copilot CLI](https://www.youtube.com/watch?v=qRXztN1hi1M) | 39 sec | The `/review` command, close cousin of the reviewer agent you build |

### Getting the tools running

| Link | Length | Note |
| --- | --- | --- |
| [Getting started with GitHub Copilot CLI](https://www.youtube.com/watch?v=BDxRhhs36ns) | 4 min | Install, authenticate, first prompt. Pairs with [Setup](setup.md). |
| [Ultimate GitHub Copilot CLI tutorial for beginners](https://www.youtube.com/watch?v=rheqk-L7Yes) | 24 min | The whole beginner series stitched together |

Several popular "Copilot custom instructions" videos are excellent but come from personal channels rather than GitHub, so they're left to you to find.

## Community

The [customization library](https://docs.github.com/en/copilot/tutorials/customization-library) above
is part of GitHub Docs. Awesome Copilot is a community-contributed collection hosted in the GitHub
organization, separate from the product documentation. Read each file before installing it,
particularly a skill carrying `allowed-tools: shell`.

| Link | Note |
| --- | --- |
| [Awesome GitHub Copilot](https://awesome-copilot.github.com/) | The gallery. Browse and filter community agents, instructions, skills, plugins, and canvas extensions, then copy or install straight from the page. |
| [Learning Hub](https://awesome-copilot.github.com/learning-hub/) | Articles and guides on the same site |
| [github/awesome-copilot](https://github.com/github/awesome-copilot) | The repo behind the gallery. Read the raw file here before installing, or open a PR to contribute one. Also one of the Copilot app's default plugin marketplaces. |

## Open specification

| Reference | Scope |
| --- | --- |
| [Agent Skills specification](https://github.com/agentskills/agentskills) | The cross-tool open standard linked from GitHub's skills documentation. Consult the GitHub or VS Code references above for product-specific support. |

## Also on this site

- [Installation](installation.md)
- [Setup](setup.md)
- [Copilot CLI](modules/copilot-cli.md)
- [GitHub Copilot app](modules/copilot-app.md)
- [Custom instructions](modules/custom-instructions.md)
- [Agent skills](modules/agent-skills.md)
- [Custom agents](modules/custom-agents.md)
- [Capstone](capstone.md)

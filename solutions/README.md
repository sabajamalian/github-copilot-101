# Solutions

Finished files from every module, so you can skip ahead or unstick yourself.

Nothing here is loaded automatically. Copy what you want into `.github/`, or in the case of
`copilot-cli/`, run it where you like.

## Layout

```text
solutions/
├── copilot-cli/
│   ├── README.md
│   ├── daily-review.sh                 → run it from anywhere
│   └── copilot-summary.yml             → .github/workflows/ in YOUR repo
├── copilot-app/
│   └── github-app.yml                  → .github/github-app.yml
├── custom-instructions/
│   ├── copilot-instructions.md         → .github/copilot-instructions.md
│   └── instructions/
│       ├── java-api.instructions.md    → .github/instructions/
│       └── tests.instructions.md       → .github/instructions/
├── agent-skills/
│   └── skills/
│       ├── add-endpoint/SKILL.md       → .github/skills/add-endpoint/
│       └── smoke-test/                 → .github/skills/smoke-test/
│           ├── SKILL.md
│           └── smoke.sh
└── custom-agents/
    └── agents/
        ├── api-reviewer.agent.md       → .github/agents/
        └── feature-builder.agent.md    → .github/agents/
```

## Copy one module

```bash
# Copilot CLI
cp solutions/copilot-cli/daily-review.sh .
chmod +x daily-review.sh
# The workflow deliberately stays out of .github/workflows/ here. See
# solutions/copilot-cli/README.md before copying it into your own repo.

# GitHub Copilot app
mkdir -p .github
cp solutions/copilot-app/github-app.yml .github/github-app.yml
# Then accept the configuration in the app when it prompts you.

# Custom instructions
mkdir -p .github/instructions
cp solutions/custom-instructions/copilot-instructions.md .github/copilot-instructions.md
cp solutions/custom-instructions/instructions/*.instructions.md .github/instructions/

# Agent skills
mkdir -p .github/skills
cp -r solutions/agent-skills/skills/* .github/skills/
chmod +x .github/skills/smoke-test/smoke.sh

# Custom agents
mkdir -p .github/agents
cp solutions/custom-agents/agents/*.agent.md .github/agents/
```

## Copy all the customizations

```bash
mkdir -p .github/instructions .github/skills .github/agents
cp solutions/custom-instructions/copilot-instructions.md .github/copilot-instructions.md
cp solutions/custom-instructions/instructions/*.instructions.md .github/instructions/
cp -r solutions/agent-skills/skills/* .github/skills/
cp solutions/custom-agents/agents/*.agent.md .github/agents/
chmod +x .github/skills/smoke-test/smoke.sh
```

## Then reload

- **VS Code:** `Cmd+Shift+P` / `Ctrl+Shift+P` then **Developer: Reload Window**
- **Copilot CLI:** `/skills reload` picks up skills. Agents and instructions need a restart.

## A note on mixing

You can combine any of these, but they overlap on purpose. `feature-builder.agent.md` and
`java-api.instructions.md` both say "no entities on the wire", because each module has to work
on its own. Running them together is fine, just redundant.

Full walkthroughs: <https://sabajamalian.github.io/github-copilot-101/>

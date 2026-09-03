# Copilot CLI

Finished files from the [Copilot CLI module](https://sabajamalian.github.io/github-copilot-101/modules/copilot-cli/).

Unlike the other solution folders, these don't go in `.github/`. One is a script you run yourself,
the other is a workflow you copy into your own repository.

## `daily-review.sh`

Summarizes commits since a point in time and flags any that changed production code under
`library-api/src/main/java` without touching a test.

```bash
cp solutions/copilot-cli/daily-review.sh .
chmod +x daily-review.sh
./daily-review.sh              # since midnight
./daily-review.sh "3 days ago" # any git --since expression
```

It is deliberately narrow: `--allow-tool='shell(git:*)'` and nothing else, `--no-ask-user` so it
can't block, `-s` so you get the response and no session chrome. That's the shape to copy for
anything unattended.

## `copilot-summary.yml`

An example GitHub Actions workflow.

**It lives here on purpose, not in `.github/workflows/`.** A real workflow in this repository would
run for everyone who forks the course and spend their AI credits. To try it:

```bash
mkdir -p .github/workflows
cp solutions/copilot-cli/copilot-summary.yml .github/workflows/
```

Then run it from the **Actions** tab.

The part people miss is the workflow permission:

```yaml
permissions:
  contents: read
  copilot-requests: write
```

Without `copilot-requests: write`, the built-in `GITHUB_TOKEN` can't make Copilot requests and the
job fails on auth with an error that doesn't mention permissions.

To bill a run to a specific person's seat instead of the organization, swap the `env` block for a
fine-grained PAT with the **Copilot Requests** permission:

```yaml
env:
  COPILOT_GITHUB_TOKEN: ${{ secrets.COPILOT_TOKEN }}
```

Classic PATs (`ghp_`) are not supported. Create a fine-grained one at
<https://github.com/settings/personal-access-tokens/new>.

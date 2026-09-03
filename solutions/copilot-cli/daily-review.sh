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
